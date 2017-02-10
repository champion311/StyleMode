package shinerich.com.stylemodel;

import android.content.Context;

/**
 * Created by Administrator on 2017/2/7.
 */

public class ContextHolder {

    private static Context sContext;

    public static void setContext(Context mContext) {
        sContext = mContext;
    }

    public static Context getsContext() {
        return sContext;
    }
}
