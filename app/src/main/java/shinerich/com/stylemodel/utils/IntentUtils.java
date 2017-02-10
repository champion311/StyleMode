package shinerich.com.stylemodel.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * Intent 工具类
 *
 * @author hunk
 */
public class IntentUtils {

    private void IntentUtils() {

    }

    /**
     * 页面跳转
     *
     * @param context
     * @param intent
     */
    public static void startIntent(Context context, Intent intent) {
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }
    }

    /**
     * 页面跳转带返回值
     *
     * @param aty
     * @param intent
     * @param requestCode
     */
    public static void startIntent(Activity aty, Intent intent,
                                   final int requestCode) {
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            aty.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 打开相册
     *
     * @param aty
     * @param requestCode
     */
    public static void openAlbum(Activity aty, final int requestCode) {
        Intent intentFromGallery = new Intent();
        intentFromGallery.setType("image/*"); // 设置文件类型
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        aty.startActivityForResult(intentFromGallery, requestCode);
    }

    /**
     * 打开相机
     *
     * @param aty
     * @param file
     * @param requestCode
     */
    public static void openCamera(Activity aty, File file, int requestCode) {

        try {
            Intent openCameraIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            Uri imageUri = Uri.fromFile(file);
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            aty.startActivityForResult(openCameraIntent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 裁剪图片
     *
     * @param aty
     * @param path
     * @param requestCode
     */
    public static void cropImage(Activity aty, String path,
                                 final int requestCode) {

        Uri mUri = Uri.fromFile(new File(path));
        if (null == mUri)
            return;
        Intent intent = new Intent();
        intent.setAction("com.android.camera.action.CROP");
        // mUri是已经选择的图片URI
        intent.setDataAndType(mUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);// 输出图片大小
        intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        aty.startActivityForResult(intent, requestCode);
    }

    /**
     * 安装APK
     *
     * @param aty
     * @param apkPath
     * @param requestCode
     */
    public static void installApk(Activity aty, String apkPath,
                                  final int requestCode) {

        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(new File(apkPath)),
                "application/vnd.android.package-archive");
        aty.startActivityForResult(intent, requestCode);
    }

    /**
     * 卸载APK
     *
     * @param aty
     * @param packageName
     * @param requestCode
     */
    public static void unInstallApk(Activity aty, String packageName,
                                    final int requestCode) {

        Intent intent = new Intent();
        intent.setAction("android.intent.action.DELETE");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("package:" + packageName));
        aty.startActivityForResult(intent, 0);
    }

    /**
     * 查看APK详情
     *
     * @param context
     * @param packageName
     */
    public static void toApkDetail(Context context, String packageName) {

        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + packageName));
        context.startActivity(intent);
    }

    /**
     * 打开APK
     *
     * @param context
     * @param packageName
     */
    public static void openApk(Context context, String packageName) {

        PackageManager manager = context.getPackageManager();
        Intent launchIntentForPackage = manager
                .getLaunchIntentForPackage(packageName);
        if (launchIntentForPackage != null) {
            context.startActivity(launchIntentForPackage);
        }
    }

    /**
     * 打开浏览器
     */
    public static void openBrowser(Context context, String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        context.startActivity(intent);
    }

}
