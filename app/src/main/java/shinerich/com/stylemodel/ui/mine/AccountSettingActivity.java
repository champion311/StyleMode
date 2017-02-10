package shinerich.com.stylemodel.ui.mine;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import shinerich.com.stylemodel.bean.AccountSetting;
import shinerich.com.stylemodel.bean.BaseResponse;
import shinerich.com.stylemodel.bean.BindState;
import shinerich.com.stylemodel.common.GloableValues;
import shinerich.com.stylemodel.engin.RemoteLogin;
import shinerich.com.stylemodel.network.RetrofitClient;
import shinerich.com.stylemodel.utils.MLog;

import static shinerich.com.stylemodel.common.GloableValues.QQ_TAG;
import static shinerich.com.stylemodel.common.GloableValues.WB_TAG;
import static shinerich.com.stylemodel.common.GloableValues.WX_TAG;

/**
 * 账号设置
 *
 * @author hunk
 */
public class AccountSettingActivity extends SimpleActivity {
    public final static int REQUEST_CODE_PERMISSION = 101;   //权限
    private int platformTag = 0;

    private AccountSetting data;
    //友盟
    private UMShareAPI mShareAPI;
    private SHARE_MEDIA share_MEDIA;
    @BindView(R.id.rl_reset_password)
    RelativeLayout rl_reset_password;
    @BindView(R.id.rl_wb)
    RelativeLayout rl_wb;
    @BindView(R.id.rl_qq)
    RelativeLayout rl_qq;
    @BindView(R.id.rl_wx)
    RelativeLayout rl_wx;
    @BindView(R.id.tv_mobile)
    TextView tv_mobile;
    @BindView(R.id.tv_qq)
    TextView tv_qq;
    @BindView(R.id.tv_wx)
    TextView tv_wx;
    @BindView(R.id.tv_wb)
    TextView tv_wb;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_account_setting;

    }

    @Override
    protected void initEventAndData() {
        mShareAPI = UMShareAPI.get(this);
        //初始化标题
        initTitle();
        //加载数据
        loadData();
    }


    /**
     * 初始化标题
     */
    public void initTitle() {
        onMyBack();
        setMyTitle("账号设置");
    }


    @OnClick(value = {R.id.rl_reset_password, R.id.rl_wb,
            R.id.rl_qq, R.id.rl_wx})
    public void OnClick(View view) {
        switch (view.getId()) {
            //绑定手机号
            case R.id.rl_reset_password:
                if (data != null && !TextUtils.isEmpty(data.getMobile())) {
                    Intent intent = new Intent(this, ResetPasswordActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("mobile", data.getMobile());
                    startActivity(intent);
                }
                break;
            //新浪微博
            case R.id.rl_wb:
                platformTag = WB_TAG;
                if (data != null) {
                    checkPermisson();
                }

                break;
            //QQ
            case R.id.rl_qq:
                platformTag = QQ_TAG;
                if (data != null) {
                    checkPermisson();
                }
                break;
            //微信
            case R.id.rl_wx:
                platformTag = WX_TAG;
                if (data != null) {
                    checkPermisson();
                }
                break;

        }
    }


    /**
     * 更新信息
     */
    public void updateData() {

        tv_mobile.setText(data.getMobile());
        if ("1".equals(data.getQq().getIs_bind())) {
            tv_qq.setText(data.getQq().getName());
        } else {
            tv_qq.setText("");
        }
        if ("1".equals(data.getSina().getIs_bind())) {
            tv_wb.setText(data.getSina().getName());
        } else {
            tv_wb.setText("");
        }

        if ("1".equals(data.getWx().getIs_bind())) {
            tv_wx.setText(data.getWx().getName());
        } else {
            tv_wx.setText("");
        }

    }


    /**
     * 加载数据
     */
    public void loadData() {

        RetrofitClient client = RetrofitClient.getInstance();
        UserService userService = client.create(UserService.class);
        Call<BaseResponse<AccountSetting>> call = userService.userSetting(getUser().getId(), getUser().getKey());
        showProgressDialog();
        call.enqueue(new Callback<BaseResponse<AccountSetting>>() {
            @Override
            public void onResponse(Call<BaseResponse<AccountSetting>> call, Response<BaseResponse<AccountSetting>> response) {
                hideProgressDialog();
                final BaseResponse<AccountSetting> body = response.body();

                if (body.getCode() == 0) {
                    data = body.getData();
                    //更新信息
                    updateData();

                } else if (body.getCode() == 200) {  //被踢

                    new RemoteLogin().remoteLoginToDo(mContext, false);

                } else {
                    showToast(body.getMsg());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<AccountSetting>> call, final Throwable t) {
                hideProgressDialog();
                t.printStackTrace();
            }
        });
    }

    /**
     * 第三方授权
     *
     * @param platform
     */
    public void oauthByThird(final int platform) {

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
                                        case WX_TAG:
                                            access_token = map.get("access_token");
                                            open_id = map.get("openid");
                                            oauth_name = map.get("screen_name");
                                            oauth_iconurl = map.get("profile_image_url");
                                            break;

                                        case QQ_TAG:
                                            access_token = map.get("access_token");
                                            open_id = map.get("openid");
                                            oauth_name = map.get("screen_name");
                                            oauth_iconurl = map.get("profile_image_url");
                                            break;
                                        case WB_TAG:
                                            access_token = map.get("id");
                                            open_id = map.get("id");
                                            oauth_name = map.get("screen_name");
                                            oauth_iconurl = map
                                                    .get("profile_image_url");
                                            break;

                                    }

                                    bindByThird(access_token, open_id,
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
     * 第三方绑定
     */
    public void bindByThird(String access_token, String open_id, String oauth_name,
                            String oauth_iconurl, final int platform) {

        Map<String, String> params = new HashMap<>();
        params.put("uid", getUser().getId());
        params.put("key", getUser().getKey());
        params.put("access_token", access_token);
        params.put("open_id", open_id);
        params.put("platform", platform + "");
        params.put("nickname", oauth_name);
        params.put("usericon", oauth_iconurl);

        RetrofitClient client = RetrofitClient.getInstance();
        UserService userService = client.create(UserService.class);
        Call<BaseResponse<BindState>> call = userService.bind(params);
        showProgressDialog();
        call.enqueue(new Callback<BaseResponse<BindState>>() {
            @Override
            public void onResponse(Call<BaseResponse<BindState>> call, Response<BaseResponse<BindState>> response) {
                hideProgressDialog();
                final BaseResponse<BindState> body = response.body();
                BindState state = body.getData();
                if (body.getCode() == 0) {

                    if (platform == WX_TAG) {
                        data.getWx().setIs_bind("1");
                        data.getWx().setName(state.getName());
                    } else if (platform == WB_TAG) {
                        data.getSina().setIs_bind("1");
                        data.getSina().setName(state.getName());
                    } else if (platform == QQ_TAG) {
                        data.getQq().setIs_bind("1");
                        data.getQq().setName(state.getName());
                    }
                    //更新数据
                    updateData();


                } else if (body.getCode() == 200) {  //被踢

                    new RemoteLogin().remoteLoginToDo(mContext, false);

                } else {
                    showToast(body.getMsg());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<BindState>> call, final Throwable t) {
                hideProgressDialog();
                t.printStackTrace();
            }
        });
    }


    /**
     * 删除第三方
     */
    public void deleteOauthByThird(final int platform) {
        switch (platform) {
            case WX_TAG:
                share_MEDIA = SHARE_MEDIA.WEIXIN;
                break;
            case QQ_TAG:
                share_MEDIA = SHARE_MEDIA.QQ;
                break;
            case WB_TAG:
                share_MEDIA = SHARE_MEDIA.SINA;
                break;
        }
        mShareAPI.deleteOauth(this, share_MEDIA, new UMAuthListener() {
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        unbindByThird(platform);
                    }
                });


            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                showToast("数据错误");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                showToast("数据取消");
            }
        });

    }

    /**
     * 第三方绑定
     */
    public void unbindByThird(final int platform) {

        Map<String, String> params = new HashMap<>();
        params.put("uid", getUser().getId());
        params.put("key", getUser().getKey());
        params.put("platform", platform + "");

        RetrofitClient client = RetrofitClient.getInstance();
        UserService userService = client.create(UserService.class);
        Call<BaseResponse<String>> call = userService.unbind(getUser().getId(), getUser().getKey(),
                platform + "");
        showProgressDialog();
        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                hideProgressDialog();
                final BaseResponse<String> body = response.body();
                if (body.getCode() == 0) {


                    if (platform == WX_TAG) {
                        data.getWx().setIs_bind("0");
                        data.getWx().setName("");
                    } else if (platform == WB_TAG) {
                        data.getSina().setIs_bind("0");
                        data.getSina().setName("");
                    } else if (platform == QQ_TAG) {
                        data.getQq().setIs_bind("0");
                        data.getQq().setName("");
                    }

                    //更新数据
                    updateData();


                } else if (body.getCode() == 200) {  //被踢
                    new RemoteLogin().remoteLoginToDo(mContext, false);

                } else {
                    showToast(body.getMsg());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, final Throwable t) {
                hideProgressDialog();
                t.printStackTrace();
            }
        });
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
            BindState state = null;
            if (platformTag == WX_TAG) {
                state = data.getWx();
            } else if (platformTag == WB_TAG) {
                state = data.getSina();
            } else if (platformTag == QQ_TAG) {
                state = data.getQq();
            }

            if (state != null) {
                if ("0".equals(state.getIs_bind())) {
                    oauthByThird(platformTag);
                } else {
                    deleteOauthDialog();
                }

            }
        }
    }


    /**
     * 删除授权Dialog
     */
    public void deleteOauthDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("确定要解除绑定嘛？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();


                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        deleteOauthByThird(platformTag);


                    }
                }).create().show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (platformTag == WX_TAG) {
            hideProgressDialog();
        }
    }
}
