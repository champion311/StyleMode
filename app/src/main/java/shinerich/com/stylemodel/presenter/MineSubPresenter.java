package shinerich.com.stylemodel.presenter;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;
import shinerich.com.stylemodel.api.ApiService;
import shinerich.com.stylemodel.base.RxPresenter;
import shinerich.com.stylemodel.bean.BaseResponse;
import shinerich.com.stylemodel.bean.BloggerInfoBean;
import shinerich.com.stylemodel.bean.MySubscribe;
import shinerich.com.stylemodel.bean.RecommendData;
import shinerich.com.stylemodel.bean.UserInfo;
import shinerich.com.stylemodel.engin.LoginUserProvider;
import shinerich.com.stylemodel.presenter.contract.MineSubContract;
import shinerich.com.stylemodel.utils.RxBus;
import shinerich.com.stylemodel.utils.RxUtils;
import shinerich.com.stylemodel.utils.ToastUtils;

/**
 * Created by Administrator on 2016/10/14.
 */
public class MineSubPresenter extends RxPresenter<MineSubContract.View>
        implements MineSubContract.Presenter {

    ApiService apiService;

    Context mContext;

    @Inject
    public MineSubPresenter(Activity activity, ApiService apiService) {
        this.mContext = activity;
        this.apiService = apiService;
        registerEvent();
    }

    @Override
    public void refreshData() {
        if (!LoginUserProvider.currentStatus) {
            return;
        }
        UserInfo info = LoginUserProvider.getUser(mContext);
        Subscription subscription = apiService.getMySubscribe(info.getId(), info.getKey())
                .compose(RxUtils.<BaseResponse<MySubscribe>>rxSchedulerHelp()).
                        compose(RxUtils.<MySubscribe>handlerResult()).subscribe(new Observer<MySubscribe>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError();

                    }

                    @Override
                    public void onNext(MySubscribe mySubscribe) {
                        mView.showView(mySubscribe);
                    }
                });
        addSubscrebe(subscription);
    }


    public void addMySub(final View view, final String id, final String type) {
        if (!LoginUserProvider.currentStatus) {
            return;
        }
        UserInfo info = LoginUserProvider.getUser(mContext);
        Subscription addSub = apiService.
                addSubscripe(info.getId(), info.getKey(), id, type).
                compose(RxUtils.<BaseResponse<String>>rxSchedulerHelp()).
                compose(RxUtils.<String>handlerResult()).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showError();
            }

            @Override
            public void onNext(String s) {
                ToastUtils.show(mContext, "订阅成功");
                mView.refreshSelectorIcon(id, type, view, true);
            }
        });
        addSubscrebe(addSub);

    }

    public void removeMySub(final View view, final String id, final String type) {
        if (!LoginUserProvider.currentStatus) {
            return;
        }
        UserInfo info = LoginUserProvider.getUser(mContext);
        Subscription remove = apiService.
                cancelSubscripe(info.getId(), info.getKey(), id, type).
                compose(RxUtils.<BaseResponse<String>>rxSchedulerHelp()).
                compose(RxUtils.<String>handlerResult()).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                ToastUtils.show(mContext, "取消订阅成功 ");
                mView.refreshSelectorIcon(id, type, view, false);
            }
        });
        addSubscrebe(remove);
    }
















    void registerEvent() {
        //他的fragment的监听
        Subscription removeRecommend = RxBus.getInstance().
                toObservable(RecommendData.class).compose(RxUtils.<RecommendData>rxSchedulerHelp()).
                subscribe(new Action1<RecommendData>() {
                    @Override
                    public void call(RecommendData recommendData) {
                        mView.updateRecommendData(recommendData);

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
        addSubscrebe(removeRecommend);

        Subscription removeBloggers = RxBus.getInstance().
                toObservable(BloggerInfoBean.class).
                compose(RxUtils.<BloggerInfoBean>rxSchedulerHelp()).subscribe(new Action1<BloggerInfoBean>() {
            @Override
            public void call(BloggerInfoBean bloggerInfoBean) {
                mView.updateBloggerData(bloggerInfoBean);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
        addSubscrebe(removeBloggers);
    }

}
