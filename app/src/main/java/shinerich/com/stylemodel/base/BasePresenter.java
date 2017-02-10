package shinerich.com.stylemodel.base;

/**
 * Created by Administrator on 2016/8/30.
 */
public interface BasePresenter<T extends BaseView> {

    void subscribe(T view);//绑定 CompositeSubscription

    void unsubscribe();//解绑 CompositeSubscription


}
