package shinerich.com.stylemodel.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import shinerich.com.stylemodel.utils.RxBus;

/**
 * Created by Administrator on 2016/9/20.
 */
public class NetStateChangeReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        RxBus.getInstance().post("233");

    }
}
