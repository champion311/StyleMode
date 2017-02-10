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
import shinerich.com.stylemodel.bean.RecommendData;
import shinerich.com.stylemodel.bean.UserInfo;
import shinerich.com.stylemodel.engin.LoginUserProvider;
import shinerich.com.stylemodel.presenter.contract.RecommendContentContract;
import shinerich.com.stylemodel.utils.RxBus;
import shinerich.com.stylemodel.utils.RxUtils;
import shinerich.com.stylemodel.utils.ToastUtils;

/**
 * Created by Administrator on 2016/10/14.
 */
public class RecommendPresenter extends
        RxPresenter<RecommendContentContract.View> implements RecommendContentContract.Presenter {

    ApiService apiService;

    Context mContext;

    @Inject
    public RecommendPresenter(ApiService apiService, Activity activity) {
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
        Subscription subscription =
                apiService.getRecommendData(info.getId(), info.getKey()).
                        compose(RxUtils.<BaseResponse<List<RecommendData>>>rxSchedulerHelp())
                        .compose(RxUtils.<List<RecommendData>>handlerResult()).subscribe(new Observer<List<RecommendData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError();
                    }

                    @Override
                    public void onNext(List<RecommendData> recommendDatas) {
                        mView.showView(recommendDatas);

                    }
                });
        addSubscrebe(subscription);
    }

    public void addRecommend(final View view, RecommendData bean, final int position) {
        if (!LoginUserProvider.currentStatus) {
            return;
        }
        UserInfo info = LoginUserProvider.getUser(mContext);
        Subscription addSub = apiService.
                addSubscripe(info.getId(), info.getKey(), bean.getId(), bean.getType()).
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
                mView.refreshSelectorIcon(view, position, true);
            }
        });
        addSubscrebe(addSub);

    }

    public void removeRecommend(final View view, RecommendData bean, final int position) {
        if (!LoginUserProvider.currentStatus) {
            return;
        }
        UserInfo info = LoginUserProvider.getUser(mContext);
        Subscription remove = apiService.
                cancelSubscripe(info.getId(), info.getKey(), bean.getId(), bean.getType()).
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
                ToastUtils.show(mContext, "订阅失败");
                mView.refreshSelectorIcon(view, position, false);
            }
        });
        addSubscrebe(remove);
    }

    void registerEvent() {
        Subscription subFromMineFragment = RxBus.getInstance().
                toObservable(RecommendData.class).compose
                (RxUtils.<RecommendData>rxSchedulerHelp()).subscribe(new Action1<RecommendData>() {
            @Override
            public void call(RecommendData recommendData) {
                mView.updateDataFromMideSub(recommendData);

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
    }

}
