package shinerich.com.stylemodel.inject.module;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import okhttp3.OkHttpClient;
import shinerich.com.stylemodel.api.ApiService;
import shinerich.com.stylemodel.inject.scope.ContextLife;
import shinerich.com.stylemodel.network.OkHttpManager;
import shinerich.com.stylemodel.network.RetrofitClient;
import shinerich.com.stylemodel.presenter.ArticleContentPresenter;
import shinerich.com.stylemodel.ui.main.activity.ArticleContentActivity;
import shinerich.com.stylemodel.utils.DoCacheUtils;

/**
 * Created by Administrator on 2016/8/26.
 */
@Module
public class AppModule {
    private final Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    @ContextLife
    Application provideApplicationContext() {
        return application;
    }


    @Provides
    @Singleton
    SharedPreferences provideSp() {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    ApiService provideApi() {
        return RetrofitClient.getInstance().getApiService();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return OkHttpManager.getOkHttpClient();
    }

    //默认realm
    @Provides
    @Singleton
    Realm provideRealmInstance() {
        RealmConfiguration config = new RealmConfiguration.
                Builder(application).name("user_column_db").deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);
        return Realm.getDefaultInstance();
    }

    @Provides
    @Singleton
    DoCacheUtils provideDocacheUtils() {
        return DoCacheUtils.get(application);
    }


//    @Provides
//    @Singleton
//    Retrofit provideRetrofit() {
//        return RetrofitClient.getInstance().;
//    }


//    @Provides
//    @Singleton
//    @Named("tma")
//    OkHttpClient provideOkHttpClient2(){
//        return OkHttpManager.getOkHttpClient();
//    }

    //TODO 添加APP全局内容


}
