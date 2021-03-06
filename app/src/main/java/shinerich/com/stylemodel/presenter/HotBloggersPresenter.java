package shinerich.com.stylemodel.presenter;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;
import shinerich.com.stylemodel.api.ApiService;
import shinerich.com.stylemodel.base.RxPresenter;
import shinerich.com.stylemodel.bean.BaseResponse;
import shinerich.com.stylemodel.bean.BloggerInfoBean;
import shinerich.com.stylemodel.bean.RecommendData;
import shinerich.com.stylemodel.bean.UserInfo;
import shinerich.com.stylemodel.engin.LoginUserProvider;
import shinerich.com.stylemodel.presenter.contract.HotBloggerContract;
import shinerich.com.stylemodel.utils.RxBus;
import shinerich.com.stylemodel.utils.RxUtils;
import shinerich.com.stylemodel.utils.ToastUtils;

/**
 * Created by Administrator on 2016/10/14.
 */
public class HotBloggersPresenter extends RxPresenter<HotBloggerContract.View>
        implements HotBloggerContract.Presenter {

    ApiService apiService;

    Context mContext;

    @Inject
    public HotBloggersPresenter(ApiService apiService, Activity activity) {
        this.apiService = apiService;
        this.mContext = activity;
        registerEvent();
    }


    @Override
    public void loadData() {
        if (!LoginUserProvider.currentStatus) {
            return;
        }
        UserInfo info = LoginUserProvider.getUser(mContext);
        //Subscription subscription = apiService.getHotBloggers();
        Subscription subscription = apiService.getHotBloggers(info.getId(), info.getKey()).
                compose(RxUtils.<BaseResponse<List<BloggerInfoBean>>>rxSchedulerHelp()).
                compose(RxUtils.<List<BloggerInfoBean>>handlerResult()).subscribe(new Observer<List<BloggerInfoBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<BloggerInfoBean> bloggerInfoBeen) {
                mView.showView(bloggerInfoBeen);
            }
        });
        addSubscrebe(subscription);

    }

    public void addRecommend(final View view, BloggerInfoBean bean, final int position) {
        if (!LoginUserProvider.currentStatus) {
            return;
        }
        UserInfo info = LoginUserProvider.getUser(mContext);
        Subscription addSub = apiService.
                addSubscripe(info.getId(), info.getKey(), bean.getId(), 1 + "").
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
                mView.refreshSelectorIcon(view, true, position);
            }
        });
        addSubscrebe(addSub);

    }

    public void removeRecommend(final View view, BloggerInfoBean bean, final int position) {
        if (!LoginUserProvider.currentStatus) {
            return;
        }
        UserInfo info = LoginUserProvider.getUser(mContext);
        Subscription remove = apiService.
                cancelSubscripe(info.getId(), info.getKey(), bean.getId(), 1 + "").
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
                ToastUtils.show(mContext, "订阅取消");
                mView.refreshSelectorIcon(view, false, position);


            }
        });
        addSubscrebe(remove);
    }

    /**
     * 监听从首页中我的订阅fragment的变化
     */
    void registerEvent() {
        Subscription updateFromMineSub = RxBus.getInstance().
                toObservable(BloggerInfoBean.class).
                compose(RxUtils.<BloggerInfoBean>rxSchedulerHelp()).subscribe(new Action1<BloggerInfoBean>() {
            @Override
            public void call(BloggerInfoBean bloggerInfoBean) {
                mView.updateFromMineSub(bloggerInfoBean);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
        addSubscrebe(updateFromMineSub);
    }


}
