package shinerich.com.stylemodel.engin;

import android.content.Context;
import android.content.Intent;

import shinerich.com.stylemodel.ui.login.LoginSelectActivity;
import shinerich.com.stylemodel.ui.main.activity.MainAcitivity;

/**
 * 处理异地登陆情况的类
 *
 * @author hunk
 */
public class RemoteLogin {

    /**
     * 异地登陆的处理
     *
     * @param mContext
     * @param isNormal true 代表是正常点击的退出
     */
    public void remoteLoginToDo(final Context mContext, boolean isNormal) {

        //清除数据
        LoginUserProvider.cleanData(mContext);

        if (isNormal) {
            Intent intent = new Intent(mContext, MainAcitivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(intent);

        } else {

            Intent intent = new Intent(mContext, LoginSelectActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(intent);
        }
    }


}
