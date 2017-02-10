package shinerich.com.stylemodel.widget.wheelview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

/**
 * Created by Administrator on 2016/12/8.
 */
public class CustomListLayoutMananger extends GridLayoutManager {

    public CustomListLayoutMananger(Context context, int spanCount) {
        super(context, spanCount);
    }

    public CustomListLayoutMananger(Context context, int spanCount, boolean isScrollEnabled) {
        super(context, spanCount);
        this.isScrollEnabled = isScrollEnabled;
    }

    private boolean isScrollEnabled = true;

    public void setScrollEnabled(boolean scrollEnabled) {
        isScrollEnabled = scrollEnabled;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }
}
