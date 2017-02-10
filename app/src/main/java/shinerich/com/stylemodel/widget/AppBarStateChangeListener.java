package shinerich.com.stylemodel.widget;

import android.support.design.widget.AppBarLayout;

/**
 * Created by Administrator on 2016/9/18.
 */
public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {


    public static final int EXPANDED = 0x00;

    public static final int COLLAPSED = 0x01;

    public static final int IDLE = 0x02;

    public int mCurrentState = IDLE;

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (Math.abs(verticalOffset) <= 50) {
            if (mCurrentState != EXPANDED) {
                onStateChanged(appBarLayout, EXPANDED);
            }
            mCurrentState = EXPANDED;
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            if (mCurrentState != COLLAPSED) {
                onStateChanged(appBarLayout, COLLAPSED);
            }
            mCurrentState = COLLAPSED;
        } else {
            if (mCurrentState != IDLE) {
                onStateChanged(appBarLayout, IDLE);
            }
            mCurrentState = IDLE;
        }


    }


    public abstract void onStateChanged(AppBarLayout appBarLayout, int state);
}
