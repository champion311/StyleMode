package shinerich.com.stylemodel.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 2016/8/26.
 */
public class NetWorkUtils {

    /**
     * 获取手机当前网络状态
     * -loading_1	无网络
     * 0	手机网络
     * loading_1	WiFi网络
     *
     * @return
     * @author fjw
     */
    public static int getNetWorkState(Context myContext) {
        int netWorkState = 0;
        try {
            ConnectivityManager connMgr = (ConnectivityManager) myContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null) {
                switch (networkInfo.getType()) {
                    case 0:
                        netWorkState = 0;
                        break;
                    case 1:
                        netWorkState = 1;
                        break;
                    default:
                        break;
                }
            } else {
                netWorkState = -1;
            }
        } catch (Exception e) {
        }
        return netWorkState;
    }


    /**
     * 检测网络是否可用
     *
     * @param context
     */
    public static boolean isNetWorkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

}
