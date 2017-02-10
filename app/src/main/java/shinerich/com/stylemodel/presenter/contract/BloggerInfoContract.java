package shinerich.com.stylemodel.presenter.contract;

import shinerich.com.stylemodel.base.BasePresenter;
import shinerich.com.stylemodel.base.BaseView;
import shinerich.com.stylemodel.bean.BloggerDetailBean;
import shinerich.com.stylemodel.bean.BloggerIntroContentBean;
import shinerich.com.stylemodel.bean.BloggersContentInfoBean;

/**
 * Created by Administrator on 2016/9/20.
 */
public interface BloggerInfoContract {

    interface View extends BaseView {

        void showData(BloggerIntroContentBean bean, int page);


        void selectorAction(boolean isSub);//当前是否订阅


        void notLoginAction();

    }

    interface Presenter extends BasePresenter<View> {

        void loadData(int blogger_id, int page, int type);

        void subscribeAction(String sub_id, String type);


    }

}
