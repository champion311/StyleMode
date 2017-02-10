package shinerich.com.stylemodel.ui.login;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.api.UserService;
import shinerich.com.stylemodel.base.SimpleActivity;
import shinerich.com.stylemodel.bean.BaseResponse;
import shinerich.com.stylemodel.bean.RefreshSubFragmentEvent;
import shinerich.com.stylemodel.bean.UserInfo;
import shinerich.com.stylemodel.engin.LoginUserProvider;
import shinerich.com.stylemodel.network.RetrofitClient;
import shinerich.com.stylemodel.ui.main.activity.MainAcitivity;
import shinerich.com.stylemodel.utils.RxBus;
import shinerich.com.stylemodel.utils.StringUtils;


/**
 * 登录页面
 *
 * @author hunk
 */
public class LoginActivity extends SimpleActivity {

    @BindView(R.id.et_mobile)
    EditText et_mobile;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.btn_forget)
    Button btn_forget;
    @BindView(R.id.btn_login)
    Button btn_login;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;

    }

    @Override
    protected void initEventAndData() {

        //初始化标题
        initTitle();
        //初始化数据
        initData();
        //设置事件监听器
        setListener();
    }


    /**
     * 初始化数据
     */
    private void initData() {
        //设置手机号
        String mobile = getIntent().getStringExtra("mobile");
        et_mobile.getText().clear();
        et_mobile.append(mobile);


    }

    /**
     * 设置事件监听器
     */
    public void setListener() {

        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && s.toString().length() >= 8) {
                    btn_login.setEnabled(true);
                    btn_login.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    btn_login.setBackgroundResource(R.drawable.login_btn_selector);
                } else {
                    btn_login.setBackgroundResource(R.drawable.login_btn_def_shape);
                    btn_login.setTextColor(ContextCompat.getColor(mContext, R.color.login_text_hint));
                    btn_login.setEnabled(false);
                }
            }
        });


    }


    /**
     * 初始化标题
     */
    private void initTitle() {
        onMyBack();
        setMyTitle("登录");
    }


    @OnClick(value = {R.id.btn_login, R.id.btn_forget})
    public void OnClick(View view) {

        switch (view.getId()) {
            //登录
            case R.id.btn_login:
                login();
                break;

            //忘记密码
            case R.id.btn_forget:
                Intent intentForget = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                String mobile = et_mobile.getText().toString();
                if (StringUtils.verityMobile(mobile)) {
                    intentForget.putExtra("mobile", mobile);
                }
                startActivity(intentForget);
                break;

        }

    }


    /***
     * 登录
     */
    public void login() {

        String mobile = et_mobile.getText().toString().trim();
        String password = et_password.getText().toString().trim();


        if (!StringUtils.verityMobile(mobile)) {
            showToast("手机号格式有误");
            return;
        }

        if (!StringUtils.verityPassword(password)) {
            showToast("密码格式有误");
            return;
        }

       final int type = 3;
        int id = 0;
        RetrofitClient client = RetrofitClient.getInstance();
        UserService userService = client.create(UserService.class);
        Call<BaseResponse<UserInfo>> call = userService.login(mobile, password, type, id);
        showProgressDialog();
        call.enqueue(new Callback<BaseResponse<UserInfo>>() {
            @Override
            public void onResponse(Call<BaseResponse<UserInfo>> call, Response<BaseResponse<UserInfo>> response) {
                hideProgressDialog();
                final BaseResponse<UserInfo> body = response.body();
                UserInfo userInfo = body.getData();
                if (body.getCode() == 0) {
                    //保存用户信息
                    LoginUserProvider.setUser(mContext, userInfo);
                    LoginUserProvider.currentStatus = true;
                    //去首页
                    startIntent(mContext, MainAcitivity.class);
                    RxBus.getInstance().post(new RefreshSubFragmentEvent());
                    //登陆选择页
                    if (LoginSelectActivity.getInstace() != null) {
                        LoginSelectActivity.getInstace().finish();
                    }
                    //手机号选择页
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
                t.printStackTrace();
            }
        });
    }

}
