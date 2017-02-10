package shinerich.com.stylemodel.presenter;

import android.app.Activity;
import android.content.Context;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import shinerich.com.stylemodel.api.ApiService;
import shinerich.com.stylemodel.api.HomeService;
import shinerich.com.stylemodel.base.RxPresenter;
import shinerich.com.stylemodel.bean.BaseResponse;
import shinerich.com.stylemodel.bean.ColumnItem;
import shinerich.com.stylemodel.bean.NightModeEvent;
import shinerich.com.stylemodel.bean.OpenDrawerEvent;
import shinerich.com.stylemodel.bean.UpdateBean;
import shinerich.com.stylemodel.bean.UserColumn;
import shinerich.com.stylemodel.common.GloableValues;
import shinerich.com.stylemodel.engin.LoginUserProvider;
import shinerich.com.stylemodel.network.GsonManager;
import shinerich.com.stylemodel.network.RetrofitClient;
import shinerich.com.stylemodel.presenter.contract.MainContract;
import shinerich.com.stylemodel.utils.HDateUtils;
import shinerich.com.stylemodel.utils.RxBus;
import shinerich.com.stylemodel.utils.RxUtils;
import shinerich.com.stylemodel.utils.SharedUtils;
import shinerich.com.stylemodel.utils.ThemeHelper;
import shinerich.com.stylemodel.utils.ToastUtils;

/**
 * Created by Administrator on 2016/9/27.
 */
public class MainPresenter extends RxPresenter<MainContract.View> implements MainContract.Presenter {


    private Realm realm;

    private Context mContext;

    private ApiService apiService;

    private RealmResults<UserColumn> datas;

    private int id = 0;


    /**
     * Test Only
     *
     * @return
     */
    public ApiService getApiService() {
        return apiService;
    }

    public void setApiService(ApiService apiService) {
        this.apiService = apiService;
    }

    @Inject
    public MainPresenter(Realm realm, Activity mActivity, ApiService apiService) {
        this.realm = realm;
        this.mContext = (Activity) mActivity;
        this.apiService = apiService;
        if (LoginUserProvider.currentStatus) {
            id = Integer.parseInt(LoginUserProvider.getUser(mContext).getId());
        }
        registerEvent();
    }

    /*Test Only*/
    public MainPresenter(Activity mActivity, ApiService apiService) {
        this.mContext = (Activity) mActivity;
        this.apiService = apiService;
        if (LoginUserProvider.currentStatus) {
            id = Integer.parseInt(LoginUserProvider.getUser(mContext).getId());
        }
        //registerEvent();
    }


    @Override
    public void getCacheTopData() {
        UserColumn userColumn = realm.where(UserColumn.class).equalTo("id", id).findFirst();
        if (userColumn == null) {
            getDataFromWeb(id);
            return;
        }
        Subscription subscription = realm.where(UserColumn.class).findAllAsync().asObservable()
                .flatMap(new Func1<RealmResults<UserColumn>, Observable<UserColumn>>() {
                    @Override
                    public Observable<UserColumn> call(RealmResults<UserColumn> userColumns) {
                        return Observable.from(userColumns);
                    }
                }).filter(new Func1<UserColumn, Boolean>() {
                    @Override
                    public Boolean call(UserColumn userColumn) {
                        return userColumn.getId() == id;
                    }
                }).
                        subscribe(new Observer<UserColumn>() {
                            @Override
                            public void onCompleted() {
                                // ToastUtils.show(mContext, "complete");
                            }

                            @Override
                            public void onError(Throwable e) {
                                mView.showErrorMessage(e.getMessage());
                            }

                            @Override
                            public void onNext(UserColumn userColumn) {
                                List<ColumnItem> mUserColum =
                                        GsonManager.getGson().fromJson(userColumn.getArraysGsonStr()
                                                , new TypeToken<ArrayList<ColumnItem>>() {
                                                }.getType());
                                userColumn.setColumnItems(mUserColum);
                                mView.showListOfTopTab(userColumn);
                            }
                        });
        addSubscrebe(subscription);
    }

    @Override
    public void saveCacheTopData(int id, List<ColumnItem> data) {

        UserColumn dataBaseData =
                realm.where(UserColumn.class).equalTo("id", id).findFirst();

        try {
            realm.beginTransaction();
            if (dataBaseData == null) {
                //如果不存在创建
                dataBaseData = realm.createObject(UserColumn.class, id);
            }
            //dataBaseData.setId(id);
            dataBaseData.setGsonStr(data);
            mView.showListOfTopTab(dataBaseData);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            realm.commitTransaction();
        }
    }

