package shinerich.com.stylemodel.presenter.contract;

import android.view.View;

import java.util.List;

import shinerich.com.stylemodel.base.BasePresenter;
import shinerich.com.stylemodel.base.BaseView;
import shinerich.com.stylemodel.bean.BloggerInfoBean;

/**
 * Created by Administrator on 2016/10/14.
 */
public interface HotBloggerContract {

    interface View extends BaseView {

        void showView(List<BloggerInfoBean> bloggerInfoBeen);

        void refreshSelectorIcon(android.view.View view, boolean isSelected, int position);

        void updateFromMineSub(BloggerInfoBean infoBean);
    }

    interface Presenter extends BasePresenter<View> {

        void loadData();
    }
}
