package shinerich.com.stylemodel.presentTest;

import android.app.Activity;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowTextView;

import io.realm.Realm;

import shinerich.com.stylemodel.BuildConfig;
import shinerich.com.stylemodel.ContextHolder;
import shinerich.com.stylemodel.api.ApiService;
import shinerich.com.stylemodel.inject.component.DaggerActivityComponent;
import shinerich.com.stylemodel.presenter.MainPresenter;

/**
 * Created by Administrator on 2017/2/7.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MainPresentTest {

    //public MainPresenter mainPresenter;

    @Before
    public void init() {
        TestUtils.setUpDagger();

    }

    @Test
    public void test() {
        Assert.assertEquals(2, 1 + 1);


    }


}
