package shinerich.com.stylemodel.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast工具类
 *
 * @author hunk
 */
public class ToastUtils {
    private ToastUtils() {

    }

    private static Toast mToast;

    /**
     * Toast
     *
     * @param context
     * @param text
     */
    public static void show(Context context, String text) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        mToast.show();
    }

    /**
     * 无网络
     */
    public static void showNoNet(Context context) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, "当前网络不可用", Toast.LENGTH_SHORT);
        mToast.show();
    }

    /**
     * 取消
     */
    public static void cancel() {
        if (mToast != null) {
            mToast.cancel();
        }
    }
}
