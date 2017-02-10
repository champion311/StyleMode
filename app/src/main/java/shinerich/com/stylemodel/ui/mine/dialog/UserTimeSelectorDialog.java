package shinerich.com.stylemodel.ui.mine.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.base.BaseDialog;
import shinerich.com.stylemodel.utils.ScreenUtils;

/**
 * 用户日期选择-Dialog
 *
 * @author hunk
 */
public class UserTimeSelectorDialog extends BaseDialog  implements View.OnClickListener {


    private OnItemSelectedListener listener;
    private DatePicker dPicker_date;
    private TextView btn_confirm, btn_cancel;
	private int year,month,day;

    public UserTimeSelectorDialog(Context context) {
        super(context);
        initParams();
    }

    private void initParams() {
        Window window = getWindow();
        setContentView(R.layout.dialog_user_datetime);

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

	private void initData() {
        dPicker_date = (DatePicker) findViewById(R.id.dPicker_date);
        btn_confirm = (TextView) findViewById(R.id.btn_confirm);
        btn_cancel = (TextView) findViewById(R.id.btn_cancel);



		btn_cancel.setOnClickListener(this);
		btn_confirm.setOnClickListener(this);
		Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
		 year = mycalendar.get(Calendar.YEAR);
		 month = mycalendar.get(Calendar.MONTH)+1;
		 day = mycalendar.get(Calendar.DAY_OF_MONTH);

		dPicker_date.setMaxDate(mycalendar.getTime().getTime());
		dPicker_date.init(year, month, day, new DatePicker.OnDateChangedListener() {
			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				UserTimeSelectorDialog.this.year=year;
				month=(monthOfYear + 1);
				day=dayOfMonth;
			}
		});

	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_confirm:
			if (listener != null) {
				listener.onSelected(year,month,day);
			}
			dismiss();
			break;
		case R.id.btn_cancel:
			this.dismiss();
			break;
		default:
			break;
		}
	}





    /**
     *   条目选择
     */
    public interface OnItemSelectedListener {
        void onSelected(int year,int month,int day);
    }


    /**
     *  设置监听事件
     */
    public void setOnItemSelectedListener(OnItemSelectedListener listener){
        this.listener=listener;
    }
}
