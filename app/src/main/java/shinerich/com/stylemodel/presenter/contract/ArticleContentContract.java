package shinerich.com.stylemodel.presenter.contract;

import java.util.List;

import shinerich.com.stylemodel.base.BasePresenter;
import shinerich.com.stylemodel.base.BaseView;
import shinerich.com.stylemodel.bean.ArticleContentBean;
import shinerich.com.stylemodel.bean.Danmu;

/**
 * Created by Administrator on 2016/10/31.
 */
public interface ArticleContentContract {

    interface View extends BaseView {

        void showView(ArticleContentBean bean);

        void showDanmuView(List<Danmu> danmus);

        void setCollected(boolean hasCollected);

        void addUserDanmu(Danmu danmu);

        void notLoginAction();

        //void addOrCancelPraiseSuccess();

    }

    interface Presenter extends BasePresenter<View> {

        void loadData(String id, String type, int sign);

        void loadDanmuData(String id, String type);

        void addCollect(String id, String type);

        void removeCollect(String id, String type);

        void addComment(String id, String type, String content);

    }
}
