package shinerich.com.stylemodel.base;

import android.app.Dialog;
import android.content.Context;

import shinerich.com.stylemodel.R;


/**
 *
 *   BaseDialog
 * 
 * @author hunk
 * 
 */
public abstract class BaseDialog extends Dialog {
	protected Context context;

	public BaseDialog(Context context) {
		this(context, R.style.custom_dialog);
	}

	public BaseDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}
}
