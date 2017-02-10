package shinerich.com.stylemodel.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/10/21.
 */
public class MyColumnarView extends View {

    public int value = 10;

    public int totalValue = 100;

    public static final float maxHeigt = 250.0f;//px

    public int mFillColor = Color.BLACK;

    private int mRadius = 20;//px

    private Paint mPaint = new Paint();

    public MyColumnarView(Context context) {
        super(context);
        initView();
    }

    public MyColumnarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyColumnarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    public void initView() {


    }

    @Override
    protected void onDraw(Canvas canvas) {
        float RectHeight = getMeasureRectHeight(value);
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        RectF rect1 = new RectF(0f, 0f, 2.0f * mRadius, 2.0f * mRadius);
        canvas.drawArc(rect1, 0, -180, true, mPaint);

        RectF rectF2 = new RectF(0, mRadius, 2 * mRadius, mRadius + RectHeight);
        canvas.drawRect(rectF2, mPaint);

        RectF rectF3 = new RectF(0, mRadius + RectHeight, 2 * mRadius, RectHeight + 2 * mRadius);
        canvas.drawArc(rectF3, 0, 180, true, mPaint);


        super.onDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public float getMeasureRectHeight(int mNumber) {
        return (float) mNumber * maxHeigt / 100.0f;
    }

}


