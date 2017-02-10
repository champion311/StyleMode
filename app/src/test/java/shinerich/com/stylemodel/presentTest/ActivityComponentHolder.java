package shinerich.com.stylemodel.presentTest;

import shinerich.com.stylemodel.inject.component.ActivityComponent;

/**
 * Created by Administrator on 2017/2/7.
 */

public class ActivityComponentHolder {

    public static ActivityComponent activityComponent;

    public static ActivityComponent getActivityComponent() {
        return activityComponent;
    }

    public static void setActivityComponent(ActivityComponent activityComponent) {
        ActivityComponentHolder.activityComponent = activityComponent;
    }
}
