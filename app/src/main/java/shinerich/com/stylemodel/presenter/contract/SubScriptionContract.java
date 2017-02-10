package shinerich.com.stylemodel.presenter.contract;

import java.util.List;

import shinerich.com.stylemodel.base.BasePresenter;
import shinerich.com.stylemodel.base.BaseView;
import shinerich.com.stylemodel.bean.BloggersContentInfoBean;
import shinerich.com.stylemodel.bean.SubHomePageContentBean;

/**
 * Created by Administrator on 2016/9/23.
 */
public interface SubScriptionContract {

    interface View extends BaseView {

        void setViews(BloggersContentInfoBean bean);

        void refreshData();

        void updateSelectors(android.view.View view, String id, String type, int position, boolean isSelected);

        void updateContentData(List<SubHomePageContentBean> datas, int page);

        void notLoginAction();

        void nightEventAction();


    }

    interface Presenter extends BasePresenter<View> {

        void loadData(); //记载内容

        void loadContentData(String uid, String key, int page);


    }
}
