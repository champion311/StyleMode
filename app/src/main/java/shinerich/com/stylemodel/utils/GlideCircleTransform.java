package shinerich.com.stylemodel.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by Administrator on 2016/10/28.
 */
public class GlideCircleTransform extends BitmapTransformation {

    public GlideCircleTransform(Context context) {
        super(context);
    }

    public GlideCircleTransform(BitmapPool bitmapPool) {
        super(bitmapPool);
    }

    public int targetRadius = 0;

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap source, int outWidth, int outHeight) {
        if (source == null) {
            return null;
        }
        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;
        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        //paint.setColor(Color.TRANSPARENT);
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float r = size / 2f;
        if (isRect) {
            if (targetRadius != 0) {
                r = targetRadius;
            }
            RectF rectF = new RectF(0, 0, x + 2 * r, y + 2 * r);
            canvas.drawRoundRect(rectF, r / 2, r / 2, paint);
        } else {
            canvas.drawCircle(r, r, r, paint);
        }
        //
        return result;
    }

    @Override
    public String getId() {
        return getClass().getName();
    }

    private boolean isRect = false;

    public GlideCircleTransform(Context context, boolean isRect) {
        super(context);
        this.isRect = isRect;
    }

    public GlideCircleTransform(Context context, boolean isRect, int radius) {
        super(context);
        this.isRect = isRect;
        this.targetRadius = radius;
    }


}
