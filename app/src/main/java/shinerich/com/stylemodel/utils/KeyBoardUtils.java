package shinerich.com.stylemodel.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 软键盘工具类
 * 
 * @author Administrator
 * 
 */
public class KeyBoardUtils {

	private KeyBoardUtils(){}


	public static void hideSoftKeyboard(Activity activity) {
		if (activity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (activity.getCurrentFocus() != null)
				((InputMethodManager) activity
						.getSystemService(Context.INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(activity.getCurrentFocus()
								.getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
	/**
	 * 打开键盘
	 * 
	 * @param mEditText
	 * @param mContext
	 */
	public static void openKeybord(EditText mEditText, Context mContext) {
		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
				InputMethodManager.HIDE_IMPLICIT_ONLY);
	}
	

	/**
	 * 关闭键盘
	 * 
	 * @param view
	 * @param mContext
	 */
	public static void closeKeybord(View view, Context mContext) {
		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
}
