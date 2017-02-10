package shinerich.com.stylemodel.presenter.contract;

import java.util.List;

import shinerich.com.stylemodel.base.BasePresenter;
import shinerich.com.stylemodel.base.BaseView;
import shinerich.com.stylemodel.bean.ColumnItem;
import shinerich.com.stylemodel.bean.RecommendData;
import shinerich.com.stylemodel.bean.UserColumn;

/**
 * Created by Administrator on 2016/11/18.
 */
public interface MoreContentActivityContract {

    interface View extends BaseView {


        void showView(List<RecommendData> datas);

        void notLoginAction();

        void updateSelectors(android.view.View view, String id, String type, int position, boolean isSelected);
    }

    interface Presenter extends BasePresenter<View> {

        void loadData();

    }
}
