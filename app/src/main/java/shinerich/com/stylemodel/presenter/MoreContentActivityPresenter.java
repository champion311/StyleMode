package shinerich.com.stylemodel.presenter;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import shinerich.com.stylemodel.api.ApiService;
import shinerich.com.stylemodel.base.RxPresenter;
import shinerich.com.stylemodel.bean.BaseResponse;
import shinerich.com.stylemodel.bean.RecommendData;
import shinerich.com.stylemodel.bean.UserInfo;
import shinerich.com.stylemodel.engin.LoginUserProvider;
import shinerich.com.stylemodel.presenter.contract.MoreContentActivityContract;
import shinerich.com.stylemodel.utils.RxUtils;
import shinerich.com.stylemodel.utils.ToastUtils;

/**
 * Created by Administrator on 2016/11/18.
 */
public class MoreContentActivityPresenter extends
        RxPresenter<MoreContentActivityContract.View>
        implements MoreContentActivityContract.Presenter {

    ApiService apiService;

    Context mContext;


    @Inject
    public MoreContentActivityPresenter(ApiService apiService, Activity mActivity) {
        this.apiService = apiService;
        this.mContext = mActivity;

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


    public void addMySub(final View view, final String id, final String type, final int position) {
        if (!LoginUserProvider.currentStatus) {
            mView.notLoginAction();
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
                mView.updateSelectors(view, id, type, position, true);
                ToastUtils.show(mContext, "订阅成功");

            }
        });
        addSubscrebe(addSub);

    }

    public void removeMySub(final View view, final String id,
                            final String type, final int position) {
        if (!LoginUserProvider.currentStatus) {
            mView.notLoginAction();
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
                mView.updateSelectors(view, id, type, position, false);
                ToastUtils.show(mContext, "取消订阅成功 ");

            }
        });
        addSubscrebe(remove);
    }
}
