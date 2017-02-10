package shinerich.com.stylemodel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import shinerich.com.stylemodel.rule.ContextRule;
import shinerich.com.stylemodel.ui.main.activity.MainAcitivity;

import static org.junit.Assert.assertEquals;

/**
 * Created by Administrator on 2017/2/7.
 */

public class MainActivityTest {

    private MainAcitivity mainAcitivity;

    @Rule
    public ContextRule contextRule = new ContextRule();


    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }





}
