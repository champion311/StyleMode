package shinerich.com.stylemodel.ui.mine.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.base.BaseDialog;
import shinerich.com.stylemodel.utils.ScreenUtils;

/**
 * 用户性别选择-Dialog
 *
 * @author hunk
 */
public class UserSexSelectorDialog extends BaseDialog implements View.OnClickListener {

    private TextView btn_sure, btn_cancel;
    private TextView tv_man, tv_woman;
    private String value = "";
    private OnItemSelectedListener listener;

    public UserSexSelectorDialog(Context context) {
        super(context);
        initParams();
    }


    /**
     * 设置值
     */
    public void setValue(String value) {
        this.value = value;
    }


    private void initParams() {
        Window window = getWindow();
        setContentView(R.layout.dialog_user_sex);
        window.setGravity(Gravity.BOTTOM);
        // 添加动画
        window.setWindowAnimations(R.style.DialogAnimMine);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = ScreenUtils.getScreenWidth(context);
        getWindow().setAttributes(lp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化数据
        initData();
    }

    public void initData() {
        btn_sure = (TextView) findViewById(R.id.btn_confirm);
        btn_cancel = (TextView) findViewById(R.id.btn_cancel);
        tv_man = (TextView) findViewById(R.id.tv_sex_man);
        tv_woman = (TextView) findViewById(R.id.tv_sex_woman);

        if ("男".equals(value)) {
            tv_man.setTextColor(context.getResources().getColor(R.color.text_color_sel));
            tv_woman.setTextColor(context.getResources().getColor(R.color.text_color));
        } else if ("女".equals(value)) {
            tv_man.setTextColor(context.getResources().getColor(R.color.text_color));
            tv_woman.setTextColor(context.getResources().getColor(R.color.text_color_sel));
        }

        btn_sure.setOnClickListener(this);
        tv_man.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        tv_woman.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //取消
            case R.id.btn_cancel:
                dismiss();
                break;
            //确定
            case R.id.btn_confirm:
                dismiss();
                if (listener != null) {
                    listener.onSelected(value);
                }
                break;
            //男
            case R.id.tv_sex_man:
                value = "男";
                tv_man.setTextColor(context.getResources().getColor(R.color.text_color_sel));
                tv_woman.setTextColor(context.getResources().getColor(R.color.text_color));
                break;

            //女
            case R.id.tv_sex_woman:
                value = "女";
                tv_man.setTextColor(context.getResources().getColor(R.color.text_color));
                tv_woman.setTextColor(context.getResources().getColor(R.color.text_color_sel));

                break;

        }
    }

    /**
     * 条目选择
     */
    public interface OnItemSelectedListener {
        void onSelected(String value);
    }


    /**
     * 设置监听事件
     */
    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }
}
