package shinerich.com.stylemodel.ui.mine;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
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
import shinerich.com.stylemodel.engin.RemoteLogin;
import shinerich.com.stylemodel.network.RetrofitClient;
import shinerich.com.stylemodel.ui.mine.dialog.UserAddressSelectorDialog;
import shinerich.com.stylemodel.ui.mine.dialog.UserSelectPhotoDialog;
import shinerich.com.stylemodel.ui.mine.dialog.UserSexSelectorDialog;
import shinerich.com.stylemodel.ui.mine.dialog.UserTimeSelectorDialog;
import shinerich.com.stylemodel.utils.FileUtils;
import shinerich.com.stylemodel.utils.GlideUtils;
import shinerich.com.stylemodel.utils.HBitmapUtils;
import shinerich.com.stylemodel.utils.HDateUtils;
import shinerich.com.stylemodel.utils.IntentUtils;
import shinerich.com.stylemodel.utils.UriUtils;
import shinerich.com.stylemodel.widget.CircleImageView;

/**
 * 用户资料
 *
 * @author hunk
 */
public class UserInfoActivity extends SimpleActivity {

    public final String cameraPath = GloableValues.BASE_PATH + "/camera";
    private GlideUtils glideUtils = GlideUtils.getInstance();
    public final static int REQUEST_CODE_CAMERA = 101;
    public final static int REQUEST_CODE_ALBUM = 102;
    private boolean isAvatarChange = false;
    private UserInfo info;
    private File head;
    private UserSexSelectorDialog sexDialog;
    private UserAddressSelectorDialog addressDialog;
    private UserTimeSelectorDialog timeDialog;
    private UserSelectPhotoDialog photoDialog;
    @BindView(R.id.rl_user_head)
    RelativeLayout rl_user_head;
    @BindView(R.id.rl_user_sex)
    RelativeLayout rl_user_sex;
    @BindView(R.id.rl_user_address)
    RelativeLayout rl_user_address;
    @BindView(R.id.rl_user_birthday)
    RelativeLayout rl_user_birthday;
    @BindView(R.id.btn_save)
    Button btn_save;
    @BindView(R.id.iv_user_head)
    CircleImageView iv_user_head;
    @BindView(R.id.ed_user_name)
    EditText ed_user_name;
    @BindView(R.id.tv_user_sex)
    TextView tv_user_sex;
    @BindView(R.id.tv_user_birthday)
    TextView tv_user_birthday;
    @BindView(R.id.tv_user_address)
    TextView tv_user_address;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_userinfo;

    }

    @Override
    protected void initEventAndData() {

        //初始化参数
        initParams();
        //初始化标题
        initTitle();
        //初始化数据
        initData();
    }


    /**
     * 初始化参数
     */
    public void initParams() {
        info = (UserInfo) getIntent().getSerializableExtra("info");
        sexDialog = new UserSexSelectorDialog(this);
        addressDialog = new UserAddressSelectorDialog(this);
        timeDialog = new UserTimeSelectorDialog(this);
        photoDialog = new UserSelectPhotoDialog(this);

    }

    /**
     * 初始化数据
     */
    public void initData() {
        //设置用户信息
        if (info != null) {
            glideUtils.load(mContext, info.getUsericon(), new GlideUtils.OnDownLoadBitmapListener() {
                @Override
                public void getBitmap(Bitmap bitmap) {
                    iv_user_head.setImageBitmap(bitmap);
                }
            });
            ed_user_name.setText(info.getNickname());
            if ("1".equals(info.getSex())) {
                tv_user_sex.setText("男");
            } else if ("2".equals(info.getSex())) {
                tv_user_sex.setText("女");
            } else {
                tv_user_sex.setText("男");
            }
            if (!TextUtils.isEmpty(info.getBirthyear())) {
                String birthday = info.getBirthyear() + "-" +
                        info.getBirthmonth() + "-" + info.getBirthday();
                tv_user_birthday.setText(birthday);
            }

            String provincename = addressDialog.getCityName(info.getProvince());
            tv_user_address.setText(provincename);
        }
    }


    /*
     *  初始化标题
     */
    public void initTitle() {
        onMyBack();
        setMyTitle("用户信息");
    }


    @OnClick(value = {R.id.rl_user_head,
            R.id.rl_user_address, R.id.rl_user_sex,
            R.id.rl_user_birthday, R.id.btn_save})
    public void OnClick(View view) {

        switch (view.getId()) {
            //头像
            case R.id.rl_user_head:
                selectPhoto();
                break;
            //性别
            case R.id.rl_user_sex:
                selectSex();
                break;
            //生日
            case R.id.rl_user_birthday:
                selectDataTime();
                break;
            //地址
            case R.id.rl_user_address:
                selectAddress();
                break;
            //保存
            case R.id.btn_save:
                saveUserInfo();
                break;

        }

    }


    /**
     * 选择日期
     */
    public void selectDataTime() {
        timeDialog.show();
        timeDialog.setOnItemSelectedListener(new UserTimeSelectorDialog.OnItemSelectedListener() {
            @Override
            public void onSelected(int year, int month, int day) {
                if (info != null) {
                    info.setBirthyear(year + "");
                    info.setBirthmonth(month + "");
                    info.setBirthday(day + "");
                }
                String strDate = year + "-" + month + "-" + day;
                tv_user_birthday.setText(strDate);
            }
        });

    }

    /**
     * 选择性别
     */
    public void selectSex() {
        sexDialog.setValue(tv_user_sex.getText().toString().trim());
        sexDialog.show();
        sexDialog.setOnItemSelectedListener(new UserSexSelectorDialog.OnItemSelectedListener() {
            @Override
            public void onSelected(String value) {

                if ("男".equals(value)) {
                    info.setSex("1");
                } else if ("女".equals(value)) {
                    info.setSex("2");
                }
                tv_user_sex.setText(value);

            }
        });
    }


    /**
     * 选择地址
     */
    public void selectAddress() {
        addressDialog.show();
        addressDialog.setOnItemSelectedListener(new UserAddressSelectorDialog.OnItemSelectedListener() {
            @Override
            public void onSelected(String code, String name) {
                if (info != null) {
                    info.setProvince(code);
                }
                tv_user_address.setText(name);
            }
        });
    }


    /**
     * 选择相片
     */
    public void selectPhoto() {
        photoDialog.setOnItemSelectedListener(new UserSelectPhotoDialog.OnItemSelectedListener() {
            @Override
            public void onCameraClick(View view) {
                if (ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(mContext,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_CAMERA);

                } else {
                    takePhoto();
                }
            }

            @Override
            public void onAlbumClick(View view) {
                //申请权限
                if (ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(mContext,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_ALBUM);
                } else {

                    choosePhoto();
                }
            }
        });
        photoDialog.show();

    }

    /**
     * 选择照片
     */
    public void choosePhoto() {
        IntentUtils.openAlbum(UserInfoActivity.this, REQUEST_CODE_ALBUM);
    }

    /**
     * 拍照
     */
    public void takePhoto() {
        //创建目录
        FileUtils.createDir(cameraPath);
        head = new File(cameraPath + "/" + HDateUtils.getId()
                + ".jpg");
        IntentUtils.openCamera(UserInfoActivity.this, head, REQUEST_CODE_CAMERA);


    }


    /**
     * 保存用户信息
     */
    public void saveUserInfo() {

        if (getUser() == null) {
            new RemoteLogin().remoteLoginToDo(this, false);
            return;
        }
        String nickname = ed_user_name.getText().toString().trim();
        Map<String, RequestBody> params = new HashMap<>();
        params.put("uid", RequestBody.create(MediaType.parse("multipart/form-data"), getUser().getId()));
        params.put("key", RequestBody.create(MediaType.parse("multipart/form-data"), getUser().getKey()));
        params.put("nickname", RequestBody.create(MediaType.parse("multipart/form-data"), nickname));
        params.put("sex", RequestBody.create(MediaType.parse("multipart/form-data"), info.getSex()));
        params.put("birthyear", RequestBody.create(MediaType.parse("multipart/form-data"), info.getBirthyear()));
        params.put("birthmonth", RequestBody.create(MediaType.parse("multipart/form-data"), info.getBirthmonth()));
        params.put("birthday", RequestBody.create(MediaType.parse("multipart/form-data"), info.getBirthday()));
        if (!TextUtils.isEmpty(info.getProvince())) {
            params.put("province", RequestBody.create(MediaType.parse("multipart/form-data"), info.getProvince()));
        } else {
            params.put("province", RequestBody.create(MediaType.parse("multipart/form-data"), ""));
        }


        RetrofitClient client = RetrofitClient.getInstance();
        UserService userService = client.create(UserService.class);
        Call<BaseResponse<UserInfo>> call;
        if (isAvatarChange) {
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("image/jpg"), head);

            call = userService.modifyUserInfo(params, requestFile);
        } else {
            call = userService.modifyUserInfo(params, null);
        }
        showProgressDialog();
        btn_save.setEnabled(false);
        call.enqueue(new Callback<BaseResponse<UserInfo>>() {
            @Override
            public void onResponse(Call<BaseResponse<UserInfo>> call, Response<BaseResponse<UserInfo>> response) {
                hideProgressDialog();
                btn_save.setEnabled(true);
                final BaseResponse<UserInfo> body = response.body();
                if (body != null) {
                    if (body.getCode() == 0) {
                        isAvatarChange = false;

                        //更新一下本地信息
                        UserInfo info = body.getData();
                        getUser().setNickname(info.getNickname());
                        getUser().setUsericon(info.getUsericon());

                        LoginUserProvider.saveUserInfo(mContext);

                        // showToast(body.getMsg());
                        finish();

                    } else {
                        showToast(body.getMsg());
                    }
                } else {
                    showToast(response.message());
                }

            }

            @Override
            public void onFailure(Call<BaseResponse<UserInfo>> call, final Throwable t) {
                btn_save.setEnabled(true);
                hideProgressDialog();
                showToast(t.getMessage());
                t.printStackTrace();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                //相机
                case REQUEST_CODE_CAMERA:
                    if (head != null) {
                        updateUserHead(head);
                    }
                    break;

                //相册
                case REQUEST_CODE_ALBUM:
                    if (data != null) {
                        String path = UriUtils.getPath(this, data.getData());
                        if (!TextUtils.isEmpty(path)) {
                            head = new File(path);
                            updateUserHead(head);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //相册
        if (requestCode == REQUEST_CODE_ALBUM) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                choosePhoto();
            } else {
                showToast("授权失败");
            }

        }
        //相机
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                showToast("授权失败");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


    /**
     * 更新用户头像
     *
     * @param file
     */
    public void updateUserHead(final File file) {
        //清除请求
        glideUtils.cleanViewRequest(iv_user_head);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = HBitmapUtils.getBitmapByScale(file.getAbsolutePath());
                Message message = Message.obtain();
                message.obj = bitmap;
                upDateHandler.sendMessage(message);
            }
        }).start();


    }

    private Handler upDateHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bitmap bitmap = (Bitmap) msg.obj;
            iv_user_head.setImageBitmap(bitmap);
            isAvatarChange = true;
        }
    };


}
