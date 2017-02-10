package shinerich.com.stylemodel.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Bitmap 工具类
 *
 * @author hunk
 */
public class HBitmapUtils {

    private HBitmapUtils() {


    }

    /**
     * 将bitmap保存指定的路径下
     *
     * @param bm
     * @param dirPath
     * @param picName
     */
    public static File saveBitmap(Bitmap bm, String dirPath, String picName) {
        FileOutputStream out = null;
        File file = new File(dirPath, picName);
        try {

            if (file.exists()) {
                file.delete();
            }
            out = new FileOutputStream(file);
            if (picName.contains(".PNG") || picName.contains(".png")) {
                bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            } else {
                bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            }
            out.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * bitmap转byte[]
     *
     * @param bm
     * @return byte[]
     */
    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * byte[]转bitmap
     *
     * @param b
     * @return
     */
    public static Bitmap bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /**
     * bitmap转Drawable
     *
     * @param context
     * @param bm
     * @return Drawable
     */
    public static Drawable bitmap2Drawable(Context context, Bitmap bm) {
        Drawable drwable = new BitmapDrawable(context.getResources(), bm);
        return drwable;
    }

    /**
     * Drawable转bitmap
     *
     * @param drawable
     * @return bitmap
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {

        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;

    }

    /**
     * 获取bitmap
     *
     * @param path
     * @return Bitmap
     */
    public static Bitmap getBitmapByPath(String path) {
        Bitmap bm = null;
        File dir = new File(path);
        if (dir.exists()) {
            bm = BitmapFactory.decodeFile(path);
        }
        return bm;
    }


    /**
     * 获取比例压缩bitmap
     *
     * @param path
     * @return
     */
    public static Bitmap getBitmapByScale(String path) {

        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;

        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 800f;// 这里设置宽度为800f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(path, newOpts);
        return bitmap;
    }

    /**
     * 获取Bitmap
     *
     * @param url
     * @return Bitmap
     */
    public static Bitmap getBitmapByUrl(String url) {
        URL fileUrl = null;
        Bitmap bitmap = null;

        try {
            fileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            HttpURLConnection conn = (HttpURLConnection) fileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;

    }


    /**
     * 获取质量压缩字节(.JPG图片)
     *
     * @param path
     * @return byte[]
     */
    public static byte[] getBytesByQuality(String path) {

        // 第一步：比例压缩
        Bitmap bitmap = getBitmapByScale(path);

        // 第二步：质量压缩
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 100表示不压缩
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        // 循环判断如果压缩后图片是否大于200kb,大于继续压
        while (baos.toByteArray().length / 1024 > 200) {
            // 重置BAOS即清空BAOS
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 10;// 每次都减少10
        }
        // 把压缩后的数据BAOS存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bmp = BitmapFactory.decodeStream(isBm, null, null);
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] buf = baos.toByteArray();

        if (bmp.isRecycled()) {
            bmp.recycle();
            bmp = null;
        }
        if (baos != null) {
            try {
                baos.close();
                baos = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (isBm != null) {
            try {
                isBm.close();
                isBm = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buf;
    }

//    public static Bitmap getRoundBitmap(Bitmap sourceBimtp) {
//        Bitmap output = Bitmap.createBitmap(sourceBimtp.getWidth(),
//                sourceBimtp.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(sourceBimtp);
//
//        final int color = 0xff424242;
//        Paint paint = new Paint();
//        Rect rect = new Rect(0, 0, sourceBimtp.getWidth(), sourceBimtp.getHeight());
//        RectF rectF = new RectF(rect);
//        float radius = 12f;
//        paint.setAntiAlias(true);
//        paint.setColor();
//
//
//    }


}
