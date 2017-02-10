package shinerich.com.stylemodel.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2017/1/16.
 */

public class CirCleDrawableT extends Drawable {

    private Paint mPaint;

    private Bitmap mBitmap;


    private static final int BLACK_COLOR = 0xb2000000;//黑色 背景
    private static final int BLACKGROUDE_ADD_SIZE = 4;//背景比图片多出来的部分
    public int BITMAP_WIDTH, BITMAP_HEIGHT;

    public int getBITMAP_WIDTH() {
        return BITMAP_WIDTH;
    }

    public void setBITMAP_WIDTH(int BITMAP_WIDTH) {
        this.BITMAP_WIDTH = BITMAP_WIDTH;
    }

    public int getBITMAP_HEIGHT() {
        return BITMAP_HEIGHT;
    }

    public void setBITMAP_HEIGHT(int BITMAP_HEIGHT) {
        this.BITMAP_HEIGHT = BITMAP_HEIGHT;
    }

    public CirCleDrawableT(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
        BitmapShader bitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(bitmapShader);

    }

    @Override
    public int getIntrinsicWidth() {
        return BITMAP_WIDTH != 0 ? BITMAP_WIDTH : mBitmap.getWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return BITMAP_HEIGHT != 0 ? BITMAP_HEIGHT : mBitmap.getHeight();
    }

    @Override
    public void draw(Canvas canvas) {
        int radius = Math.min(getIntrinsicHeight(), getIntrinsicWidth()) / 2;
        canvas.drawCircle(getIntrinsicWidth() / 2, getIntrinsicHeight() / 2, radius, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
           mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }


}
