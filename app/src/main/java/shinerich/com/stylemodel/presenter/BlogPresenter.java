package shinerich.com.stylemodel.presenter;

import android.app.Activity;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;
import shinerich.com.stylemodel.api.ApiService;
import shinerich.com.stylemodel.base.RxPresenter;
import shinerich.com.stylemodel.bean.BaseResponse;
import shinerich.com.stylemodel.bean.BloggerDetailBean;
import shinerich.com.stylemodel.bean.BloggerIntroContentBean;
import shinerich.com.stylemodel.bean.BloggersContentInfoBean;
import shinerich.com.stylemodel.bean.UserInfo;
import shinerich.com.stylemodel.engin.LoginUserProvider;
import shinerich.com.stylemodel.presenter.contract.BloggerInfoContract;
import shinerich.com.stylemodel.utils.RxUtils;
import shinerich.com.stylemodel.utils.ToastUtils;


/**
 * Created by Administrator on 2016/9/20.
 */
public class BlogPresenter
        extends RxPresenter<BloggerInfoContract.View> implements BloggerInfoContract.Presenter {


    ApiService apiService;

    Context mContext;


    @Inject
    public BlogPresenter(ApiService apiService, Activity mActivity) {
        this.apiService = apiService;
        this.mContext = mActivity;

    }


    @Override
    public void loadData(int blogger_id, final int page, int type) {
        Map<String, String> requestData = new HashMap<>();
        if (LoginUserProvider.currentStatus) {
            UserInfo userInfo = LoginUserProvider.getUser(mContext);
            requestData.put("uid", userInfo.getId());
            requestData.put("key", userInfo.getKey());
        }
        if (type == 0) {
            Subscription subBloggers = apiService.getEditorInfo(blogger_id, page, requestData)
                    .compose(RxUtils.<BaseResponse<BloggerIntroContentBean>>rxSchedulerHelp()).
                            compose(RxUtils.<BloggerIntroContentBean>handlerResult()).
                            subscribe(new Action1<BloggerIntroContentBean>() {
                                @Override
                                public void call(BloggerIntroContentBean detailBean) {
                                    mView.showData(detailBean, page);

                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    ToastUtils.show(mContext, throwable.toString());
                                }
                            });
            addSubscrebe(subBloggers);
        } else {
            Subscription subBloggers = apiService.getBloggerInfo(blogger_id, page, requestData)
                    .compose(RxUtils.<BaseResponse<BloggerIntroContentBean>>rxSchedulerHelp()).
                            compose(RxUtils.<BloggerIntroContentBean>handlerResult()).
                            subscribe(new Action1<BloggerIntroContentBean>() {
                                @Override
                                public void call(BloggerIntroContentBean detailBean) {
                                    mView.showData(detailBean, page);

                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    //ToastUtils.show(mContext, throwable.toString());
                                }
                            });
            addSubscrebe(subBloggers);

        }

    }

    //订阅栏目的操作？

    @Override
    public void subscribeAction(String sub_id, final String type) {
        //0编辑 1博主 2栏目
        if (!LoginUserProvider.currentStatus) {
            mView.notLoginAction();
            return;
        }
        UserInfo userInfo = LoginUserProvider.getUser(mContext);
        Subscription subscription =
                apiService.addSubscripe(userInfo.getId(), userInfo.getKey(), sub_id, type).
                        compose(RxUtils.<BaseResponse<String>>rxSchedulerHelp()).
                        compose(RxUtils.<String>handlerResult()).subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //ToastUtils.show(mContext, e.toString());
                    }

                    @Override
                    public void onNext(String s) {
                        mView.selectorAction(true);
                    }
                });
        addSubscrebe(subscription);
    }


    public void cancelSubAction(String sub_id, String type) {
        //0编辑 1博主 2栏目
        if (!LoginUserProvider.currentStatus) {
            mView.notLoginAction();
            return;
        }
        UserInfo userInfo = LoginUserProvider.getUser(mContext);
        Subscription subscription =
                apiService.cancelSubscripe(userInfo.getId(), userInfo.getKey(), sub_id, type).
                        compose(RxUtils.<BaseResponse<String>>rxSchedulerHelp()).
                        compose(RxUtils.<String>handlerResult()).subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.show(mContext, e.toString());
                    }

                    @Override
                    public void onNext(String s) {
                        mView.selectorAction(false);
                    }
                });
        addSubscrebe(subscription);

    }


}
