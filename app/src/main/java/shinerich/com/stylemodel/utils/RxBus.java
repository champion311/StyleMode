package shinerich.com.stylemodel.utils;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;

/**
 * Created by Administrator on 2016/9/13.
 */
public class RxBus {


    private static volatile RxBus instance;

    //SerializedSubject线程安全
    private final SerializedSubject<Object, Object> subject;

    private RxBus() {

        subject = new SerializedSubject<>(PublishSubject.create());

    }

    public static RxBus getInstance() {

        if (instance == null) {

            synchronized (RxBus.class) {

                if (instance == null) {

                    instance = new RxBus();

                }

            }

        }

        return instance;

    }

    public void post(Object object) {

        subject.onNext(object);

    }

    public <T> Observable<T> toObservable(final Class<T> type) {

        return subject.ofType(type);
        //先过滤类型ofTYPe方法回先过滤类型
        //bus.filter(eventType::isInstance).cast(eventType)

    }

    public boolean hasObservers() {

        return subject.hasObservers();


    }
//    注：
//            loading_1、Subject同时充当了Observer和Observable的角色，Subject是非线程安全的，要避免该问题，需要将 Subject转换为一个 SerializedSubject ，上述RxBus类中把线程非安全的PublishSubject包装成线程安全的Subject。
//
//    文／YoKey（简书作者）
    //RxBus异常处理

}
