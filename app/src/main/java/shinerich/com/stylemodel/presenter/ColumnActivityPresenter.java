package shinerich.com.stylemodel.presenter;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import shinerich.com.stylemodel.api.ApiService;
import shinerich.com.stylemodel.base.BasePresenter;
import shinerich.com.stylemodel.base.RxPresenter;
import shinerich.com.stylemodel.bean.BaseResponse;
import shinerich.com.stylemodel.bean.ListByCateBean;
import shinerich.com.stylemodel.bean.UserInfo;
import shinerich.com.stylemodel.engin.LoginUserProvider;
import shinerich.com.stylemodel.presenter.contract.ColumnActivityContract;
import shinerich.com.stylemodel.ui.main.activity.ColumnActivity;
import shinerich.com.stylemodel.utils.RxUtils;
import shinerich.com.stylemodel.utils.ToastUtils;

/**
 * Created by Administrator on 2016/10/24.
 */
public class ColumnActivityPresenter extends RxPresenter<ColumnActivityContract.View>
        implements ColumnActivityContract.Presenter {

    ApiService apiService;

    Context mContext;

    @Inject
    public ColumnActivityPresenter(ApiService apiService) {
        this.apiService = apiService;
        this.mContext = mContext;
    }

    @Override
    public void loadData(String cate_second_id, final int page) {
        Map<String, String> requestData = new HashMap<>();
        if (LoginUserProvider.currentStatus) {
            UserInfo userInfo = LoginUserProvider.getUser(mContext);
            requestData.put("uid", userInfo.getId());
            requestData.put("key", userInfo.getKey());
        }
        requestData.put("cate_second_id", cate_second_id);
        requestData.put("page", page + "");
        Subscription mSub = apiService.getListCate(page, requestData).
                compose(RxUtils.<BaseResponse<ListByCateBean>>rxSchedulerHelp()).
                compose(RxUtils.<ListByCateBean>handlerResult()).subscribe(new Observer<ListByCateBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showError();
            }

            @Override
            public void onNext(ListByCateBean listByCateBean) {
                mView.showData(listByCateBean, page);
            }
        });
        addSubscrebe(mSub);
    }

    //订阅栏目的操作？

    @Override
    public void subscribeAction(String sub_id, String type) {
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
