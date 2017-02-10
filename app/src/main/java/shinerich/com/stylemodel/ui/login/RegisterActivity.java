package shinerich.com.stylemodel.ui.login;

import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

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
import shinerich.com.stylemodel.common.DownTimerTask;
import shinerich.com.stylemodel.common.GloableValues;
import shinerich.com.stylemodel.engin.LoginUserProvider;
import shinerich.com.stylemodel.network.RetrofitClient;
import shinerich.com.stylemodel.ui.main.activity.MainAcitivity;
import shinerich.com.stylemodel.utils.MLog;
import shinerich.com.stylemodel.utils.SharedUtils;
import shinerich.com.stylemodel.utils.StringUtils;

/**
 * 注册页面
 *
 * @author hunk
 */
public class RegisterActivity extends SimpleActivity {

    private DownTimerTask task;
    @BindView(R.id.et_mobile)
    EditText et_mobile;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.et_code)
    EditText et_code;
    @BindView(R.id.btn_get_code)
    Button btn_get_code;
    @BindView(R.id.btn_register)
    Button btn_register;
    @BindView(R.id.tv_user_agreement)
    TextView tv_user_agreement;
    @BindView(R.id.ll_protocol)
    LinearLayout ll_protocol;
    @BindView(R.id.rb_agreement)
    RadioButton rb_agreement;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }


    @Override
    protected void initEventAndData() {
        //初始化标题
        initTitle();
        //初始化数据
        initData();
        //设置事件器
        setListener();

    }

    /***
     * 初始化数据
     */
    public void initData() {

        //设置手机号
        String mobile = getIntent().getStringExtra("mobile");
        et_mobile.getText().clear();
        et_mobile.append(mobile);
    }

    /**
     * 设置事件监听器
     */
    public void setListener() {
        et_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String mobile = s.toString();
                String password = et_password.getText().toString().trim();
                String code = et_code.getText().toString().trim();

                if (isSubmit(mobile, password, code)) {
                    btn_register.setEnabled(true);
                    btn_register.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    btn_register.setBackgroundResource(R.drawable.login_btn_selector);
                } else {
                    btn_register.setBackgroundResource(R.drawable.login_btn_def_shape);
                    btn_register.setTextColor(ContextCompat.getColor(mContext, R.color.login_text_hint));
                    btn_register.setEnabled(false);
                }
            }
        });

        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String mobile = et_mobile.getText().toString().trim();
                String password = s.toString();
                String code = et_code.getText().toString().trim();

                if (isSubmit(mobile, password, code)) {
                    btn_register.setEnabled(true);
                    btn_register.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    btn_register.setBackgroundResource(R.drawable.login_btn_selector);
                } else {
                    btn_register.setBackgroundResource(R.drawable.login_btn_def_shape);
                    btn_register.setTextColor(ContextCompat.getColor(mContext, R.color.login_text_hint));
                    btn_register.setEnabled(false);
                }
            }
        });


        et_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String mobile = et_mobile.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                String code = s.toString();

                if (isSubmit(mobile, password, code)) {
                    btn_register.setEnabled(true);
                    btn_register.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    btn_register.setBackgroundResource(R.drawable.login_btn_selector);
                } else {
                    btn_register.setBackgroundResource(R.drawable.login_btn_def_shape);
                    btn_register.setTextColor(ContextCompat.getColor(mContext, R.color.login_text_hint));
                    btn_register.setEnabled(false);
                }
            }
        });
    }


    @OnClick(value = {R.id.tv_user_agreement, R.id.btn_get_code, R.id.btn_register, R.id.ll_protocol})
    public void OnClick(View view) {
        switch (view.getId()) {
            //用户协议
            case R.id.tv_user_agreement:
                if (rb_agreement.isChecked()) {
                    startIntent(this, UserAgreementActivity.class);
                }
                break;

            //注册
            case R.id.btn_register:
                verifyCode();
                break;

            //获取验证码
            case R.id.btn_get_code:
                getCode();
                break;
            //协议
            case R.id.ll_protocol:
                rb_agreement.setChecked(!rb_agreement.isChecked());
                break;

        }

    }


    /**
     * 初始化标题
     */
    public void initTitle() {
        onMyBack();
        setMyTitle("注册");
    }


    /**
     * 获取验证码
     */
    public void getCode() {

        String mobile = et_mobile.getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            showToast("手机号不能为空");
            return;
        }

        if (!StringUtils.verityMobile(mobile)) {
            showToast("手机号格式有误");
            return;
        }

        RetrofitClient client = RetrofitClient.getInstance();
        UserService userService = client.create(UserService.class);
        // 1注册,2.密码找回(忘记密码),3.修改手机,4修改邮箱,5填写手机
        int type = 1;
        btn_get_code.setEnabled(false);
        Call<BaseResponse<String>> call = userService.sendCode(mobile, type);
        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                hideProgressDialog();
                BaseResponse<String> body = response.body();
                if (body.getCode() == 0) {
                    if (MLog.isDUG()) {
                        showToast(body.getData());

                    } else {
                        showToast(body.getMsg());
                    }

                    task = new DownTimerTask(60000, new DownTimerTask.OnCallbackListener() {
                        @Override
                        public void onTick(int interval) {
                            btn_get_code.setText("重新发送" + interval + "s后");
                        }

                        @Override
                        public void onFinish() {
                            btn_get_code.setEnabled(true);
                            btn_get_code.setText("获取验证码");
                        }
                    });
                    task.start();

                } else {
                    btn_get_code.setEnabled(true);
                    showToast(body.getMsg());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                btn_get_code.setEnabled(true);
                MLog.showToast(mContext, t.getMessage());
            }
        });
    }

    /**
     * 注册
     */
    public void register() {

        String mobile = et_mobile.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        RetrofitClient client = RetrofitClient.getInstance();
        UserService userService = client.create(UserService.class);
        // 1 ios系统 2 安卓系统
        int rsource = 2;
        final int type = 3;   //手机
        int id = 0;
        Call<BaseResponse<UserInfo>> call = userService.registerUser(mobile, password, rsource, type, id);
        showProgressDialog();
        call.enqueue(new Callback<BaseResponse<UserInfo>>() {
            @Override
            public void onResponse(Call<BaseResponse<UserInfo>> call, Response<BaseResponse<UserInfo>> response) {
                hideProgressDialog();
                BaseResponse<UserInfo> body = response.body();
                UserInfo userInfo = body.getData();
                if (body.getCode() == 0) {
                    showToast(body.getMsg());
                    //保存用户信息
                    LoginUserProvider.setUser(mContext, userInfo);
                    LoginUserProvider.currentStatus = true;
                    SharedUtils.save(mContext, GloableValues.PLATFORM_TAG, type);

                    //去主页面
                    startIntent(mContext, MainAcitivity.class);
                    if (MobileSelectActivity.getInstace() != null) {
                        MobileSelectActivity.getInstace().finish();
                    }
                    finish();


                } else {
                    showToast(body.getMsg());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<UserInfo>> call, final Throwable t) {
                hideProgressDialog();
                MLog.showToast(mContext, t.getMessage());
            }
        });
    }

    /**
     * 是否可以提交
     */
    public boolean isSubmit(String mobile, String password, String code) {
        boolean isOK = false;
        if (StringUtils.verityMobile(mobile) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(code) && code.length() > 3) {
            isOK = true;
        }

        return isOK;
    }


    /***
     * 验证验证码
     */
    public void verifyCode() {

        String mobile = et_mobile.getText().toString().trim();
        String code = et_code.getText().toString().trim();

        if (TextUtils.isEmpty(mobile)) {
            showToast("手机号不能为空");
            return;
        }

        if (!StringUtils.verityMobile(mobile)) {
            showToast("手机号格式有误");
            return;
        }

        if (TextUtils.isEmpty(code)) {
            showToast("验证码不能为空");
            return;
        }

        RetrofitClient client = RetrofitClient.getInstance();
        UserService userService = client.create(UserService.class);
        //1注册,2.密码找回(忘记密码),3.修改手机,4修改邮箱,5填写手机
        int type = 1;
        Call<BaseResponse<String>> call = userService.verifyCode(mobile, code, type);
        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {


                BaseResponse<String> body = response.body();
                if (body.getCode() == 0) {
                    //注册
                    register();
                } else {
                    showToast(body.getMsg());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                MLog.showToast(mContext, t.getMessage());
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task != null) {
            task.cancel();
        }
    }
}
