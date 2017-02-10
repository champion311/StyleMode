package shinerich.com.stylemodel.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import javax.inject.Inject;

import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;
import shinerich.com.stylemodel.AppApplication;
import shinerich.com.stylemodel.inject.component.DaggerFragmentComponent;
import shinerich.com.stylemodel.inject.component.FragmentComponent;
import shinerich.com.stylemodel.inject.module.FragmentModule;

/**
 * Created by Administrator on 2016/8/30.
 */
public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements BaseView {

    @Inject
    protected T mPresenter;

    protected View mView;

    protected Context mContext;
    protected Activity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mActivity = (Activity) mContext;
    }

    public FragmentComponent getFragmentComponent() {
        return DaggerFragmentComponent.builder().appComponent(AppApplication.getAppComponent())
                .fragmentModule(new FragmentModule(this)).build();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutId(), null);
        initInject();
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mPresenter.subscribe(this);
        initEventAndData();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.unsubscribe();
        }

    }

    //TODO WORK
    protected abstract void initInject();

    protected abstract int getLayoutId();

    protected abstract void initEventAndData();
}
