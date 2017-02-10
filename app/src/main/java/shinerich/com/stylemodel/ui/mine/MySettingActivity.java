package shinerich.com.stylemodel.ui.mine;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.api.HomeService;
import shinerich.com.stylemodel.base.SimpleActivity;
import shinerich.com.stylemodel.bean.BaseResponse;
import shinerich.com.stylemodel.bean.UpdateBean;
import shinerich.com.stylemodel.common.GloableValues;
import shinerich.com.stylemodel.engin.RemoteLogin;
import shinerich.com.stylemodel.network.RetrofitClient;
import shinerich.com.stylemodel.ui.mine.dialog.VersionUpdateDialog;
import shinerich.com.stylemodel.utils.ApplicationUtils;
import shinerich.com.stylemodel.utils.FileUtils;
import shinerich.com.stylemodel.utils.SharedUtils;

/**
 * 我的设置
 *
 * @author hunk
 */
public class MySettingActivity extends SimpleActivity {

    private VersionUpdateDialog updateDialog;
    private UMShareAPI mShareAPI;
    @BindView(R.id.rl_about_wo)
    RelativeLayout rl_about_wo;
    @BindView(R.id.rl_clean_memory)
    RelativeLayout rl_clean_memory;
    @BindView(R.id.rl_last_version)
    RelativeLayout rl_last_version;
    @BindView(R.id.rl_go_grade)
    RelativeLayout rl_go_grade;

    @BindView(R.id.btn_exit)
    Button btn_exit;
    @BindView(R.id.tv_size)
    TextView tv_size;
    @BindView(R.id.tv_version)
    TextView tv_version;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_setting;

    }

    @Override
    protected void initEventAndData() {
        //初始化标题
        initTitle();
        //初始化数据
        initData();

    }

    /**
     * 初始化标题
     */
    private void initTitle() {
        onMyBack();
        setMyTitle("设置");
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mShareAPI = UMShareAPI.get(this);
        //设置缓存大小
        tv_size.setText(getChacheSize());
        //版本更新
        String lastVersion = ApplicationUtils.getVersion(this);
        tv_version.setText("v" + lastVersion);


        if (getUser() != null) {
            btn_exit.setVisibility(View.VISIBLE);
        } else {
            btn_exit.setVisibility(View.GONE);
        }

    }


    @OnClick(value = {R.id.rl_clean_memory, R.id.rl_about_wo,
            R.id.rl_last_version, R.id.btn_exit, R.id.rl_go_grade})
    public void OnClick(View view) {
        switch (view.getId()) {
            //关于我们
            case R.id.rl_about_wo:
                startIntent(this, AboutActivity.class);
                break;
            //清除缓存
            case R.id.rl_clean_memory:
                cleanChache(view);
                break;
            //去评分
            case R.id.rl_go_grade:
                goGrade();
                break;
            //版本更新
            case R.id.rl_last_version:
                updateVersion();
                break;
            //退出登录
            case R.id.btn_exit:
                exitLogin();
                break;

        }
    }

    /**
     * 去评分
     */
    public void goGrade() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + getPackageName()));
            startActivity(intent);
        } catch (Exception e) {
            showToast("您的手机上没有安装Android应用市场");
            e.printStackTrace();
        }
    }


    /**
     * 退出登录
     */
    public void exitLogin() {
        if (getUser() == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("确定要退出登录嘛？")
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
                        deleteOauth();

                    }
                }).create().show();
    }

    /**
     * 版本更新
     */
    public void updateVersion() {
        if (updateDialog == null) {
            RetrofitClient client = RetrofitClient.getInstance();
            HomeService homeService = client.create(HomeService.class);
            String version = ApplicationUtils.getVersion(this);
            //	type 1、ios系统 2、安卓系统
            Call<BaseResponse<UpdateBean>> call = homeService.updateApp(version, 2);
            call.enqueue(new Callback<BaseResponse<UpdateBean>>() {
                @Override
                public void onResponse(Call<BaseResponse<UpdateBean>> call, Response<BaseResponse<UpdateBean>> response) {
                    final BaseResponse<UpdateBean> body = response.body();
                    if (body.getCode() == 0) {
                        UpdateBean updateBean = body.getData();

                        //更新
                        if (updateBean.getType() != 0) {
                            updateDialog = new VersionUpdateDialog(mContext);
                            updateDialog.setUpdateBean(updateBean);
                            updateDialog.show();
                        } else {
                            showToast("已是最新版本");
                        }

                    } else {
                        showToast(body.getMsg());
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<UpdateBean>> call, final Throwable t) {
                    t.printStackTrace();
                }
            });

        } else {
            updateDialog.show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 清除缓存
     */
    public void cleanChache(final View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("确定要清除缓存嘛？")
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
                        view.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                File chaCheDir = getCacheDir();
                                FileUtils.deleteDir(chaCheDir);
                                tv_size.setText(getChacheSize());
                                Glide.get(mContext).clearMemory();
                                showToast("清理成功");

                            }
                        }, 200);

                        //磁盘缓存
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                Glide.get(mContext).clearDiskCache();
                            }
                        }).start();

                    }
                }).create().show();


    }

    /**
     * 获取缓存大小
     */
    public String getChacheSize() {

        //应用缓存
        File chaCheDir = getCacheDir();
        double size = FileUtils.getDirSize(chaCheDir) * 1.0 / 1024 / 1024;
        //磁盘缓存
        File diskDir = new File(GloableValues.BASE_PATH + "/chache");
        size += FileUtils.getDirSize(diskDir) * 1.0 / 1024 / 1024;

        DecimalFormat df = new DecimalFormat("0.00");
        String strSize = df.format(size) + "M";

        return strSize;
    }

    /**
     * 删除授权
     */
    public void deleteOauth() {
        int platform = SharedUtils.getInt(mContext, GloableValues.PLATFORM_TAG, -1);
        if (platform < 0 || platform > 2) {
            //退出
            btn_exit.setVisibility(View.GONE);
            new RemoteLogin().remoteLoginToDo(mContext, true);
            return;
        }
        SHARE_MEDIA share_MEDIA = SHARE_MEDIA.SINA;
        switch (platform) {
            case 0:
                share_MEDIA = SHARE_MEDIA.SINA;
                break;
            case 1:
                share_MEDIA = SHARE_MEDIA.WEIXIN;
                break;

            case 2:
                share_MEDIA = SHARE_MEDIA.QQ;
                break;

        }
        mShareAPI.deleteOauth(this, share_MEDIA, new UMAuthListener() {
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //退出
                        btn_exit.setVisibility(View.GONE);
                        new RemoteLogin().remoteLoginToDo(mContext, true);

                    }
                });

            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("数据错误");
                    }
                });
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("取消");
                    }
                });
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (updateDialog != null) {
            updateDialog.cancel();
        }
    }
}
