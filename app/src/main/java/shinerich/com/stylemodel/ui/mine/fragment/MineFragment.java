package shinerich.com.stylemodel.ui.mine.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecloud.pulltozoomview.PullToZoomScrollViewEx;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.api.UserService;
import shinerich.com.stylemodel.base.SimpleFragment;
import shinerich.com.stylemodel.bean.BaseResponse;
import shinerich.com.stylemodel.bean.NightModeEvent;
import shinerich.com.stylemodel.bean.UserInfo;
import shinerich.com.stylemodel.engin.RemoteLogin;
import shinerich.com.stylemodel.network.RetrofitClient;
import shinerich.com.stylemodel.ui.login.LoginSelectActivity;
import shinerich.com.stylemodel.ui.mine.AccountSettingActivity;
import shinerich.com.stylemodel.ui.mine.FeedbackActivity;
import shinerich.com.stylemodel.ui.mine.MyCollectActivity;
import shinerich.com.stylemodel.ui.mine.MyMessageActivity;
import shinerich.com.stylemodel.ui.mine.MySettingActivity;
import shinerich.com.stylemodel.ui.mine.UserInfoActivity;
import shinerich.com.stylemodel.utils.GlideUtils;
import shinerich.com.stylemodel.utils.MLog;
import shinerich.com.stylemodel.utils.RxBus;
import shinerich.com.stylemodel.utils.ThemeHelper;

/**
 * 我的-Fragment
 *
 * @author hunk
 */
public class MineFragment extends SimpleFragment implements View.OnClickListener {

    private GlideUtils glideUtils = GlideUtils.getInstance();
    private View rootView;
    private UserInfo mUserInfo;
    @BindView(R.id.scroll_view)
    PullToZoomScrollViewEx scrollView;
    RelativeLayout rl_collect;
    RelativeLayout rl_msg;
    RelativeLayout rl_suggest;
    RelativeLayout rl_setting;
    RelativeLayout rl_model;
    RelativeLayout rl_account;
    View v_account;
    ImageView iv_user_head;
    TextView tv_user_name;
    ImageView iv_zoom;
    ImageView iv_dot;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initEventAndData() {
        //初始化视图
        initView();
        //设置事件监听器
        setListener();


    }

    /**
     * 初始化视图
     */
    public void initView() {
        View headView = View.inflate(mContext, R.layout.view_mine_head, null);
        View zoomView = View.inflate(mContext, R.layout.view_mine_zoom, null);
        View contentView = View.inflate(mContext, R.layout.view_mine_content, null);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);
        scrollView.setHeaderView(headView);
        rootView = scrollView.getPullRootView();

