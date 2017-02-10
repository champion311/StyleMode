package shinerich.com.stylemodel.utils;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 2016/11/2.
 */
public class MyDefaultAnimator extends DefaultItemAnimator {

    @Override
    public boolean animateRemove(RecyclerView.ViewHolder holder) {
        //return super.animateRemove(holder);
        return false;
    }

    @Override
    public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromX, int fromY, int toX, int toY) {
        //return super.animateChange(oldHolder, newHolder, fromX, fromY, toX, toY);
//        if (oldHolder != null) {
//            oldHolder.item
//        }


        return false;
    }
}
