package shinerich.com.stylemodel.presenter.contract;

import java.util.List;

import shinerich.com.stylemodel.base.BasePresenter;
import shinerich.com.stylemodel.base.BaseView;
import shinerich.com.stylemodel.bean.BloggerInfoBean;
import shinerich.com.stylemodel.bean.Danmu;
import shinerich.com.stylemodel.bean.ImageAlbumBean;
import shinerich.com.stylemodel.bean.ImageHideEvent;

/**
 * Created by Administrator on 2016/11/15.
 */
public interface ImageAlbumContract {

    interface View extends BaseView {

        void setView(ImageAlbumBean bean);

        void showDanmuView(List<Danmu> danmus);

        void addUserDanmu(Danmu danmu);

        void setCollected(boolean hasCollected);

        void notLoginAction();

        void hideOrShowView(ImageHideEvent event);

    }

    interface Presenter extends BasePresenter<View> {

        void loadData(String id, String type);

        void loadDanmu(String id, String type);

        void addComment(String id, String type, String content);


        void addCollect(String id, String type);

        void removeCollect(String id, String type);
    }

}
