package shinerich.com.stylemodel.presenter;

import android.app.Activity;
import android.content.Context;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import shinerich.com.stylemodel.api.ApiService;
import shinerich.com.stylemodel.base.RxPresenter;
import shinerich.com.stylemodel.bean.ColumnItem;
import shinerich.com.stylemodel.bean.NightModeEvent;
import shinerich.com.stylemodel.bean.UserColumn;
import shinerich.com.stylemodel.network.GsonManager;
import shinerich.com.stylemodel.presenter.contract.HomePageContract;
import shinerich.com.stylemodel.utils.RxBus;
import shinerich.com.stylemodel.utils.RxUtils;

/**
 * Created by Administrator on 2016/9/29.
 */
public class HomePagePresenter extends RxPresenter<HomePageContract.View> implements
        HomePageContract.Presenter {

    ApiService apiService;

    private Realm realm;

    private Context mContext;

    @Inject
    public HomePagePresenter(ApiService apiService, Realm realm, Activity mActivity) {
        this.apiService = apiService;
        this.realm = realm;
        this.mContext = mActivity;
        registerEvent();
    }


    void registerEvent() {
        //注册Activity和fragment交互的事件
        Subscription s = RxBus.getInstance().toObservable(UserColumn.class).
                compose(RxUtils.<UserColumn>rxSchedulerHelp()).
                subscribe(new Observer<UserColumn>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //ToastUtils.show(mContext, e.toString());
                    }

                    @Override
                    public void onNext(UserColumn userColumn) {
                        mView.showTopTabData(userColumn);
                    }
                });
        addSubscrebe(s);

        Subscription reStartSub = RxBus.getInstance().
                toObservable(NightModeEvent.class).
                compose(RxUtils.<NightModeEvent>rxSchedulerHelp()).subscribe(new Observer<NightModeEvent>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(NightModeEvent userColumn) {

                mView.reStartAction();
            }
        });
        addSubscrebe(reStartSub);

    }


}
