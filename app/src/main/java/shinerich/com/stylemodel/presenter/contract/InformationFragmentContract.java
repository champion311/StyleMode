package shinerich.com.stylemodel.presenter.contract;

import shinerich.com.stylemodel.base.BasePresenter;
import shinerich.com.stylemodel.base.BaseView;
import shinerich.com.stylemodel.bean.ContentIndexBean;
import shinerich.com.stylemodel.bean.NightModeEvent;

/**
 * Created by Administrator on 2016/10/11.
 */
public interface InformationFragmentContract {

    interface View extends BaseView {

        void showNetErrorView(int page);

        void refreshView(ContentIndexBean bean, int page);

        void nightEvent(NightModeEvent event);
    }

    interface Presenter extends BasePresenter<View> {

        //TODO 待修改/

        /**
         * @param cate_id 分类id（推荐的分类id传0）
         * @param page    当前页
         */

        void loadData(int cate_id, int page);


    }
}
