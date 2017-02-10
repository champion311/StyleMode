package shinerich.com.stylemodel.ui.main.activity;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.Window;

import shinerich.com.stylemodel.R;

/**
 * Created by Administrator on 2016/12/26.
 */

public class TransitionActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //透明过渡Activity
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_transition);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TransitionActivity.this.finish();
            }
        }, 50);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}
