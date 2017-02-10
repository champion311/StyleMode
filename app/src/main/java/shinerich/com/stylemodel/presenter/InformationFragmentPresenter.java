package shinerich.com.stylemodel.presenter;

import android.app.Activity;
import android.content.Context;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;
import shinerich.com.stylemodel.api.ApiService;
import shinerich.com.stylemodel.base.RxPresenter;
import shinerich.com.stylemodel.bean.BaseResponse;
import shinerich.com.stylemodel.bean.ContentIndexBean;
import shinerich.com.stylemodel.bean.NightModeEvent;
import shinerich.com.stylemodel.presenter.contract.InformationFragmentContract;
import shinerich.com.stylemodel.utils.NetWorkUtils;
import shinerich.com.stylemodel.utils.RxBus;
import shinerich.com.stylemodel.utils.RxUtils;

/**
 * Created by Administrator on 2016/10/11.
 */
public class InformationFragmentPresenter
        extends RxPresenter<InformationFragmentContract.View>
        implements InformationFragmentContract.Presenter {

    private Context mContext;

    private ApiService apiService;

    @Inject
    public InformationFragmentPresenter(Activity mActivity, ApiService apiService) {

        this.mContext = mActivity;
        this.apiService = apiService;
        registerEvent();
    }

    @Override
    public void loadData(int cate_id, final int page) {
        if (!NetWorkUtils.isNetWorkConnected(mContext)) {
            //提示当前网络错误
            mView.showNetErrorView(page);
            return;
        }
        Subscription s = apiService.getIndexContent(cate_id, page).
                compose(RxUtils.<BaseResponse<ContentIndexBean>>rxSchedulerHelp())
                .compose(RxUtils.<ContentIndexBean>handlerResult()).subscribe(
                        new Observer<ContentIndexBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                                mView.showNetErrorView(page);
                            }

                            @Override
                            public void onNext(ContentIndexBean contentIndexBean) {
                                mView.refreshView(contentIndexBean, page);
                            }
                        }
                );

        addSubscrebe(s);
    }

    void registerEvent() {
        Subscription subscription = RxBus.getInstance().
                toObservable(NightModeEvent.class).
                compose(RxUtils.<NightModeEvent>rxSchedulerHelp()).subscribe(new Action1<NightModeEvent>() {
            @Override
            public void call(NightModeEvent nightModeEvent) {
                mView.nightEvent(nightModeEvent);

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
        addSubscrebe(subscription);


    }

}
