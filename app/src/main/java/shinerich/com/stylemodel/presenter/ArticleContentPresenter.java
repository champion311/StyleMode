package shinerich.com.stylemodel.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscription;
import rx.functions.Action1;
import shinerich.com.stylemodel.api.ApiService;
import shinerich.com.stylemodel.base.RxPresenter;
import shinerich.com.stylemodel.bean.ArticleContentBean;
import shinerich.com.stylemodel.bean.BaseResponse;
import shinerich.com.stylemodel.bean.Danmu;
import shinerich.com.stylemodel.bean.UserInfo;
import shinerich.com.stylemodel.engin.LoginUserProvider;
import shinerich.com.stylemodel.presenter.contract.ArticleContentContract;
import shinerich.com.stylemodel.utils.RxUtils;
import shinerich.com.stylemodel.utils.ToastUtils;

/**
 * Created by Administrator on 2016/10/31.
 */
public class ArticleContentPresenter extends RxPresenter<ArticleContentContract.View> implements
        ArticleContentContract.Presenter {

    ApiService apiService;

    Context mContext;

    @Inject
    public ArticleContentPresenter(Activity activity, ApiService apiService) {
        this.apiService = apiService;
        this.mContext = activity;
    }


    @Override
    public void loadData(String id, String type, int sign) {
        final Map<String, String> maps = new HashMap<>();
        if (LoginUserProvider.currentStatus) {
            UserInfo info = LoginUserProvider.getUser(mContext);
            maps.put("uid", info.getId());
            maps.put("key", info.getKey());
        }
        maps.put("sign", String.valueOf(sign));
        Subscription subscription = apiService.getArticleBean(maps, id, type).
                compose(RxUtils.<BaseResponse<ArticleContentBean>>rxSchedulerHelp()).
                compose(RxUtils.<ArticleContentBean>handlerResult()).subscribe(new Action1<ArticleContentBean>() {
            @Override
            public void call(ArticleContentBean bean) {
                mView.showView(bean);

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void loadDanmuData(String id, String type) {
        Subscription danMuSub = apiService.getDanmuAllComents(id, type).
                compose(RxUtils.<BaseResponse<List<Danmu>>>rxSchedulerHelp()).
                compose(RxUtils.<List<Danmu>>handlerResult()).subscribe(new Action1<List<Danmu>>() {
            @Override
            public void call(List<Danmu> danmus) {
                mView.showDanmuView(danmus);

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {


            }
        });
        addSubscrebe(danMuSub);
    }


    //    **
//            * 添加评论 发送弹幕
//    *
//            * @param id
//    * @param type
//    * @param content
//    */
    @Override
    public void addComment(String id, String type, String content) {
        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(type)) {
            return;
        }
        HashMap<String, String> maps = new HashMap<>();
        if (LoginUserProvider.currentStatus) {
            UserInfo info = LoginUserProvider.getUser(mContext);
            maps.put("uid", String.valueOf(info.getId()));
            maps.put("key", info.getKey());
        } else {
            mView.notLoginAction();
        }
        maps.put("id", id);
        maps.put("type", type);
        maps.put("content", content);
        Subscription subscription = apiService.addComment(maps).
                compose(RxUtils.<BaseResponse<Danmu>>rxSchedulerHelp()).
                compose(RxUtils.<Danmu>handlerResult()).subscribe(new Action1<Danmu>() {
            @Override
            public void call(Danmu danmu) {
                //添加评论成功
                mView.addUserDanmu(danmu);


            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
        addSubscrebe(subscription);

    }

    @Override
    public void addCollect(String id, final String type) {
        if (!LoginUserProvider.currentStatus) {
            mView.notLoginAction();
            return;
        }
        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(type)) {
            return;
        }
        UserInfo info = LoginUserProvider.getUser(mContext);
        Subscription addCollectSub = apiService.addCollect(info.getId(), info.getKey(), id, type).
                compose(RxUtils.<BaseResponse<String>>rxSchedulerHelp()).
                compose(RxUtils.<String>handlerResult()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                //收藏成功
                mView.setCollected(true);

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                ToastUtils.show(mContext, "收藏成功");
            }
        });
        addSubscrebe(addCollectSub);


    }

    @Override
    public void removeCollect(String id, String type) {
        if (!LoginUserProvider.currentStatus) {
            mView.notLoginAction();
            return;
        }
        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(type)) {
            return;
        }

        UserInfo info = LoginUserProvider.getUser(mContext);
        Subscription addCollectSub = apiService.delCollect(info.getId(), info.getKey(), id, type).
                compose(RxUtils.<BaseResponse<String>>rxSchedulerHelp()).
                compose(RxUtils.<String>handlerResult()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                //取消收藏成功
                mView.setCollected(false);
                ToastUtils.show(mContext, "取消收藏成功");

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
        addSubscrebe(addCollectSub);
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
