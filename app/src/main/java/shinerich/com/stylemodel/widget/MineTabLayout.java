package shinerich.com.stylemodel.widget;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;

import com.bilibili.magicasakura.utils.TintManager;
import com.bilibili.magicasakura.widgets.AppCompatBackgroundHelper;
import com.bilibili.magicasakura.widgets.Tintable;

/**
 * Created by Administrator on 2016/12/26.
 */

public class MineTabLayout extends TabLayout implements Tintable, AppCompatBackgroundHelper.BackgroundExtensible {

    private AppCompatBackgroundHelper mBackgroundHelper;

    public MineTabLayout(Context context) {
        super(context);
    }

    public MineTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MineTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (isInEditMode()) {
            return;
        }


    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        super.setBackgroundDrawable(background);
        if (mBackgroundHelper != null) {
            mBackgroundHelper.setBackgroundDrawableExternal(background);
        }
    }

    @Override
    public void setBackgroundTintList(int resId) {

    }

    @Override
    public void setBackgroundTintList(int resId, PorterDuff.Mode mode) {

    }

    @Override
    public void tint() {

    }
}
