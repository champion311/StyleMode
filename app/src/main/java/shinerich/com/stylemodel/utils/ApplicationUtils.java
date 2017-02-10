package shinerich.com.stylemodel.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.WindowManager;

import java.io.File;
import java.util.List;

/**
 * 应用程序工具类
 *
 * @author hunk
 */
public class ApplicationUtils {


    /**
     * 获取当前Activity
     */
    public static String getTopActivity(Context context) {
        ActivityManager manager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        if (runningTaskInfos != null)
            return runningTaskInfos.get(0).topActivity.getClassName();
        else
            return "";
    }


    /**
     * 设置屏幕的背景透明度
     *
     * @param aty
     * @param bgAlpha
     */
    public static void backgroundAlpha(Activity aty, float bgAlpha) {
        WindowManager.LayoutParams lp = aty.getWindow().getAttributes();
        // 0.0-loading_1.0
        lp.alpha = bgAlpha;
        aty.getWindow().setAttributes(lp);
    }

    /**
     * 复制功能
     *
     * @param context
     * @param content
     */
    public static void copy(Context context, String content) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    /**
     * 粘贴功能
     *
     * @param context
     * @return
     */
    public static String paste(Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);

        return cmb.getText().toString().trim();
    }


    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion(Activity activity) {
        try {
            PackageManager manager = activity.getPackageManager();
            PackageInfo info = manager.getPackageInfo(
                    activity.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "v_1.0.1";
        }
    }

    /**
     * App是否运行
     *
     * @param context
     * @return
     */
    public boolean isAppRunning(Context context) {

        boolean isRunning = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getPackageName();
        List<RunningTaskInfo> list = am.getRunningTasks(100);
        for (RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(packageName) &&
                    info.baseActivity.getPackageName().equals(packageName)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    /**
     * 安装APK
     */
    public static void installApk(Context context, String pathName) {
        File apkfile = new File(pathName);
        if (!apkfile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 打开APK
     */
    public static void openApk(Context context, String pathName) {
        if (pathName == null || pathName.length() == 0) {
            return;
        }
        PackageManager manager = context.getPackageManager();
        // 这里的是你下载好的文件路径
        PackageInfo info = manager.getPackageArchiveInfo(pathName,
                PackageManager.GET_ACTIVITIES);
        Log.d("down-openActivity", "安装完成，打开程序");
        if (info != null) {
            Intent intent = manager
                    .getLaunchIntentForPackage(info.applicationInfo.packageName);
            context.startActivity(intent);
        }
    }


}
