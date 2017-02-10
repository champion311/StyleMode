package shinerich.com.stylemodel.base;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bilibili.magicasakura.utils.ThemeUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import javax.inject.Inject;

import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportActivity;
import shinerich.com.stylemodel.AppApplication;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.inject.component.ActivityComponent;
import shinerich.com.stylemodel.inject.component.DaggerActivityComponent;
import shinerich.com.stylemodel.inject.module.ActivityModuel;
import shinerich.com.stylemodel.utils.KeyBoardUtils;
import shinerich.com.stylemodel.utils.ThemeHelper;

/**
 * Created by Administrator on 2016/8/30.
 */
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements
        BaseView {

    protected static String tag = "";

    protected Context mContext;


    @Inject
    protected T mPresenter;

    public T getmPresenter() {
        return mPresenter;
    }

    public static BaseActivity instance;

    public static BaseActivity getThis() {
        return instance;
    }

    private ActivityComponent mActivityComponent;

    //TODO 待修改


    @Override
    public void onCreate(Bundle savedInstanceState) {
        //setStatusBarColor();
        instance = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        if (getContentViewID() != 0) {
            setContentView(getContentViewID());
        }
        ButterKnife.bind(this);
        tag = this.getClass().getSimpleName();
        mContext = this;
        injectDaggger(getActivityComponent());
        if (mPresenter != null) {
            mPresenter.subscribe(this);
        }
        initViewAndEvents();


        //ActivityManagerUtils.addActivity(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.unsubscribe();
        }
        //ActivityManagerUtils.finishActivity(this);

    }


    public ActivityComponent getActivityComponent() {
        if (mActivityComponent == null) {

            mActivityComponent = DaggerActivityComponent.builder().
                    appComponent(AppApplication.getAppComponent()).
                    activityModuel(new ActivityModuel(this)).build();

        }
        return mActivityComponent;
    }

    /**
     * @param res colorRes
     */

    public void setStatusBarColor(int res) {
        if (Build.VERSION.SDK_INT >= 21) {
            View dectorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            dectorView.setSystemUiVisibility(option);
            if (res != 0) {
                getWindow().setStatusBarColor(getResources().getColor(res));
                // getWindow().setNavigationBarColor(getResources().getColor(res));
            } else {
                getWindow().setStatusBarColor(Color.TRANSPARENT);

            }
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    public void setStatusBarColor() {
        setStatusBarColor(0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //点击空白隐藏键盘
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            KeyBoardUtils.hideSoftKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     * bind activity_blogger resource file
     */
    protected abstract int getContentViewID();

    /**
     * inject Daggger
     *
     * @param activityComponent
     */

    protected abstract void injectDaggger(ActivityComponent activityComponent);


    /**
     * initViews
     */
    protected abstract void initViewAndEvents();


    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    public void setSystemBarColorRes(@ColorRes int res) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        if (res != -1) {
            tintManager.setStatusBarTintResource(res);
        }
    }

    public void setSystemBarColorValue(int value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        if (value != -1) {
            tintManager.setStatusBarTintColor(value);
        }
    }


    public void setSystemBarColorRes() {
        setSystemBarColorRes(-1);
    }


    public void switchNightMode(boolean isNightMode) {
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        }
        //}
        ThemeHelper.setNightMode(this, isNightMode);

    }


}
