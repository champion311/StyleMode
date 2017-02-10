package shinerich.com.stylemodel.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import shinerich.com.stylemodel.utils.ToastUtils;

/**
 * Created by Administrator on 2016/9/26.
 */
public class MySlidingUpPaneLayout extends SlidingUpPanelLayout {

    private float mInitialMotionX;

    private float mInitialMotionY;

    private boolean enAbleDrag = false;

    public MyMenuDragUpListener myMenuDragUpListener;


    public void setMyMenuDragUpListener(MyMenuDragUpListener myMenuDragUpListener) {
        this.myMenuDragUpListener = myMenuDragUpListener;
    }

    public void setEnAbleDrag(boolean enAbleDrag) {
        this.enAbleDrag = enAbleDrag;
    }


    public MySlidingUpPaneLayout(Context context) {
        super(context);
    }

    public MySlidingUpPaneLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySlidingUpPaneLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (isInEditMode()) {
            return;
        }
        super.onDraw(canvas);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {


        final float x = ev.getX();
        final float y = ev.getY();


        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mInitialMotionX = x;
                mInitialMotionY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if (y - mInitialMotionY > 50 && !enAbleDrag) {
                    //ToastUtils.show(getContext(), "drag_top_unable");
                    return false;
                } else {
                    //向上滑动的监听
                    if (myMenuDragUpListener != null) {
                        myMenuDragUpListener.call();
                    }
                }
                break;

        }
        return super.onInterceptTouchEvent(ev);

    }

    public interface MyMenuDragUpListener {

        void call();
    }


}
