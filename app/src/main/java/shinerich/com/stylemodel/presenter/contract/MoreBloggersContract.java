package shinerich.com.stylemodel.presenter.contract;

import android.view.View;

import java.util.List;

import shinerich.com.stylemodel.base.BasePresenter;
import shinerich.com.stylemodel.base.BaseView;
import shinerich.com.stylemodel.bean.BloggerInfoBean;

/**
 * Created by Administrator on 2016/10/27.
 */
public interface MoreBloggersContract {

    interface View extends BaseView {

        void showView(List<BloggerInfoBean> bloggerInfoBeen);

        void notLoginAction();

        void updateSelectors(android.view.View view, String id, String type, int position, boolean isSelected);
    }

    interface Presenter extends BasePresenter<View> {

        void loadData();


    }
}
