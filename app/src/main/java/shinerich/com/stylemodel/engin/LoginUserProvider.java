package shinerich.com.stylemodel.engin;

import android.content.Context;

import shinerich.com.stylemodel.bean.UserInfo;
import shinerich.com.stylemodel.utils.DoCacheUtils;

/**
 * 用户信息提供类
 *
 * @author hunk
 */
public class LoginUserProvider {
    private final static String USER_TAG = "userInfo";
    private static UserInfo loginUser;// 当前登陆者
    public static boolean currentStatus;// 当前登录状态，true代表已登录，false代表未登录

    /**
     * 获取当前登陆者的信息
     *
     * @param ctx 上下文
     */
    public static UserInfo getUser(Context ctx) {
        if (loginUser == null) {
            loginUser = (UserInfo) DoCacheUtils.get(ctx).getAsObject(USER_TAG);
            if (loginUser != null) {
                currentStatus = true;
            } else {
                currentStatus = false;
            }
        } else {
            currentStatus = true;
        }
        return loginUser;
    }

    /**
     * 设置内存中的登录用户信息，并根据需要保存到本地
     *
     * @param ctx
     * @param loginUser
     * @param saveDisk  true代表存到本地
     */
    public static void setUser(Context ctx, UserInfo loginUser, boolean saveDisk) {
        LoginUserProvider.loginUser = loginUser;
        if (saveDisk) {
            saveUserInfo(ctx);
        }
    }

    /**
     * 设置内存中的登录用户信息，并根据需要保存到本地
     *
     * @param ctx
     * @param loginUser true代表存到本地
     */
    public static void setUser(Context ctx, UserInfo loginUser) {
        setUser(ctx, loginUser, true);
    }

    /**
     * 登陆者信息修改完后，保存到本地
     *
     * @param ctx 上下文
     * @return 保存成功返回true，其它返回false
     */
    public static synchronized boolean saveUserInfo(Context ctx) {
        if (loginUser != null) {
            DoCacheUtils.get(ctx).put(USER_TAG, loginUser);
            currentStatus = true;
            return true;
        } else {
            currentStatus = false;
            return false;
        }
    }

    /**
     * 清除登陆者的信息，本地、内存中的信息将全部清除，并且变成未登录状态
     *
     * @param ctx 上下文
     */
    public static void cleanData(Context ctx) {
        DoCacheUtils.get(ctx).remove(USER_TAG);
        loginUser = null;
        currentStatus = false;
    }

}
