package shinerich.com.stylemodel.presentTest;

import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;

import dagger.Component;
import shinerich.com.stylemodel.inject.component.ActivityComponent;
import shinerich.com.stylemodel.inject.component.AppComponent;
import shinerich.com.stylemodel.inject.component.DaggerActivityComponent;
import shinerich.com.stylemodel.inject.component.DaggerAppComponent;
import shinerich.com.stylemodel.inject.module.ActivityModuel;
import shinerich.com.stylemodel.inject.module.AppModule;
import shinerich.com.stylemodel.ui.main.activity.MainAcitivity;

import static org.mockito.Mockito.spy;

/**
 * Created by Administrator on 2017/2/7.
 */

public class TestUtils {

    public static final AppModule appModuel = spy(new AppModule(RuntimeEnvironment.application));

    public static void setUpDagger() {
        AppComponent appComponent = DaggerAppComponent.builder().appModule(appModuel).build();
        ComponentHolder.setAppComponent(appComponent);

//        MainAcitivity loginActivity = Robolectric.setupActivity(MainAcitivity.class);
//        ActivityComponent activityComponent = DaggerActivityComponent.builder().
//                appComponent(appComponent).activityModuel(new ActivityModuel(loginActivity)).build();
//        ActivityComponentHolder.setActivityComponent(activityComponent);

    }


}
