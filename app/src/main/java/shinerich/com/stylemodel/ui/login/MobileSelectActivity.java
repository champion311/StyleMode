package shinerich.com.stylemodel.ui.login;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
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
import shinerich.com.stylemodel.network.RetrofitClient;
import shinerich.com.stylemodel.utils.StringUtils;

/**
 * 手机注册|登录
 *
 * @author hunk
 */
public class MobileSelectActivity extends SimpleActivity {

    private static MobileSelectActivity instance;
    @BindView(R.id.et_mobile)
    EditText et_mobile;
    @BindView(R.id.btn_select)
    Button btn_select;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_mobile_select;
    }

    @Override
    protected void initEventAndData() {
        instance = this;
        //初始化标题
        initTitle();
        //设置事件监听器
        setListener();


    }

    public static MobileSelectActivity getInstace() {
        return instance;
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
                if (StringUtils.verityMobile(s.toString())) {
                    btn_select.setEnabled(true);

                    btn_select.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    btn_select.setBackgroundResource(R.drawable.login_btn_selector);
                } else {
                    btn_select.setBackgroundResource(R.drawable.login_btn_def_shape);
                    btn_select.setTextColor(ContextCompat.getColor(mContext, R.color.login_text_hint));
                    btn_select.setEnabled(false);
                }
            }
        });


    }


    /**
     * 初始化标题
     */
    public void initTitle() {
        onMyBack();
        setMyTitle("手机注册|登录");
    }


    @OnClick(value = {R.id.btn_select})
    public void OnClick(View view) {
        switch (view.getId()) {
            //选择登录
            case R.id.btn_select:
                selectLogin();
                break;
        }

    }

    /**
     * 选择登录
     */
    public void selectLogin() {

        final String mobile = et_mobile.getText().toString().trim();
        RetrofitClient client = RetrofitClient.getInstance();
        UserService userService = client.create(UserService.class);
        Call<BaseResponse<String>> call = userService.isExistsMobile(mobile);
        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {


                final BaseResponse<String> body = response.body();

                if (body.getCode() == 0) { //已注册
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("mobile", mobile);
                    startActivity(intent);

                } else if (body.getCode() == 1) { //未注册
                    Intent intent = new Intent(mContext, RegisterActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("mobile", mobile);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                t.printStackTrace();
                showToast(getResources().getString(R.string.network_no));
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }
}
