package shinerich.com.stylemodel.utils;

import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.NonNull;

/**
 * SDCard 工具类
 *
 * @author hunk
 */
public class SDCardUtils {
    private SDCardUtils() {
    }

    /*
     * 判断SDCard是否挂载
     */
    public static boolean isMounted() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /*
     * 获取SDCard绝对物理路径
     */
    @NonNull
    public static String getPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /*
     * 获取Camera绝对物理路径
     */
    @NonNull
    public static String getCameraPath() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
    }

    /*
     * 获取SDCard的全部的空间大小。返回MB字节
     */
    @SuppressWarnings("deprecation")
    public static long getSize() {
        if (isMounted()) {
            StatFs fs = new StatFs(getPath());
            long size = fs.getBlockSize();
            long count = fs.getBlockCount();
            return size * count / 1024 / 1024;
        }
        return 0;
    }

    /*
     * 获取SDCard的剩余的可用空间的大小。返回MB字节
     */
    @SuppressWarnings("deprecation")
    public static long getFreeSize() {
        if (isMounted()) {
            StatFs fs = new StatFs(getPath());

            long size = fs.getBlockSize();
            long count = fs.getAvailableBlocks();

            return size * count / 1024 / 1024;
        }
        return 0;
    }

}