        rl_collect = (RelativeLayout) rootView.findViewById(R.id.rl_collect);
        rl_msg = (RelativeLayout) rootView.findViewById(R.id.rl_msg);
        rl_suggest = (RelativeLayout) rootView.findViewById(R.id.rl_suggest);
        rl_setting = (RelativeLayout) rootView.findViewById(R.id.rl_setting);
        rl_account = (RelativeLayout) rootView.findViewById(R.id.rl_account);
        rl_model = (RelativeLayout) rootView.findViewById(R.id.rl_model);
        iv_user_head = (ImageView) rootView.findViewById(R.id.iv_user_head);
        tv_user_name = (TextView) rootView.findViewById(R.id.tv_user_name);
        iv_dot = (ImageView) rootView.findViewById(R.id.iv_dot);
        iv_zoom = (ImageView) rootView.findViewById(R.id.iv_zoom);
        v_account = rootView.findViewById(R.id.v_account);
        //未登录
        if (getUser() == null) {
            v_account.setVisibility(View.GONE);
            rl_account.setVisibility(View.GONE);
        } else {
            v_account.setVisibility(View.VISIBLE);
            rl_account.setVisibility(View.VISIBLE);
        }

    }


    public void setListener() {

        rl_collect.setOnClickListener(this);
        rl_suggest.setOnClickListener(this);
        rl_setting.setOnClickListener(this);
        rl_msg.setOnClickListener(this);
        iv_user_head.setOnClickListener(this);
        rl_model.setOnClickListener(this);
        rl_account.setOnClickListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        //更新用户信息
        updateUserInfo();
        //获取去用户数量
        getUserMsgNum();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //头像
            case R.id.iv_user_head:
                if (getUser() != null && mUserInfo != null) {
                    Intent userIntent = new Intent(mContext, UserInfoActivity.class);
                    userIntent.putExtra("info", mUserInfo);
                    startActivity(userIntent);

                } else {
                    startIntent(mContext, LoginSelectActivity.class);
                }
                break;
            //我的收藏
            case R.id.rl_collect:
                if (getUser() != null && mUserInfo != null) {
                    startIntent(mContext, MyCollectActivity.class);
                } else {
                    startIntent(mContext, LoginSelectActivity.class);
                }

                break;
            //我的消息
            case R.id.rl_msg:
                if (getUser() != null && mUserInfo != null) {
                    startIntent(mContext, MyMessageActivity.class);
                } else {
                    startIntent(mContext, LoginSelectActivity.class);
                }

                break;
            //建议
            case R.id.rl_suggest:
                startIntent(mContext, FeedbackActivity.class);
                break;
            //设置
            case R.id.rl_setting:
                startIntent(mContext, MySettingActivity.class);
                break;

            //账号设置
            case R.id.rl_account:
                startIntent(mContext, AccountSettingActivity.class);
                break;
            //切换模式
            case R.id.rl_model:
                switchModel();
                break;
        }
    }


    /**
     * 切换模式
     */
    public void switchModel() {
        //showToast("夜间模式");
        rl_model.setSelected(!ThemeHelper.isNightMode(getActivity()));

        NightModeEvent event = new NightModeEvent();
        event.setNightMode(ThemeHelper.isNightMode(getActivity()));
        RxBus.getInstance().post(event);


    }


    /**
     * 更新用户信息
     */
    public void updateUserInfo() {

        // 已登录
        if (getUser() != null) {

            String uid = getUser().getId();
            String key = getUser().getKey();

            RetrofitClient client = RetrofitClient.getInstance();
            UserService userService = client.create(UserService.class);
            Call<BaseResponse<UserInfo>> call = userService.getUserInfo(uid, key);
            call.enqueue(new Callback<BaseResponse<UserInfo>>() {
                @Override
                public void onResponse(Call<BaseResponse<UserInfo>> call, Response<BaseResponse<UserInfo>> response) {
                    final BaseResponse<UserInfo> body = response.body();

                    if (body.getCode() == 0) {
                        mUserInfo = body.getData();

                        if (mUserInfo != null) {
                            MLog.i("msg", mUserInfo.toString() + "");

                            //头像
                            glideUtils.loadRoundImage(mContext, mUserInfo.getUsericon(), new GlideUtils.OnDownLoadBitmapListener() {
                                @Override
                                public void getBitmap(Bitmap bitmap) {
                                    iv_zoom.setImageBitmap(bitmap);
                                    iv_zoom.setVisibility(View.VISIBLE);
                                    iv_user_head.setImageBitmap(bitmap);

                                }
                            });


                            //姓名
                            tv_user_name.setText(mUserInfo.getNickname());
                        }

                    } else if (body.getCode() == 200) {
                        //被踢
                        new RemoteLogin().remoteLoginToDo(mActivity, false);

                    } else {
                        showToast(body.getMsg());
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<UserInfo>> call, final Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            glideUtils.cleanViewRequest(iv_user_head);
            iv_user_head.setImageResource(R.drawable.user_img_def);
            glideUtils.cleanViewRequest(iv_zoom);
            iv_zoom.setImageDrawable(new ColorDrawable(Color.parseColor("#ccf2f2f2")));
            tv_user_name.setText("点击头像登录");
        }


    }

    /**
     * 获取用户消息数量
     */
    public void getUserMsgNum() {
        String uid = "", key = "";
        //已登录
        if (getUser() != null) {
            uid = getUser().getId();
            key = getUser().getKey();
            RetrofitClient client = RetrofitClient.getInstance();
            UserService userService = client.create(UserService.class);
            Call<BaseResponse<String>> call = userService.getUserMsgNum(uid, key);
            call.enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    BaseResponse<String> body = response.body();
                    if (body.getCode() == 0) {
                        String data = body.getData();
                        int num = Integer.parseInt(data);
                        if (num > 0) {
                            iv_dot.setVisibility(View.VISIBLE);
                        } else {
                            iv_dot.setVisibility(View.GONE);
                        }


                    } else {
                        showToast(body.getMsg());
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {

                }
            });


        }


    }

}
