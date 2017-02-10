package shinerich.com.stylemodel.presenter.contract;

import android.view.View;

import java.util.List;

import shinerich.com.stylemodel.base.BasePresenter;
import shinerich.com.stylemodel.base.BaseView;
import shinerich.com.stylemodel.bean.RecommendData;
import shinerich.com.stylemodel.bean.UserColumn;

/**
 * Created by Administrator on 2016/10/14.
 */
public interface RecommendContentContract {

    interface View extends BaseView {

        void showView(List<RecommendData> recommendDatas);

        void refreshSelectorIcon(android.view.View view, int position, boolean isSelected);

        void updateDataFromMideSub(RecommendData data);
    }

    interface Presenter extends BasePresenter<View> {

        void loadData();

    }
}
