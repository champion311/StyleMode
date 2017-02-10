package shinerich.com.stylemodel.presenter.contract;

import android.view.View;

import shinerich.com.stylemodel.base.BasePresenter;
import shinerich.com.stylemodel.base.BaseView;
import shinerich.com.stylemodel.bean.BloggerInfoBean;
import shinerich.com.stylemodel.bean.ContentData;
import shinerich.com.stylemodel.bean.MySubscribe;
import shinerich.com.stylemodel.bean.RecommendData;

/**
 * Created by Administrator on 2016/10/14.
 */
public interface MineSubContract {

    interface View extends BaseView {

        void showView(MySubscribe mySubscribe);

        void refreshSelectorIcon(String id, String utype, android.view.View view, boolean isSelected);

        void updateRecommendData(RecommendData data);


        void updateBloggerData(BloggerInfoBean bloggerInfoBean);

    }

    interface Presenter extends BasePresenter<View> {

        void refreshData();
    }
}

