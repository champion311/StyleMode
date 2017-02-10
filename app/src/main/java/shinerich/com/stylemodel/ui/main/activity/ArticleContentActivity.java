package shinerich.com.stylemodel.ui.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bilibili.magicasakura.utils.ThemeUtils;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.ui.widget.DanmakuView;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.base.BaseActivity;
import shinerich.com.stylemodel.bean.ArticleContentBean;
import shinerich.com.stylemodel.bean.Danmu;
import shinerich.com.stylemodel.bean.ShareBean;
import shinerich.com.stylemodel.common.ShareDialog;
import shinerich.com.stylemodel.engin.LoginUserProvider;
import shinerich.com.stylemodel.inject.component.ActivityComponent;
import shinerich.com.stylemodel.network.WebSocketManager;
import shinerich.com.stylemodel.presenter.ArticleContentPresenter;
import shinerich.com.stylemodel.presenter.contract.ArticleContentContract;
import shinerich.com.stylemodel.ui.login.LoginSelectActivity;
import shinerich.com.stylemodel.utils.DanmuController;
import shinerich.com.stylemodel.utils.DensityUtils;
import shinerich.com.stylemodel.utils.KeyBoardUtils;
import shinerich.com.stylemodel.utils.NetWorkUtils;
import shinerich.com.stylemodel.utils.ThemeHelper;
import shinerich.com.stylemodel.utils.ToastUtils;


/**
 * Created by Administrator on 2016/10/31.
 */
