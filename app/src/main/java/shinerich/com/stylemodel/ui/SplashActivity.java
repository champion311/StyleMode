package shinerich.com.stylemodel.ui;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.base.SimpleActivity;
import shinerich.com.stylemodel.common.GloableValues;
import shinerich.com.stylemodel.utils.SharedUtils;


/**
 * 引导页
 *
 * @author hunk
 */
public class SplashActivity extends SimpleActivity implements View.OnClickListener {

    private boolean isFrist = false;
    private Button btn_go;
    @BindView(R.id.btn_right)
    Button btn_right;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    List<View> views;
    @BindView(R.id.img_dot_1)
    ImageView img_dot_1;
    @BindView(R.id.img_dot_2)
    ImageView img_dot_2;
    @BindView(R.id.img_dot_3)
    ImageView img_dot_3;
    @BindView(R.id.rl_item)
    RelativeLayout rl_item;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initEventAndData() {
        isFrist = SharedUtils.getBool(this, GloableValues.IS_FIRST_INSTALL,
                true);
        if (!isFrist) {
            startIntent(this, WelcomeActivity.class);
            finish();
        } else {
            //初始化数据
            initData();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //去广告页
            case R.id.btn_go:
            case R.id.btn_right:
                SharedUtils.save(SplashActivity.this,
                        GloableValues.IS_FIRST_INSTALL, false);
                Intent intent = new Intent(this,
                        WelcomeActivity.class);
                startActivity(intent);
                finish();

                break;


        }
    }

    private void initData() {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        views = new ArrayList<View>();
        View view1 = View.inflate(this, R.layout.item_splash_1, null);
        View view2 = View.inflate(this, R.layout.item_splash_2, null);
        View view3 = View.inflate(this, R.layout.item_splash_3, null);
        btn_go = (Button) view3.findViewById(R.id.btn_go);
        views.add(view1);
        views.add(view2);
        views.add(view3);
        viewPager.setAdapter(new MyViewPagerAdapter(views));
        viewPager.setCurrentItem(0);
        btn_go.setOnClickListener(this);
        btn_right.setOnClickListener(this);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        rl_item.setBackgroundResource(R.drawable.wel_pager_bg1);
                        img_dot_1.setImageResource(R.drawable.splash_sel);
                        img_dot_2.setImageResource(R.drawable.splash_no);
                        img_dot_3.setImageResource(R.drawable.splash_no);
                        break;

                    case 1:
                        rl_item.setBackgroundResource(R.drawable.wel_pager_bg2);
                        img_dot_1.setImageResource(R.drawable.splash_no);
                        img_dot_2.setImageResource(R.drawable.splash_sel);
                        img_dot_3.setImageResource(R.drawable.splash_no);
                        break;
                    case 2:
                        rl_item.setBackgroundResource(R.drawable.wel_pager_bg3);
                        img_dot_1.setImageResource(R.drawable.splash_no);
                        img_dot_2.setImageResource(R.drawable.splash_no);
                        img_dot_3.setImageResource(R.drawable.splash_sel);
                        break;


                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    /**
     * 滑动事件
     */
    private class MyViewPagerAdapter extends PagerAdapter {
        private List<View> mListViews;

        public MyViewPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mListViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mListViews.get(position), 0);
            return mListViews.get(position);
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }


}
