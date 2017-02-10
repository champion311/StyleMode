package shinerich.com.stylemodel.presenter.contract;

import android.view.View;

import java.util.List;

import shinerich.com.stylemodel.base.BasePresenter;
import shinerich.com.stylemodel.base.BaseView;
import shinerich.com.stylemodel.bean.CommentResponseBean;
import shinerich.com.stylemodel.bean.ReplyBean;

/**
 * Created by Administrator on 2016/10/31.
 */
public interface CommentActivityContract {

    interface View extends BaseView {

        void showData(CommentResponseBean bean, String page);

        void showCommentIsPraised(android.view.View view, int position, boolean isPraised);

        void addReplyData(List<ReplyBean> replyBeans, int position);

        void notLoginAction();

    }

    interface Presenter extends BasePresenter<View> {

        void loadData(String id, String type, String page);

    }

}