    /**
     * 初始化工作
     */
    void registerEvent() {

        datas = realm.where(UserColumn.class).findAll();
        Subscription subscription = RxBus.getInstance().toObservable(OpenDrawerEvent.class)
                .compose(RxUtils.<OpenDrawerEvent>rxSchedulerHelp()).subscribe(new Observer<OpenDrawerEvent>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //ToastUtils.show(mContext, e.toString());
                    }

                    @Override
                    public void onNext(OpenDrawerEvent openDrawerEvent) {
                        mView.showTopMenu(openDrawerEvent.isOpenDrawer());
                    }
                });
        addSubscrebe(subscription);

        Subscription reCreate = RxBus.getInstance().toObservable(NightModeEvent.class).compose(RxUtils
                .<NightModeEvent>rxSchedulerHelp()).subscribe(new Action1<NightModeEvent>() {
            @Override
            public void call(NightModeEvent nightModeEvent) {
                mView.reCreateAction();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
        addSubscrebe(reCreate);

        ThemeHelper.setNightMode(mContext, false);


    }

    /**
     * 请求默认数据
     */
    @Override
    public void getDataFromWeb(final int id) {
        Subscription webSub = apiService.getNav().
                compose(RxUtils.<BaseResponse<List<ColumnItem>>>rxSchedulerHelp()).
                compose(RxUtils.<List<ColumnItem>>handlerResult()).subscribe(new Observer<List<ColumnItem>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(mContext, e.toString());
            }

            @Override
            public void onNext(List<ColumnItem> columnItems) {
                saveCacheTopData(id, columnItems);
            }
        });
        addSubscrebe(webSub);
//        apiService.getNavv().enqueue(new Callback<BaseResponse<List<ColumnItem>>>() {
//            @Override
//            public void onResponse(Call<BaseResponse<List<ColumnItem>>> call, Response<BaseResponse<List<ColumnItem>>> response) {
//
//            }

//
//            @Override
//            public void onFailure(Call<BaseResponse<List<ColumnItem>>> call, Throwable t) {
//
//            }
//        });

    }

    public void getVersionDataAlter(String version) {
        Subscription updateSub = apiService.updateApp(version, 2).
                compose(RxUtils.<BaseResponse<UpdateBean>>
                        rxSchedulerHelp()).subscribe(new Action1<BaseResponse<UpdateBean>>() {
            @Override
            public void call(BaseResponse<UpdateBean> updateBeanBaseResponse) {
                UpdateBean updateBean = updateBeanBaseResponse.getData();
                if (updateBeanBaseResponse.getCode() != 0) {
                    return;
                }
                if (updateBean.getType() != 0) {

                    if (updateBean.getType() == 1) {
                        //强制更新
                        if (GloableValues.is_first_update) {
                            mView.showUpdateDialog(updateBean);
                        }
                        GloableValues.is_first_update = false;
                    } else if (updateBean.getType() == 2) {
                        //非强制
                        String oldDate = SharedUtils.getStr(mContext, GloableValues.FIRST_UPDATE_DATE);
                        String currentDate = HDateUtils.getCurrentDate();
                        if (!currentDate.equals(oldDate)) {
                            SharedUtils.save(mContext, GloableValues.FIRST_UPDATE_DATE, currentDate);
                            mView.showUpdateDialog(updateBean);
                        }
                    }
                } else {
                    ToastUtils.show(mContext, "已是最新版本");
                }

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
        addSubscrebe(updateSub);

    }


    @Override
    public void getVersionData(String version) {
        RetrofitClient client = RetrofitClient.getInstance();
        HomeService homeService = client.create(HomeService.class);
        //	type 1、ios系统 2、安卓系统
        Call<BaseResponse<UpdateBean>> call = homeService.updateApp(version, 2);
        call.enqueue(new retrofit2.Callback<BaseResponse<UpdateBean>>() {
            @Override
            public void onResponse(Call<BaseResponse<UpdateBean>> call, Response<BaseResponse<UpdateBean>> response) {
                final BaseResponse<UpdateBean> body = response.body();
                if (body.getCode() == 0) {
                    UpdateBean updateBean = body.getData();
                    //强制更新
                    if (updateBean.getType() != 0) {

                        if (updateBean.getType() == 1) {
                            //强制更新
                            if (GloableValues.is_first_update) {
                                mView.showUpdateDialog(updateBean);
                            }
                            GloableValues.is_first_update = false;
                        } else if (updateBean.getType() == 2) {
                            //非强制
                            String oldDate = SharedUtils.getStr(mContext, GloableValues.FIRST_UPDATE_DATE);
                            String currentDate = HDateUtils.getCurrentDate();
                            if (!currentDate.equals(oldDate)) {
                                SharedUtils.save(mContext, GloableValues.FIRST_UPDATE_DATE, currentDate);
                                mView.showUpdateDialog(updateBean);
                            }


                        }


                    } else {
                        ToastUtils.show(mContext, "已是最新版本");
                    }
                } else {
                    ToastUtils.show(mContext, body.getMsg());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<UpdateBean>> call, final Throwable t) {
                t.printStackTrace();
            }
        });
    }

//    void TestUtils() {
//        Observable.create(new Observable.OnSubscribe<Object>() {
//        });
//
//    }


}
