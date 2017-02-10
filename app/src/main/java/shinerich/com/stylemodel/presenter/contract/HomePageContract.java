package shinerich.com.stylemodel.presenter.contract;

import shinerich.com.stylemodel.base.BasePresenter;
import shinerich.com.stylemodel.base.BaseView;
import shinerich.com.stylemodel.bean.UserColumn;

/**
 * Created by Administrator on 2016/9/29.
 */
public interface HomePageContract {

    interface View extends BaseView {

        //void showListOfTopTab(List<ColumnItem> data);

        void showErrorMessage(String message);

        void showTopTabData(UserColumn userColumn);

        void reStartAction();

    }

    interface Presenter extends BasePresenter<View> {


    }
}
