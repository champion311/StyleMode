package shinerich.com.stylemodel.inject.component;

/**
 * Created by Administrator on 2017/2/7.
 */

public class MainPresenterHolder {
    private static AppComponent sAppComponent;

    public static void setAppComponent(AppComponent appComponent) {
        sAppComponent = appComponent;
    }

    public static AppComponent getAppComponent() {
        return sAppComponent;
    }

}
