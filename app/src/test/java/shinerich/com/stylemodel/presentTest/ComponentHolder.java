package shinerich.com.stylemodel.presentTest;

import shinerich.com.stylemodel.inject.component.AppComponent;

/**
 * Created by Administrator on 2017/2/7.
 */

public class ComponentHolder {

    private static AppComponent sAppComponent;

    public static void setAppComponent(AppComponent appComponent) {
        sAppComponent = appComponent;
    }

    public static AppComponent getAppComponent() {
        return sAppComponent;
    }

}
