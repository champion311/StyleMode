package shinerich.com.stylemodel.ui.mine;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
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
 * 意见反馈
 *
 * @author hunk
 */
public class FeedbackActivity extends SimpleActivity implements View.OnClickListener {


    @BindView(R.id.et_feedback)
    EditText et_feedback;
    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.iv_close)
    ImageView iv_close;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_feedback;
    }

    @Override
    protected void initEventAndData() {
        //初始化标题
        initTitle();
        //初始化数据
        initData();
    }

    /**
     * 初始化数据
     */
    public void initData() {

        iv_close.setOnClickListener(this);
        et_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (TextUtils.isEmpty(s)) {
                    iv_close.setVisibility(View.GONE);
                } else {
                    iv_close.setVisibility(View.VISIBLE);
                }

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //发送
            case R.id.tv_base_right:
                submit();
                break;
            case R.id.iv_close:
                et_email.getText().clear();
                break;

        }
    }

    /**
     * 初始化标题
     */
    private void initTitle() {
        onMyBack();
        setMyTitle("意见反馈");
        setRightText("发送");
        setRightTextListener(this);
    }

    /**
     * 提交数据
     */
    public void submit() {

        String content = et_feedback.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        if(TextUtils.isEmpty(content)){
            showToast("请输入您的建议");
            return;
        }

        if(StringUtils.verityEmail(email)){
            showToast("邮箱格式有误");
            return;
        }

        Map<String, String> params = new HashMap<String, String>();
        if (getUser() != null) {
            params.put("uid", getUser().getId());
            params.put("key", getUser().getKey());
        }
        params.put("content", content);
        params.put("contact", email);

        RetrofitClient client = RetrofitClient.getInstance();
        UserService userService = client.create(UserService.class);
        Call<BaseResponse<String>> call = userService.feedback(params);
        showProgressDialog();
        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                hideProgressDialog();
                final BaseResponse<String> body = response.body();
                if (body.getCode() == 0) {
                    showToast(body.getMsg());

                    finish();

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


}
