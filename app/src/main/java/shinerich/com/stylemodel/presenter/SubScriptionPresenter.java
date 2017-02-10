package shinerich.com.stylemodel.presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;
import shinerich.com.stylemodel.api.ApiService;
import shinerich.com.stylemodel.base.RxPresenter;
import shinerich.com.stylemodel.bean.BaseResponse;
import shinerich.com.stylemodel.bean.BloggersContentInfoBean;
import shinerich.com.stylemodel.bean.NightModeEvent;
import shinerich.com.stylemodel.bean.RefreshDataEvent;
import shinerich.com.stylemodel.bean.SubHomePageContentBean;
import shinerich.com.stylemodel.bean.UserInfo;
import shinerich.com.stylemodel.engin.LoginUserProvider;
import shinerich.com.stylemodel.presenter.contract.SubScriptionContract;
import shinerich.com.stylemodel.utils.RxBus;
import shinerich.com.stylemodel.utils.RxUtils;
import shinerich.com.stylemodel.utils.ToastUtils;

/**
 * Created by Administrator on 2016/9/23.
 */
public class SubScriptionPresenter extends RxPresenter<SubScriptionContract.View>
        implements SubScriptionContract.Presenter {

    ApiService apiService;

    Context mContext;

    @Inject
    public SubScriptionPresenter(Activity mActivity, ApiService apiService) {
        this.mContext = mActivity;
        this.apiService = apiService;
        registerEvent();
    }


    @Override
    public void loadData() {
        HashMap<String, String> maps = new HashMap<>();
        if (LoginUserProvider.currentStatus) {
            UserInfo info = LoginUserProvider.getUser(mContext);
            maps.put("uid", info.getId());
            maps.put("key", info.getKey());
        }
        Subscription subContent = apiService.getBloggersIndex(maps).
                compose(RxUtils.<BaseResponse<BloggersContentInfoBean>>rxSchedulerHelp()).
                compose(RxUtils.<BloggersContentInfoBean>handlerResult()).subscribe(new Observer<BloggersContentInfoBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                Log.d("SubScriptionPresenter", e.toString());

            }

            @Override
            public void onNext(BloggersContentInfoBean bloggersContentInfoBean) {
                mView.setViews(bloggersContentInfoBean);
            }
        });
        addSubscrebe(subContent);

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
                mView.showError();
            }

            @Override
            public void onNext(String s) {
                mView.updateSelectors(view, id, type, position, false);
                ToastUtils.show(mContext, "取消订阅成功 ");

            }
        });
        addSubscrebe(remove);
    }


    @Override
    public void loadContentData(String uid, String key, final int page) {
//        if (!LoginUserProvider.currentStatus) {
//            mView.notLoginAction();
//        }
        Subscription loadContentSub = apiService.getSubList(uid, key, page).
                compose(RxUtils.

                        <BaseResponse<List<SubHomePageContentBean>>>rxSchedulerHelp()).
                compose(RxUtils.<List<SubHomePageContentBean>>handlerResult()).subscribe(new Action1<List<SubHomePageContentBean>>() {
            @Override
            public void call(List<SubHomePageContentBean> subHomePageContentBeen) {
                mView.updateContentData(subHomePageContentBeen, page);


            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mView.showError();

            }
        });
        addSubscrebe(loadContentSub);
    }


    void registerEvent() {
        Subscription refreshEvent = RxBus.getInstance().toObservable(RefreshDataEvent.class).
                compose(RxUtils.<RefreshDataEvent>rxSchedulerHelp()).subscribe(new Action1<RefreshDataEvent>() {
            @Override
            public void call(RefreshDataEvent refreshDataEvent) {
                mView.refreshData();

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mView.showError();
            }
        });
        addSubscrebe(refreshEvent);


        //切换为夜间模式哦
        Subscription nightEvent = RxBus.getInstance().
                toObservable(NightModeEvent.class).
                compose(RxUtils.<NightModeEvent>rxSchedulerHelp()).subscribe(new Action1<NightModeEvent>() {
            @Override
            public void call(NightModeEvent event) {
                mView.nightEventAction();

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
        addSubscrebe(nightEvent);


    }


}
