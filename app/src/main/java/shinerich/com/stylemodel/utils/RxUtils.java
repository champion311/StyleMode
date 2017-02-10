package shinerich.com.stylemodel.utils;


import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import shinerich.com.stylemodel.AppApplication;
import shinerich.com.stylemodel.bean.BaseResponse;
import shinerich.com.stylemodel.engin.RemoteLogin;

/**
 * Created by Administrator on 2016/9/14.
 */
public class RxUtils {

    //Rxjava的一些工具类

    /**
     * 统一线程处理
     *
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<T, T> rxSchedulerHelp() {
        //compose简化线程
        //Observable.from("233").compose(rxSchedulerHelp());
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 统一处理结果
     *
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<BaseResponse<T>, T> handlerResult() {
        return new Observable.Transformer<BaseResponse<T>, T>() {

            @Override
            public Observable<T> call(Observable<BaseResponse<T>> baseResponseObservable) {
                return baseResponseObservable.flatMap(new Func1<BaseResponse<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(BaseResponse<T> tBaseResponse) {
                        if (tBaseResponse.isOk()) {
                            return createData(tBaseResponse.getData());
                        } else if (tBaseResponse.getCode() == 200) {
                            //退出登录
                            new RemoteLogin().remoteLoginToDo(AppApplication.getAppContext(), true);

                        }
                        return Observable.error(new Exception("ServerBack error"));
                    }
                });
            }
        };
    }


    /**
     * 生成Observable
     *
     * @param <T>
     * @return
     */
    public static <T> Observable<T> createData(final T t) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    subscriber.onNext(t);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }


}