public class ArticleContentActivity extends
        BaseActivity<ArticleContentPresenter> implements
        ArticleContentContract.View,
        DanmuController.OnDanmuClickListener,
        SurfaceHolder.Callback, View.OnTouchListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener,
        SeekBar.OnSeekBarChangeListener, Runnable, MediaPlayer.OnSeekCompleteListener {


    @BindView(R.id.mWebView)
    WebView mWebView;
    @BindView(R.id.mViewStub)
    ViewStub mViewStub;

    View dialogView;
    @BindView(R.id.back_icon)
    ImageView backIcon;
    @BindView(R.id.danmakuView)
    DanmakuView danmakuView;
    @BindView(R.id.input_edit)
    EditText inputEdit;
    @BindView(R.id.comment_icon)
    ImageView commentIcon;
    @BindView(R.id.comment_number)
    TextView commentNumber;
    @BindView(R.id.collect_btn)
    ImageView collectBtn;
    @BindView(R.id.danmu_btn)
    ImageView danmuBtn;
    @BindView(R.id.input_parent)
    FrameLayout inputParent;
    @BindView(R.id.bottom_input_view)
    LinearLayout bottmInputView;
    @BindView(R.id.share_btn)
    View shareBtn;

    @BindView(R.id.mContentParent)
    RelativeLayout mContentParent;
    @BindView(R.id.mSurfaceView)
    SurfaceView mSurfaceView;
    @BindView(R.id.mProgressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.net_alert)
    TextView netAlert;
    @BindView(R.id.im_back)
    ImageView imBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.top_wrapper)
    RelativeLayout topWrapper;
    @BindView(R.id.pause_play_btn)
    ImageView pausePlayBtn;
    @BindView(R.id.video_time)
    TextView videoTime;
    @BindView(R.id.viedo_progressbar)
    SeekBar videoProgressbar;
    @BindView(R.id.changeOriButton)
    ImageView changeOriButton;
    @BindView(R.id.bottom_wrapper)
    LinearLayout bottomWrapper;
    @BindView(R.id.video_play_parent)
    RelativeLayout videoPlayParent;
    @BindView(R.id.comment_wrapper)
    RelativeLayout commentWrapper;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.total_time)
    TextView totalTime;
    @BindView(R.id.mBlack_bg)
    ImageView mBlack_bg;


    //可滑动区域
    private String id;
    private String type;
    private boolean isShowDanmu = false;

    private AnimationDrawable animationDrawable;

    private DanmuController mDanmuControl;

    private List<Danmu> danmus = new ArrayList<>();

    private boolean hasScollToBottom = false;

    private boolean hasAddedDanmu = false;

    private boolean isCollected = false;

    public static final int showDanmuTag = 0x10;

    private ShareBean shareBean;

    private ShareDialog shareDialog;

    ArticleContentBean bean;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (animationDrawable != null) {
                        animationDrawable.stop();
                    }
                    dialogView.setVisibility(View.GONE);
                    break;
            }
        }
    };


    @Override
    protected int getContentViewID() {
        return R.layout.activity_article;
    }

    @Override
    protected void injectDaggger(ActivityComponent activityComponent) {
        activityComponent.inject(this);

    }


    @Override
    protected void initViewAndEvents() {
        id = getIntent().getStringExtra("id");

        type = getIntent().getStringExtra("type");
        if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(type)) {
            if (ThemeHelper.isNightMode(this)) {
                mPresenter.loadData(id, type, 1);
                setSystemBarColorValue(Color.parseColor("#454547"));
            } else {
                mPresenter.loadData(id, type, 0);
                setSystemBarColorRes(R.color.white);
            }
            mPresenter.loadDanmuData(id, type);
        }

        bottmInputView.setFocusable(true);

    }

    @Override
    public void showError() {

    }


    @Override
    public void showView(final ArticleContentBean bean) {
        this.bean = bean;
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(bean.getUrl());
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //ToastUtils.show(ArticleContentActivity.this, url);

                handleH5Test(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        dialogView = mViewStub.inflate();
        ImageView spinnerImageView = (ImageView) dialogView.findViewById(R.id.spinnerImageView);
        animationDrawable = (AnimationDrawable) spinnerImageView.getBackground();
        if (animationDrawable != null) {
            animationDrawable.start();
        }
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    handler.sendEmptyMessage(1);
                }
            }


        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mWebView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    //滑到底部的监听
                    if (scrollY - oldScrollY < 0)
                        return;
                    float webViewContentHeight = mWebView.getContentHeight() * mWebView.getScale();

                    float webViewCurrentScollHeight = mWebView.getHeight() + mWebView.getScrollY();

                    if (webViewContentHeight - webViewCurrentScollHeight < 200) {
                        hasScollToBottom = true;
//                        if (danmus.size() > 0) {
//                            //网络加载快于滑动
//                            if (hasAddedDanmu) {
//                                return;
//                            }
//                            //mDanmuControl.addDanmuList(danmus); @
//                            mDanmuControl.addDanmuListTest(danmus);
//                            hasAddedDanmu = true;
//                        }
                    }
                }
            });
        }

        mDanmuControl.setDanmakuView(danmakuView);
        mDanmuControl.setOnDanmuClicklistener(this);
        commentNumber.setText(bean.getComment_num());
        isCollected = bean.getIs_collect().equals("1");
        collectBtn.setSelected(isCollected);
        danmuBtn.setSelected(isShowDanmu);

        shareBean = new ShareBean();
        shareBean.setClickUrl(bean.getShare_url());
        shareBean.setContent(bean.getTitle());
        shareBean.setTitle(bean.getTitle());
        shareBean.setImageUrl(bean.getThumb());
        shareBean.setType("1");
        shareDialog = new ShareDialog(this);
        shareDialog.setShareData(shareBean);
        shareDialog.setCopyUrl(bean.getShare_url());

    }

    /**
     * 显示弹幕
     *
     * @param danmus
     */
    @Override
    public void showDanmuView(List<Danmu> danmus) {
        if (danmus != null && danmus.size() > 0) {
            this.danmus.addAll(danmus);
            mDanmuControl.addDanmuListTest(danmus);
        }
        if (isShowDanmu) {
            mDanmuControl.show();
        } else {
            mDanmuControl.hide();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDanmuControl.hide();
            }
        }, 50);


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDanmuControl = new DanmuController(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mDanmuControl.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDanmuControl.resume();
        if (!LoginUserProvider.currentStatus) {
            inputEdit.setFocusable(false);

        } else {
            inputEdit.setFocusable(true);
            inputEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEND) {
                        mPresenter.addComment(id, type, v.getText().toString());
                    }
                    inputEdit.clearFocus();
                    return false;
                }
            });
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDanmuControl.destroy();
        mDanmuControl = null;
        if (player != null) {
            player.release();
            player = null;
        }


    }


    private boolean isLanscape = false;//当前是否为水平

    @OnClick({R.id.comment_icon,
            R.id.collect_btn, R.id.danmu_btn,
            R.id.comment_number, R.id.back_icon, R.id.share_btn,
            R.id.pause_play_btn, R.id.im_back, R.id.net_alert, R.id.changeOriButton,
            R.id.input_edit, R.id.comment_wrapper})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.comment_icon:
                inputEdit.setFocusable(true);
                inputEdit.requestFocus();
                break;
            case R.id.collect_btn:
                if (isCollected) {
                    mPresenter.removeCollect(id, type);
                } else {
                    mPresenter.addCollect(id, type);
                }

                break;
            case R.id.danmu_btn:
                isShowDanmu = !isShowDanmu;
                if (isShowDanmu) {
                    mDanmuControl.show();
                } else {
                    mDanmuControl.hide();
                }
                danmuBtn.setSelected(isShowDanmu);
                break;
            case R.id.comment_number:
                if (bean == null) {
                    return;
                }
                Intent intent = new Intent(ArticleContentActivity.this, CommentActivity.class);
                intent.putExtra("id", bean.getId());
                intent.putExtra("type", bean.getType());
                startActivity(intent);
                break;
            case R.id.back_icon:
                finish();
                break;
            case R.id.share_btn:
                if (shareDialog != null) {
                    shareDialog.show();
                }
                break;
            case R.id.pause_play_btn:
                pauseOrPlayAction();
                break;
            case R.id.net_alert:
                initPlayer();
                break;
            case R.id.im_back:
                //从全屏模式退出
            case R.id.changeOriButton:
                changeScreenAction();
                break;
            case R.id.input_edit:
                if (!LoginUserProvider.currentStatus) {
                    notLoginAction();
                }
                break;
            case R.id.comment_wrapper:
                if (bean == null) {
                    return;
                }
                Intent intent2 = new Intent(ArticleContentActivity.this, CommentActivity.class);
                intent2.putExtra("id", bean.getId());
                intent2.putExtra("type", bean.getType());
                startActivity(intent2);
                break;


        }
    }

    @Override
    public void setCollected(boolean hasCollected) {
        collectBtn.setSelected(hasCollected);
        isCollected = hasCollected;
    }

    @Override
    public void addUserDanmu(Danmu danmu) {
        if (mDanmuControl != null) {
            IDanmakus idanmaks = danmakuView.getCurrentVisibleDanmakus();
            danmu.setFromLocal(true);
            mDanmuControl.addDanmu(danmu, -1);
            ToastUtils.show(this, "发送弹幕成功");
            KeyBoardUtils.hideSoftKeyboard(this);
        }
    }

    @Override
    public void notLoginAction() {
        //未登录的时候访问操作
        Intent intent = new Intent(this, LoginSelectActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    @Override
    public void onDanmuClick(BaseDanmaku latest) {
        //ToastUtils.show(this, latest.userId + "");
    }


    public void handleH5Test(String url) {
//
        int index = url.indexOf("//");
        if (index == -1) {
            return;
        }
        url = url.substring(index + 2);
        //ToastUtils.show(this, url);
        int wenIndex = url.indexOf("?");
        if (wenIndex != -1) {
            String type = url.substring(0, wenIndex);
            String number = url.substring(wenIndex + 1);
            if ("listbycate".equals(type)) {
                //栏目
                Intent intent = new Intent(this, ColumnActivity.class);
                intent.putExtra("cate_id", number);
                startActivity(intent);

            } else if ("listbyeditor".equals(type)) {
                //编辑 ,博客主
                Intent intent = new Intent(this, BloggerInfoActivity.class);
                intent.putExtra("bloggers_id", number);
                startActivity(intent);

            } else if ("share".equals(type)) {
                if (shareDialog == null) {
                    return;
                }
                if ("qq".equals(number)) {
                    shareDialog.toShare(SHARE_MEDIA.QQ);
                } else if ("qqzone".equals(number)) {
                    shareDialog.toShare(SHARE_MEDIA.QZONE);
                } else if ("friendzone".equals(number)) {
                    shareDialog.toShare(SHARE_MEDIA.WEIXIN_CIRCLE);
                } else if ("weixin".equals(number)) {
                    shareDialog.toShare(SHARE_MEDIA.WEIXIN);
                } else if ("weibo".equals(number)) {
                    shareDialog.toShare(SHARE_MEDIA.SINA);
                }


            } else if ("detail".equals(type)) {
                if (number.indexOf("/") != -1) {
                    int dividerLineIndex = number.indexOf("/");
                    String id = number.substring(0, dividerLineIndex);
                    String deType = number.substring(dividerLineIndex + 1);
                    Intent intent;
                    switch (Integer.valueOf(deType)) {
                        case 0:
                            //文章
                            intent = new Intent(ArticleContentActivity.this,
                                    ArticleContentActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("type", deType);
                            startActivity(intent);

                            break;
                        case 1:
                            //图集
                            intent = new Intent(ArticleContentActivity.this,
                                    ImageListActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("type", deType);
                            startActivity(intent);


                            break;
                        case 2:
                            //视频
                            intent = new Intent(ArticleContentActivity.this,
                                    ArticleContentActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("type", deType);
                            startActivity(intent);


                            break;
                        case 3:
                            //博文
                            intent = new Intent(ArticleContentActivity.this,
                                    ArticleContentActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("type", deType);
                            startActivity(intent);

                            break;

                    }
                } else if ("login".equals(type)) {

                    //点赞。取消点赞
                }

            }
        } else {
            if ("video".equals(url)) {
//                String video_url = ArticleContentActivity.this.bean.getVideo_url();
//                Intent intent = new Intent(this, VideoPlayActivity.class);
//                intent.putExtra("video_url", video_url);
//                intent.putExtra("title", ArticleContentActivity.this.bean.getTitle());
//                startActivity(intent);
                initVideoPlayEvents();
                initPlayer();
                videoUrl = bean.getVideo_url();
                topWrapper.setVisibility(View.GONE);
                title.setText(bean.getTitle());
                ViewGroup.LayoutParams params = videoPlayParent.getLayoutParams();
                params.height = DensityUtils.dip2px(mContext, 230);
                videoPlayParent.setVisibility(View.VISIBLE);
                videoPlayParent.setLayoutParams(params);
                videoPlayParent.invalidate();
                pausePlayBtn.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);

            } else if ("morecomment".equals(url)) {
                Intent intent = new Intent(this, CommentActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("type", type);
                startActivity(intent);
            }
        }
    }

    //TODO视频处理部分


    private String videoUrl;

    private SurfaceHolder mSurfaceHolder;

    //是否准备好了
    private boolean isPrepared = false;

    // 当player未准备好，并且当前activity经过onPause()生命周期时，此值为true
    private boolean isFreeze;

    private int currentPosition = 0;// 记录数上此播放的时间

    private boolean isPlaying = false;

    private boolean isSurfaceHasDestory = false;

    private boolean isShowView = false;


    private DelayeDismiss dimissThread;

    private MediaPlayer player;


    /*
   * 自动消失wrapper的线程
   */
    private class DelayeDismiss implements Runnable {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (isShowView) {
                disMissWrapper(false);
            }
        }
    }

    /**
     * 是否显示View
     *
     * @param isShow 是否显示wrapper
     */
    public void disMissWrapper(boolean isShow) {


        isShowView = !isShowView;
        if (!isShow) {
            // 隐藏
            Animation bottomDisMiss = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.f,
                    Animation.RELATIVE_TO_SELF, 0.f,
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                    1f);
            bottomDisMiss.setDuration(500);
            bottomWrapper.startAnimation(bottomDisMiss);
            bottomDisMiss.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // TODO Auto-generated method stub
                    bottomWrapper.setVisibility(View.GONE);

                }
            });
            if (isLanscape) {
                Animation TopDimss = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 0.f,
                        Animation.RELATIVE_TO_SELF, 0.f,
                        Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                        -1f);
                TopDimss.setDuration(500);
                topWrapper.startAnimation(TopDimss);
                TopDimss.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // TODO Auto-generated method stub
                        topWrapper.setVisibility(View.GONE);
                        handler.removeCallbacks(dimissThread);

                    }
                });
            }
            pausePlayBtn.setVisibility(View.GONE);

        } else {
            // 展开
            bottomWrapper.setVisibility(View.VISIBLE);
            Animation bottomShow = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.f,
                    Animation.RELATIVE_TO_SELF, 0.f,
                    Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF,
                    0f);
            bottomShow.setDuration(500);
            bottomWrapper.startAnimation(bottomShow);
            bottomShow.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // TODO Auto-generated method stub

                }
            });
            topWrapper.setVisibility(View.VISIBLE);
            Animation TopShow = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.f,
                    Animation.RELATIVE_TO_SELF, 0.f,
                    Animation.RELATIVE_TO_SELF, -1f,
                    Animation.RELATIVE_TO_SELF, 0f);
            TopShow.setDuration(500);
            topWrapper.startAnimation(TopShow);
            TopShow.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // TODO Auto-generated method stub

                }
            });

            if (!isLanscape) {
                topWrapper.setVisibility(View.GONE);
            }
            if (isPrepared) {
                pausePlayBtn.setVisibility(View.VISIBLE);
            }

        }


    }


    public void initVideoPlayEvents() {

        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); // 2.3及以下使用，不然出现只有声音没有图像的问题
        mSurfaceHolder.addCallback(this);
        pausePlayBtn.setClickable(false);
        videoTime.setText("00:00");
        videoProgressbar.setEnabled(false);//默认无法移动,需要加载完成
        topWrapper.setVisibility(View.GONE);
        bottomWrapper.setVisibility(View.GONE);
        mSurfaceView.setOnTouchListener(this);
        dimissThread = new DelayeDismiss();

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        holder.setFixedSize(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (player == null) {
            return;
        }
        //移除线程
        handler.removeCallbacks(this);
        try {
            if (isPrepared) {
                // 记录播放的时间
                currentPosition = player.getCurrentPosition();
            } else {
                //player.reset();
                player.release();
                return;
            }
            isSurfaceHasDestory = true;
            player.pause();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        if (player != null && isPrepared) {
            videoProgressbar.setSecondaryProgress(videoProgressbar.getMax()
                    * percent / 100);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        handler.removeCallbacks(this);
        if (isPrepared) {
            player.stop();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mProgressBar.setVisibility(View.GONE);
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                if (player.isPlaying()) {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                mProgressBar.setVisibility(View.GONE);
                break;
            case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                mProgressBar.setVisibility(View.GONE);
                break;

        }
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mProgressBar.setVisibility(View.GONE);
        pausePlayBtn.setVisibility(View.GONE);
        if (!isFreeze) {
            player.start();
        }
        if (currentPosition > 0) {
            player.seekTo(currentPosition);
        }
        isPrepared = true;
        videoProgressbar.setEnabled(true);
        mBlack_bg.setVisibility(View.GONE);
        player.start();
        pausePlayBtn.setClickable(true);
        videoProgressbar.setMax(player.getDuration());
        videoProgressbar.setOnSeekBarChangeListener(this);
        handler.post(this);
        player.setOnSeekCompleteListener(this);

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            if (player != null) {
                player.seekTo(progress);
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        setTime(player.getCurrentPosition());
    }

    private long lastTimePressDownTime = 0;

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 处理连续点情况
                //ToastUtils.show(this, "ACTION_DOWN");
                if (System.currentTimeMillis() - lastTimePressDownTime <= 500) {
                    // TODO 暂停
                    lastTimePressDownTime = System.currentTimeMillis();
                    return false;
                }
                lastTimePressDownTime = System.currentTimeMillis();
                disMissWrapper(!isShowView);
                // 延时15s消失
                handler.postDelayed(dimissThread, 15 * 1000);
                break;
        }
        return false;
    }

    @Override
    public void run() {
        if (player == null || !player.isPlaying()) {
            return;
        }
        if (player.isPlaying()) {
            handler.postDelayed(this, 50);
            videoProgressbar.setProgress(player.getCurrentPosition());
            setTime(player.getCurrentPosition());
        }
    }

    public void initPlayer() {
        if (TextUtils.isEmpty(videoUrl)) {
            return;
        }
        if (!NetWorkUtils.isNetWorkConnected(this)) {
            mProgressBar.setVisibility(View.GONE);
            netAlert.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
            netAlert.setVisibility(View.GONE);
        }
        isPrepared = false;
        try {
            if (player == null) {
                player = new MediaPlayer();
            }
            player.reset();
            player.setLooping(false);
            player.setDataSource(this, Uri.parse(videoUrl));
            player.prepareAsync();
            player.setOnErrorListener(this);
            player.setOnInfoListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (isPrepared) {
            if (player != null) {
                player.start();
                handler.post(this);
            }
        }
        try {
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setOnBufferingUpdateListener(this);
            player.setOnPreparedListener(this);
            player.setOnCompletionListener(this);
            player.setDisplay(mSurfaceHolder);
            if (isSurfaceHasDestory) {
                player.prepareAsync();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停或者继续的动作
     */
    public void pauseOrPlayAction() {
        boolean selected = pausePlayBtn.isSelected();
        if (!selected) {
            // 当前播放中
            if (player.isPlaying()) {
                player.pause();
                isPlaying = false;
                handler.removeCallbacks(this);// 移除线程
            }
        } else {
            if (!player.isPlaying()) {
                try {
                    //player.prepareAsync();
                    player.start();
                    handler.post(this);
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                isPlaying = true;
            }
        }
        pausePlayBtn.setSelected(!selected);
    }


    public void setTime(int duration) {
        if (duration <= 0) {
            return;
        }
        duration /= 1000;
        String ret = "";
        if (duration > 0 && duration < 60) {
            String secondStr = duration < 10 ? "0" + duration : duration + "";
            ret = "00:" + secondStr;
        } else {
            int min = duration / 60;
            int secound = duration % 60;
            String minStr = min < 10 ? "0" + min : min + "";
            String secondStr = secound < 10 ? "0" + secound : secound + "";
            ret = minStr + ":" + secondStr;
        }
        videoTime.setText(ret);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (isLanscape) {
            changeScreenAction();
        } else {
            finish();
        }

    }

    public void changeScreenAction() {
        if (isLanscape) {
            //当前横屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
            mContentParent.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams params = videoPlayParent.getLayoutParams();
            params.height = DensityUtils.dip2px(mContext, 230);
            videoPlayParent.setLayoutParams(params);
            videoPlayParent.invalidate();
            backIcon.setVisibility(View.VISIBLE);
            changeOriButton.setEnabled(true);
            setSystemBarColorRes(R.color.white);

        } else {
            //当前竖屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            mContentParent.setVisibility(View.GONE);
            ViewGroup.LayoutParams params = videoPlayParent.getLayoutParams();
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            videoPlayParent.setLayoutParams(params);
            videoPlayParent.invalidate();
            topWrapper.setVisibility(View.VISIBLE);
            backIcon.setVisibility(View.GONE);
            changeOriButton.setEnabled(false);
            setSystemBarColorRes(R.color.transparent_black);
        }
        isLanscape = !isLanscape;
    }
}
