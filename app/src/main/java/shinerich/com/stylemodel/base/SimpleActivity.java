package shinerich.com.stylemodel.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportActivity;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.bean.UserInfo;
import shinerich.com.stylemodel.common.NetDialog;
import shinerich.com.stylemodel.engin.LoginUserProvider;
import shinerich.com.stylemodel.utils.KeyBoardUtils;

/**
 * Created by Administrator on 2016/9/13.
 * 无MVP的Acitivity基类
 */
public abstract class SimpleActivity extends SupportActivity {

    //标记类名
    protected String TAG = "";
    protected Activity mContext;
    private Toast mToast;
    protected NetDialog netDialog;
    protected Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        TAG = this.getClass().getSimpleName();


        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
        //绑定
        unbinder = ButterKnife.bind(this);
        //初始化事件和数据
        initEventAndData();

    }

    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract void initEventAndData();

    /**
     * 显示等待框
     */
    public void showProgressDialog() {
        if (netDialog == null) {
            netDialog = new NetDialog(this);
        }
        netDialog.show();
    }

    /**
     * 隐藏等待框
     */
    public void hideProgressDialog() {
        if (netDialog != null && netDialog.isShowing()) {
            netDialog.dismiss();
        }
    }


    /**
     * 获取登陆者信息
     *
     * @return UserInfo
     */
    public UserInfo getUser() {
        return LoginUserProvider.getUser(this);
    }

    /**
     * 页面跳转
     *
     * @param context
     * @param cls
     */
    public void startIntent(Context context, Class<?> cls) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(context, cls);
        context.startActivity(intent);
    }

    /**
     * Toast
     *
     * @param text
     */
    public void showToast(String text) {
        if (mToast != null)
            mToast.cancel();
        mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        mToast.show();
    }

    /**
     * 设置标题，带默认的返回按钮监听
     */
    public void setMyTitle(String title) {
        TextView tv_title = (TextView) findViewById(R.id.tv_base_title);
        if (tv_title != null) {
            tv_title.setText(title);
        }

    }

    /**
     * 设置标题颜色
     */
    public void setMyTitleColor(int color) {
        TextView tv_title = (TextView) findViewById(R.id.tv_base_title);
        if (tv_title != null) {
            tv_title.setTextColor(color);
        }

    }


    /**
     * 返回事件
     */
    public void onMyBack() {
        ImageView iv_back = (ImageView) findViewById(R.id.iv_base_back);
        if (iv_back != null) {
            iv_back.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }


    /**
     * 隐藏返回键
     */
    public void hideMyBack() {
        ImageView iv_back = (ImageView) findViewById(R.id.iv_base_back);
        if (iv_back != null) {
            iv_back.setVisibility(View.GONE);
        }
    }

    /**
     * 设置右侧文本
     */
    public void setRightText(String text) {
        ImageView iv_base_right = (ImageView) findViewById(R.id.iv_base_right);
        if (iv_base_right != null) {
            iv_base_right.setVisibility(View.GONE);
        }
        TextView tv_base_right = (TextView) findViewById(R.id.tv_base_right);
        if (tv_base_right != null) {
            tv_base_right.setVisibility(View.VISIBLE);
            tv_base_right.setText(text);
        }

    }

    /**
     * 设置右侧文本事件
     */
    public void setRightTextListener(View.OnClickListener listener) {

        TextView tv_base_right = (TextView) findViewById(R.id.tv_base_right);
        if (tv_base_right != null) {
            tv_base_right.setOnClickListener(listener);
        }

    }

    /**
     * 获取右侧文本
     */
    public TextView getRightText() {

        TextView tv_base_right = (TextView) findViewById(R.id.tv_base_right);
        if (tv_base_right != null) {
            return tv_base_right;
        }
        return null;
    }

    /**
     * 返回事件
     */
    public void onMyBack(View.OnClickListener listener) {
        ImageView iv_back = (ImageView) findViewById(R.id.iv_base_back);
        if (iv_back != null) {
            iv_back.setOnClickListener(listener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mToast != null) {
            mToast.cancel();
        }
        //解绑
        if (unbinder != null) {
            unbinder.unbind();
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //点击空白隐藏键盘
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            KeyBoardUtils.hideSoftKeyboard(this);


        }
        return super.dispatchTouchEvent(ev);
    }

//
//    /**
//     * 设置状态栏颜色
//     *
//     * @param resColor
//     */
//    public void setStatusBarColor(int resColor) {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            View dectorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//            dectorView.setSystemUiVisibility(option);
//            if (resColor != 0) {
//                getWindow().setStatusBarColor(ContextCompat.getColor(this, resColor));
//            }
//        }
//    }



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

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        if (res != -1) {
            tintManager.setStatusBarTintResource(res);

        }
//        tintManager.setNavigationBarTintEnabled(false);
//        //tintManager.setStatusBarTintResource(res);
//        tintManager.setStatusBarTintResource(res);
    }


    public void setSystemBarColorRes() {
        setSystemBarColorRes(-1);
    }
}
