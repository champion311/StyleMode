package shinerich.com.stylemodel.inject.module;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import shinerich.com.stylemodel.api.ApiService;
import shinerich.com.stylemodel.inject.scope.ActivityScope;
import shinerich.com.stylemodel.presenter.MainPresenter;

/**
 * Created by Administrator on 2016/8/30.
 */
@Module
public class ActivityModuel {

    private Activity mActivity;

    public ActivityModuel(Activity activity) {
        mActivity = activity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }

    public MainPresenter provideMainPresenter(Activity mActivity, Realm realm, ApiService apiService) {
        return new MainPresenter(realm, mActivity, apiService);
    }
}
