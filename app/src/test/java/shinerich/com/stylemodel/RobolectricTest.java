package shinerich.com.stylemodel;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import shinerich.com.stylemodel.presentTest.*;
import shinerich.com.stylemodel.ui.main.activity.MainAcitivity;

/**
 * Created by Administrator on 2017/2/6.
 */


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = Config.NONE)

public class RobolectricTest {

    private MainAcitivity mainAcitivity;

    @Before
    public void befozeTest() throws Exception {
        shinerich.com.stylemodel.presentTest.TestUtils.setUpDagger();
        System.out.println("before");
    }

    @Test
    public void mainContentTest() throws Exception {
        System.out.println("now");
    }



}
