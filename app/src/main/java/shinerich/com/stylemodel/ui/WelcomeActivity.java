package shinerich.com.stylemodel.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.api.HomeService;
import shinerich.com.stylemodel.base.SimpleActivity;
import shinerich.com.stylemodel.bean.BaseResponse;
import shinerich.com.stylemodel.bean.HomeAdv;
import shinerich.com.stylemodel.common.DownTimerTask;
import shinerich.com.stylemodel.network.RetrofitClient;
import shinerich.com.stylemodel.ui.login.LoginSelectActivity;
import shinerich.com.stylemodel.ui.main.activity.MainAcitivity;
import shinerich.com.stylemodel.utils.GlideUtils;

/**
 * 欢迎页面
 *
 * @author hunk
 */
public class WelcomeActivity extends SimpleActivity {

    public final static int RESULT_WEBVIEW_CODE = 101; //浏览器
    private int duration = 2000;                    //时间间隔
    private DownTimerTask task;
    private boolean isInterrupt = false;


    @BindView(R.id.iv_img)
    ImageView iv_img;
    @BindView(R.id.btn_go)
    Button btn_go;
    @BindView(R.id.ll_go)
    LinearLayout ll_go;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initEventAndData() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 获取广告页
        getAdv();
    }

    /**
     * 获取广告页
     */
    public void getAdv() {
        RetrofitClient client = RetrofitClient.getInstance();
        HomeService homeService = client.create(HomeService.class);
        Call<BaseResponse<HomeAdv>> call = homeService.getAds();
        call.enqueue(new Callback<BaseResponse<HomeAdv>>() {
            @Override
            public void onResponse(Call<BaseResponse<HomeAdv>> call,
                                   Response<BaseResponse<HomeAdv>> response) {
                BaseResponse<HomeAdv> body = response.body();

                if (body.getCode() == 0) {
                    final HomeAdv adv = body.getData();
                    if (!TextUtils.isEmpty(adv.getThumbnals())) {
                        GlideUtils.getInstance().load(mContext, adv.getThumbnals(), new GlideUtils.OnDownLoadBitmapListener() {
                            @Override
                            public void getBitmap(Bitmap bitmap) {
                                iv_img.setVisibility(View.VISIBLE);
                                iv_img.setImageBitmap(bitmap);

                                //开始倒计时
                                ll_go.setVisibility(View.VISIBLE);
                                duration = 3000;
                                task = new DownTimerTask(duration, new DownTimerTask.OnCallbackListener() {
                                    @Override
                                    public void onTick(int interval) {
                                        btn_go.setText(interval + "秒");
                                    }

                                    @Override
                                    public void onFinish() {
                                        btn_go.setText("0秒");
                                    }
                                });
                                task.start();
                                // 初始化动画
                                initImageAnim();

                                //设置点击事件
                                iv_img.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (!TextUtils.isEmpty(adv.getUrl())) {
                                            isInterrupt = true;
                                            Intent intent = new Intent(mContext, WebViewActivity.class);
                                            intent.putExtra("title", adv.getTitle());
                                            intent.putExtra("url", adv.getUrl());
                                            startActivityForResult(intent, RESULT_WEBVIEW_CODE);
                                        }

                                    }
                                });


                            }
                        });
                    } else {
                        // 初始化动画
                        initImageAnim();
                    }

                } else {
                    showToast(body.getMsg());
                    // 初始化动画
                    initImageAnim();
                }


            }

            @Override
            public void onFailure(Call<BaseResponse<HomeAdv>> call, Throwable t) {
                // 初始化动画
                initImageAnim();
            }
        });

    }

    @OnClick(value = {R.id.ll_go})
    public void Onclick(View view) {
        switch (view.getId()) {
            //去首页
            case R.id.ll_go:
                isInterrupt = true;
                goHome();
                break;
        }

    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(Looper.getMainLooper()) {

        public void handleMessage(Message msg) {
            if (!isInterrupt) {
                goHome();
            }
        }

    };

    /**
     * 初始化动画
     */
    public void initImageAnim() {

        AnimationSet animationset = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(duration + 500);
        animationset.addAnimation(alphaAnimation);
        iv_img.startAnimation(animationset);
        Message msg = Message.obtain();
        handler.sendMessageDelayed(msg, duration);
    }


    /**
     * 去首页
     */
    public void goHome() {
        //登陆状态
        if (getUser() == null) {
            Intent intent = new Intent(WelcomeActivity.this,
                    LoginSelectActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(WelcomeActivity.this,
                    MainAcitivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //浏览返回
                case RESULT_WEBVIEW_CODE:
                    goHome();
                    break;

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task != null) {
            task.cancel();
        }
    }


    @Override
    public void onBackPressedSupport() {
        super.onBackPressedSupport();
        isInterrupt = true;
    }
}
