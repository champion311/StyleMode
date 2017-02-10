package shinerich.com.stylemodel.presenter.contract;

import java.util.List;

import shinerich.com.stylemodel.base.BasePresenter;
import shinerich.com.stylemodel.base.BaseView;
import shinerich.com.stylemodel.bean.ColumnItem;
import shinerich.com.stylemodel.bean.UpdateBean;
import shinerich.com.stylemodel.bean.UserColumn;

/**
 * Created by Administrator on 2016/9/27.
 */
public interface MainContract {


    interface View extends BaseView {

        //void showListOfTopTab(List<ColumnItem> data);

        void showErrorMessage(String message);

        void showListOfTopTab(UserColumn userColumn);//显示上拉菜单内容

        void showTopMenu(boolean flag);//

        void reCreateAction();

        //显示版本更新dialog
        void showUpdateDialog(UpdateBean data);


    }

    interface Presenter extends BasePresenter<View> {

        //void getCacheTopData();
        //用户Id
        void getCacheTopData();

        void saveCacheTopData(int id, List<ColumnItem> data);

        void getDataFromWeb(int id);

        //获取版本更新数据
        void getVersionData(String version);
    }


}
