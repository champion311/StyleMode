package shinerich.com.stylemodel.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;

/**
 * Activity管理 工具类
 * 
 * @author hunk
 * 
 */
public class ActivityManagerUtils {
	private ActivityManagerUtils(){

	}
	private static Stack<Activity> activityStack = new Stack<Activity>();
	/**
	 * 添加Activity
	 * 
	 * @param activity
	 */
	public static void addActivity(Activity activity) {
		activityStack.add(activity);
	}

	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public static Activity currentActivity() {
		return activityStack.lastElement();
	}

	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public static void finishActivity() {
		Activity activity = activityStack.lastElement();
		if (activity != null) {
			finishActivity(activity);
		}
	}

	/**
	 * 结束指定的Activity
	 */
	public static void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public static void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * 结束所有Activity
	 */
	public static void finishAllActivity() {
		for (Activity activity : activityStack) {
			if (activity != null) {
				activity.finish();
			}
		}
		activityStack.clear();
	}

	
	/**
	 * 退出应用程序
	 */
	public static void AppExit(Context context) {
		try {
			//结束所有Activity
			finishAllActivity();
			ActivityManager activityMgr = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.killBackgroundProcesses(context.getPackageName());
			System.exit(0);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}