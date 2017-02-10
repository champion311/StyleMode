package shinerich.com.stylemodel.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscription;
import rx.functions.Action1;
import shinerich.com.stylemodel.api.ApiService;
import shinerich.com.stylemodel.base.RxPresenter;
import shinerich.com.stylemodel.bean.BaseResponse;
import shinerich.com.stylemodel.bean.Danmu;
import shinerich.com.stylemodel.bean.ImageAlbumBean;
import shinerich.com.stylemodel.bean.ImageHideEvent;
import shinerich.com.stylemodel.bean.UserInfo;
import shinerich.com.stylemodel.engin.LoginUserProvider;
import shinerich.com.stylemodel.presenter.contract.ImageAlbumContract;
import shinerich.com.stylemodel.utils.RxBus;
import shinerich.com.stylemodel.utils.RxUtils;
import shinerich.com.stylemodel.utils.ToastUtils;

/**
 * Created by Administrator on 2016/11/15.
 */
public class ImageAlbumPresenter extends RxPresenter<ImageAlbumContract.View> implements
        ImageAlbumContract.Presenter {

    ApiService apiService;

    Context mContext;

    @Inject
    public ImageAlbumPresenter(ApiService apiService, Activity mActivity) {
        this.apiService = apiService;
        this.mContext = mActivity;
        registerEvent();
    }


    @Override
    public void loadData(String id, String type) {
        final Map<String, String> maps = new HashMap<>();
        if (LoginUserProvider.currentStatus) {
            UserInfo info = LoginUserProvider.getUser(mContext);
            maps.put("uid", info.getId());
            maps.put("key", info.getKey());
        }

        Subscription albumSub = apiService.getAlbum(maps, id, type).
                compose(RxUtils.<BaseResponse<ImageAlbumBean>>
                        rxSchedulerHelp()).compose(RxUtils.<ImageAlbumBean>handlerResult()).subscribe(new Action1<ImageAlbumBean>() {
            @Override
            public void call(ImageAlbumBean imageAlbumBean) {
                mView.setView(imageAlbumBean);

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mView.showError();

            }
        });
        addSubscrebe(albumSub);
    }

    @Override
    public void loadDanmu(String id, String type) {
        Subscription danMuSub = apiService.getDanmuAllComents(id, type).
                compose(RxUtils.<BaseResponse<List<Danmu>>>rxSchedulerHelp()).
                compose(RxUtils.<List<Danmu>>handlerResult()).subscribe(new Action1<List<Danmu>>() {
            @Override
            public void call(List<Danmu> danmus) {
                mView.showDanmuView(danmus);

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.d("tag", throwable.toString());
                mView.showError();
            }
        });

    }

    @Override
    public void addComment(String id, String type, String content) {
        if (!LoginUserProvider.currentStatus) {
            mView.notLoginAction();
            return;
        }
        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(type)) {
            return;
        }
        HashMap<String, String> maps = new HashMap<>();
        if (LoginUserProvider.currentStatus) {
            UserInfo info = LoginUserProvider.getUser(mContext);
            maps.put("uid", String.valueOf(info.getId()));
            maps.put("key", info.getKey());
        }
        maps.put("id", id);
        maps.put("type", type);
        maps.put("content", content);
        Subscription subscription = apiService.addComment(maps).
                compose(RxUtils.<BaseResponse<Danmu>>rxSchedulerHelp()).
                compose(RxUtils.<Danmu>handlerResult()).subscribe(new Action1<Danmu>() {
            @Override
            public void call(Danmu danmu) {
                //添加评论成功
                mView.addUserDanmu(danmu);


            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mView.showError();
            }
        });
        addSubscrebe(subscription);
    }


    @Override
    public void addCollect(String id, final String type) {
        if (!LoginUserProvider.currentStatus) {
            mView.notLoginAction();
            return;
        }
        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(type)) {
            return;
        }

        UserInfo info = LoginUserProvider.getUser(mContext);
        Subscription addCollectSub = apiService.addCollect(info.getId(), info.getKey(), id, type).
                compose(RxUtils.<BaseResponse<String>>rxSchedulerHelp()).
                compose(RxUtils.<String>handlerResult()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                //收藏成功
                mView.setCollected(true);

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
               mView.showError();
            }
        });
        addSubscrebe(addCollectSub);
    }

    @Override
    public void removeCollect(String id, String type) {
        if (!LoginUserProvider.currentStatus) {
            mView.notLoginAction();
            return;
        }
        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(type)) {
            return;
        }

        UserInfo info = LoginUserProvider.getUser(mContext);
        Subscription addCollectSub = apiService.delCollect(info.getId(), info.getKey(), id, type).
                compose(RxUtils.<BaseResponse<String>>rxSchedulerHelp()).
                compose(RxUtils.<String>handlerResult()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                //取消收藏成功
                mView.setCollected(false);
                ToastUtils.show(mContext, "取消收藏成功");

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                 mView.showError();
            }
        });
        addSubscrebe(addCollectSub);
    }

    void registerEvent() {
        Subscription subscription = RxBus.getInstance().toObservable(ImageHideEvent.class).
                compose(RxUtils.<ImageHideEvent>rxSchedulerHelp()).subscribe(new Action1<ImageHideEvent>() {
            @Override
            public void call(ImageHideEvent imageHideEvent) {
                mView.hideOrShowView(imageHideEvent);

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mView.showError();
            }
        });
        addSubscrebe(subscription);
    }

}
