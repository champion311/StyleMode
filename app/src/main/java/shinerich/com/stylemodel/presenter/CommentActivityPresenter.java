package shinerich.com.stylemodel.presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.functions.Action1;
import shinerich.com.stylemodel.api.ApiService;
import shinerich.com.stylemodel.base.RxPresenter;
import shinerich.com.stylemodel.bean.BaseResponse;
import shinerich.com.stylemodel.bean.CommentResponseBean;
import shinerich.com.stylemodel.bean.Danmu;
import shinerich.com.stylemodel.bean.ReplyBean;
import shinerich.com.stylemodel.bean.UserInfo;
import shinerich.com.stylemodel.engin.LoginUserProvider;
import shinerich.com.stylemodel.presenter.contract.CommentActivityContract;
import shinerich.com.stylemodel.utils.RxUtils;
import shinerich.com.stylemodel.utils.ToastUtils;

/**
 * Created by Administrator on 2016/10/31.
 */
public class CommentActivityPresenter extends
        RxPresenter<CommentActivityContract.View>
        implements CommentActivityContract.Presenter {


    ApiService apiService;

    Context mContext;


    @Inject()
    public CommentActivityPresenter(ApiService apiService, Activity activity) {
        this.apiService = apiService;
        this.mContext = activity;
    }


    @Override
    public void loadData(String id, String type, final String page) {
        HashMap<String, String> maps = new HashMap<>();
        if (LoginUserProvider.currentStatus) {
            UserInfo info = LoginUserProvider.getUser(mContext);
            maps.put("uid", String.valueOf(info.getId()));
            maps.put("key", info.getKey());
        }
        maps.put("id", id);
        maps.put("type", type);
        maps.put("page", page);

        Subscription subscription = apiService.getCommentResponse(maps).
                compose(RxUtils.<BaseResponse<CommentResponseBean>>rxSchedulerHelp())
                .compose(RxUtils.<CommentResponseBean>handlerResult()).subscribe
                        (new Action1<CommentResponseBean>() {
                             @Override
                             public void call(CommentResponseBean bean) {
                                 mView.showData(bean, page);

                             }
                         }, new Action1<Throwable>() {
                             @Override
                             public void call(Throwable throwable) {
                                 mView.showError();
                             }
                         }
                        );
        addSubscrebe(subscription);
    }

    /**
     * 添加评论
     *
     * @param id
     * @param type
     * @param content
     */
    public void addComment(String id, String type, String content) {
        if (!LoginUserProvider.currentStatus) {
            mView.notLoginAction();
            return;
        }

        HashMap<String, String> maps = new HashMap<>();
        UserInfo info = LoginUserProvider.getUser(mContext);
        maps.put("uid", String.valueOf(info.getId()));
        maps.put("key", info.getKey());

        maps.put("id", id);
        maps.put("type", type);
        maps.put("content", content);
        Subscription subscription = apiService.addComment(maps).
                compose(RxUtils.<BaseResponse<Danmu>>rxSchedulerHelp()).
                compose(RxUtils.<Danmu>handlerResult()).subscribe(new Action1<Danmu>() {
            @Override
            public void call(Danmu danmu) {
                //添加评论成功

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mView.showError();
            }
        });
        addSubscrebe(subscription);


    }

    /**
     * 回复
     *
     * @param comment_id 评论ID
     * @param reply_id   回复id 回复评论时候传0
     * @param reply_uid  所回复内容的用户id
     * @param content
     */
    public void addReply(String comment_id, String reply_id,
                         String reply_uid, String content, final int position) {
        if (!LoginUserProvider.currentStatus) {
            mView.notLoginAction();
            return;
        }
        HashMap<String, String> maps = new HashMap<>();
        UserInfo info = LoginUserProvider.getUser(mContext);
        maps.put("uid", String.valueOf(info.getId()));
        maps.put("key", info.getKey());

        maps.put("comment_id", comment_id);
        maps.put("reply_id", reply_id);
        maps.put("content", content);
        Subscription subscription = apiService.addReply(maps).
                compose(RxUtils.<BaseResponse<List<ReplyBean>>>rxSchedulerHelp()).
                compose(RxUtils.<List<ReplyBean>>handlerResult()).
                subscribe(new Action1<List<ReplyBean>>() {
                    @Override
                    public void call(List<ReplyBean> replyBeans) {
                        //添加回复成功
                        mView.addReplyData(replyBeans, position);

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.d("tag", throwable.toString());
                        mView.showError();
                    }
                });
        addSubscrebe(subscription);
    }

    /**
     * 点赞
     *
     * @param view
     * @param comment_id
     * @param position
     */

    public void addPraise(final View view, String comment_id, final int position) {
        if (!LoginUserProvider.currentStatus) {
            mView.notLoginAction();
            return;
        }
        UserInfo info = LoginUserProvider.getUser(mContext);
        Subscription addSub = apiService.addPraise(info.getId(), info.getKey(), comment_id).
                compose(RxUtils.<BaseResponse<String>>rxSchedulerHelp()).
                compose(RxUtils.<String>handlerResult()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                //点赞成功
                ToastUtils.show(mContext, "点赞成功");
                mView.showCommentIsPraised(view, position, true);

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mView.showError();

            }
        });
        addSubscrebe(addSub);
    }

    /**
     * 取消点赞
     *
     * @param view
     * @param comment_id
     * @param position
     */

    public void delPraise(final View view, String comment_id, final int position) {
        if (!LoginUserProvider.currentStatus) {
            mView.notLoginAction();
            return;
        }
        UserInfo info = LoginUserProvider.getUser(mContext);
        Subscription delSub = apiService.delpraise(info.getId(), info.getKey(), comment_id).
                compose(RxUtils.<BaseResponse<String>>rxSchedulerHelp()).
                compose(RxUtils.<String>handlerResult()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                //取消点赞成功
                ToastUtils.show(mContext, "取消点赞成功");
                mView.showCommentIsPraised(view, position, false);

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mView.showError();

            }
        });
        addSubscrebe(delSub);
    }


}
