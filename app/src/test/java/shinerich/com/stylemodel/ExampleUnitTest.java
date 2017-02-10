package shinerich.com.stylemodel;

import android.app.Activity;
import android.support.v4.view.ViewPager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import shinerich.com.stylemodel.api.ApiService;
import shinerich.com.stylemodel.bean.UserColumn;
import shinerich.com.stylemodel.inject.module.AppModule;
import shinerich.com.stylemodel.presenter.MainPresenter;
import shinerich.com.stylemodel.presenter.SubScriptionPresenter;
import shinerich.com.stylemodel.ui.main.activity.MainAcitivity;
import shinerich.com.stylemodel.utils.ApplicationUtils;
import shinerich.com.stylemodel.utils.HDateUtils;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {


    @Before
    public void beforeTest() {

    }


    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testData() {
        //HDateUtils hDateUtils = new HDateUtils();
        String testData = HDateUtils.getCurrentDateTime();
        assertEquals("2010-1-2", testData);
        //assertSame();
    }


    @Test
    public void testMainPresenter() {

        ApiService apiService = Mockito.mock(ApiService.class);
        Activity mActivity = Mockito.mock(Activity.class);
        MainPresenter mainPresenter = new MainPresenter(mActivity, apiService);
        mainPresenter.getDataFromWeb(0);
        Mockito.mock(ApiService.class);
        Mockito.verify(apiService).getNav();

        //Mockito.doAnswer();
        //SubScriptionPresenter mockPresent=mock


    }


}