package shinerich.com.stylemodel.presenter.contract;

import shinerich.com.stylemodel.base.BasePresenter;
import shinerich.com.stylemodel.base.BaseView;
import shinerich.com.stylemodel.bean.ListByCateBean;
import shinerich.com.stylemodel.bean.UserColumn;

/**
 * Created by Administrator on 2016/10/24.
 */
public interface ColumnActivityContract {

    interface View extends BaseView {

        void showData(ListByCateBean data, int page);

        void selectorAction(boolean isSub);

        void notLoginAction();
    }

    interface Presenter extends BasePresenter<View> {

        void loadData(String cate_second_id, int page);

        void subscribeAction(String sub_id, String type);
    }
}
