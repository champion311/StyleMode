package shinerich.com.stylemodel.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;
import shinerich.com.stylemodel.bean.UserInfo;
import shinerich.com.stylemodel.common.NetDialog;
import shinerich.com.stylemodel.engin.LoginUserProvider;

/**
 * Created by Administrator on 2016/9/13.
 * <p/>
 * 无MVP的Fragment基类
 */
public abstract class SimpleFragment extends Fragment {

    //标记类名
    protected String TAG = "";
    protected View mView;
    protected Activity mActivity;
    protected Context mContext;
    private Toast mToast;
    protected NetDialog netDialog;
    protected Unbinder unbinder;


    /**
     * 获取登陆者信息
     *
     * @return
     */
    public UserInfo getUser() {
        return LoginUserProvider.getUser(getActivity());
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
        mContext = context;
        TAG = getClass().getSimpleName();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutId(), null);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //绑定
        unbinder = ButterKnife.bind(this, view);
        //初始化事件和数据
        initEventAndData();
    }


    /**
     * 显示等待框
     */
    public void showProgressDialog() {
        if (netDialog == null) {
            netDialog = new NetDialog(mContext);
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

    protected abstract int getLayoutId();

    protected abstract void initEventAndData();


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
        if (getActivity() != null) {
            mToast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mToast != null) {
            mToast.cancel();
        }

        //解绑
        if (unbinder != null) {
            unbinder.unbind();
        }
    }


}
