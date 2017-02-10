package shinerich.com.stylemodel.inject.component;

import android.app.Application;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Component;
import io.realm.Realm;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import shinerich.com.stylemodel.AppApplication;
import shinerich.com.stylemodel.api.ApiService;
import shinerich.com.stylemodel.inject.module.AppModule;
import shinerich.com.stylemodel.inject.scope.ContextLife;
import shinerich.com.stylemodel.presenter.HomePagePresenter;
import shinerich.com.stylemodel.utils.DoCacheUtils;

/**
 * Created by Administrator on 2016/8/26.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(AppApplication appApplication);

    /**
     * 获得Application.
     *
     * @return Application
     */
    @ContextLife
    Application application();

    SharedPreferences sp();

    ApiService api();

    OkHttpClient client();

    Realm mRealm();

    DoCacheUtils doCacheUtils();

    //Retrofit retrofit();




}
