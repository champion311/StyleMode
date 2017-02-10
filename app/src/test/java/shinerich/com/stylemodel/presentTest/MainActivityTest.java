package shinerich.com.stylemodel.presentTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import shinerich.com.stylemodel.BuildConfig;
import shinerich.com.stylemodel.RobolectricTest;
import shinerich.com.stylemodel.ui.main.activity.MainAcitivity;

/**
 * Created by Administrator on 2017/2/7.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MainActivityTest {



    @Test
    public void testActivityStart() {
        MainAcitivity mainAcitivity= Robolectric.setupActivity(MainAcitivity.class);
        mainAcitivity.showError();

    }


}
