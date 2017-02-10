package shinerich.com.stylemodel.ui.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.api.UserService;
import shinerich.com.stylemodel.base.SimpleActivity;
import shinerich.com.stylemodel.bean.BaseResponse;
import shinerich.com.stylemodel.bean.UserInfo;
import shinerich.com.stylemodel.common.GloableValues;
import shinerich.com.stylemodel.engin.LoginUserProvider;
import shinerich.com.stylemodel.network.RetrofitClient;
import shinerich.com.stylemodel.ui.main.activity.MainAcitivity;
import shinerich.com.stylemodel.utils.MLog;
import shinerich.com.stylemodel.utils.SharedUtils;

import static android.R.attr.type;


/**
 * 登录选择页面
 *
 * @author hunk
 */
public class LoginSelectActivity extends SimpleActivity {

    public final static int REQUEST_CODE_PERMISSION = 101;   //权限
    private int platformTag = 0;
    private static LoginSelectActivity instance;
    //友盟
    private UMShareAPI mShareAPI;
    private SHARE_MEDIA share_MEDIA;
    @BindView(R.id.btn_avoid)
    Button btn_avoid;
    @BindView(R.id.iv_wx)
    ImageView iv_wx;
    @BindView(R.id.iv_qq)
    ImageView iv_qq;
    @BindView(R.id.iv_wb)
    ImageView iv_wb;
    @BindView(R.id.iv_mobile)
    ImageView iv_mobile;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_select;

    }

    public static LoginSelectActivity getInstace() {
        return instance;
    }


    @Override
    protected void initEventAndData() {
        instance = this;
        //初始化数据
        initData();

    }

    /**
     * 初始化数据
     */
    public void initData() {
        mShareAPI = UMShareAPI.get(this);

        //免登陆
        if (getUser() != null) {
            startIntent(this, MainAcitivity.class);
            finish();
        }
    }


    @OnClick(value = {R.id.iv_wx, R.id.iv_qq,
            R.id.iv_mobile, R.id.iv_wb, R.id.btn_avoid})
    public void OnClick(View view) {
        switch (view.getId()) {
            //免登录
            case R.id.btn_avoid:
                startIntent(this, MainAcitivity.class);
                finish();
                break;
            //微信
            case R.id.iv_wx:
                platformTag = GloableValues.WX_TAG;
                checkPermisson();
                break;

            //qq
            case R.id.iv_qq:
                platformTag = GloableValues.QQ_TAG;
                checkPermisson();
                break;

            //wb
            case R.id.iv_wb:
                platformTag = GloableValues.WB_TAG;
                checkPermisson();
                break;
            //手机
            case R.id.iv_mobile:
                startIntent(this, MobileSelectActivity.class);
                break;

        }

    }

    /**
     * 第三方登录
     *
     * @param platform
     */
    public void loginByThird(final int platform) {

        switch (platform) {
            case GloableValues.WX_TAG:
                share_MEDIA = SHARE_MEDIA.WEIXIN;
                break;
            case GloableValues.QQ_TAG:
                share_MEDIA = SHARE_MEDIA.QQ;
                break;
            case GloableValues.WB_TAG:
                share_MEDIA = SHARE_MEDIA.SINA;
                break;
        }
        if (platform != GloableValues.WB_TAG &&
                !mShareAPI.isInstall(this, share_MEDIA)) {
            showToast("请安装客服端");
            return;
        }

        showProgressDialog();
        // 获取相关授权信息
        mShareAPI.getPlatformInfo(mContext,
                share_MEDIA, new

                        UMAuthListener() {

                            @Override
                            public void onError(SHARE_MEDIA arg0,
                                                int arg1, Throwable arg2) {
                                hideProgressDialog();
                                showToast("获取数据错误");
                            }

                            @Override
                            public void onComplete(SHARE_MEDIA arg0,
                                                   int status, Map<String, String> map) {
                                hideProgressDialog();
                                String access_token = "", open_id = "";
                                String oauth_name = "", oauth_iconurl = "";

                                if (map != null) {
                                    MLog.i("msg", map.toString());
                                    switch (platform) {
                                        case GloableValues.WX_TAG:
                                            access_token = map.get("access_token");
                                            open_id = map.get("openid");
                                            oauth_name = map.get("screen_name");
                                            oauth_iconurl = map.get("profile_image_url");
                                            break;

                                        case GloableValues.QQ_TAG:
                                            access_token = map.get("access_token");
                                            open_id = map.get("openid");
                                            oauth_name = map.get("screen_name");
                                            oauth_iconurl = map.get("profile_image_url");
                                            break;
                                        case GloableValues.WB_TAG:
                                            access_token = map.get("id");
                                            open_id = map.get("id");
                                            oauth_name = map.get("screen_name");
                                            oauth_iconurl = map
                                                    .get("profile_image_url");
                                            break;

                                    }

                                    oauthlogin(access_token, open_id,
                                            oauth_name,
                                            oauth_iconurl,
                                            platform);
                                }
                            }

                            @Override
                            public void onCancel(SHARE_MEDIA arg0,
                                                 int arg1) {
                                hideProgressDialog();
                                showToast("获取数据取消");
                            }
                        }

        );
    }


    /**
     * 第三方登录
     */
    public void oauthlogin(String access_token, String open_id, String oauth_name,
                           String oauth_iconurl, final int platform) {

        Map<String, String> params = new HashMap<>();
        params.put("access_token", access_token);
        params.put("open_id", open_id);
        params.put("platform", platform + "");
        params.put("nickname", oauth_name);
        params.put("usericon", oauth_iconurl);
        //1、ios系统 2、安卓系统
        params.put("rsource", "2");

        RetrofitClient client = RetrofitClient.getInstance();
        UserService userService = client.create(UserService.class);
        Call<BaseResponse<UserInfo>> call = userService.oauthLogin(params);
        showProgressDialog();
        call.enqueue(new Callback<BaseResponse<UserInfo>>() {
            @Override
            public void onResponse(Call<BaseResponse<UserInfo>> call, Response<BaseResponse<UserInfo>> response) {
                hideProgressDialog();
                final BaseResponse<UserInfo> body = response.body();
                UserInfo userInfo = body.getData();
                if (body.getCode() == 0) {
                    MLog.i("msg", userInfo.toString());
                    //保存信息
                    LoginUserProvider.setUser(mContext, userInfo);
                    LoginUserProvider.currentStatus = true;
                    SharedUtils.save(mContext, GloableValues.PLATFORM_TAG, type);

                    //去首页
                    startIntent(mContext, MainAcitivity.class);
                    finish();

                } else if (body.getCode() == 3) {
                    //未绑定手机号
                    int id = Integer.parseInt(userInfo.getId());
                    int platfrom = Integer.parseInt(userInfo.getPlatform());

                    Intent intent = new Intent(mContext, IsBindActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("platform", platfrom);
                    startActivity(intent);

                } else {
                    showToast(body.getMsg());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<UserInfo>> call, final Throwable t) {
                hideProgressDialog();
                t.printStackTrace();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (platformTag == GloableValues.WX_TAG) {
            hideProgressDialog();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
        mShareAPI.release();
    }

    /**
     * 检查权限
     */
    public void checkPermisson() {
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, REQUEST_CODE_PERMISSION);
        } else {
            loginByThird(platformTag);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //相册
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loginByThird(platformTag);
            } else {
                showToast("授权失败");
            }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}
