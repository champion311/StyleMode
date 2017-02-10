package shinerich.com.stylemodel.ui.subscription.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.base.SimpleActivity;
import shinerich.com.stylemodel.bean.NightModeEvent;
import shinerich.com.stylemodel.ui.subscription.adapter.SubArrangeFragmentAdapter;
import shinerich.com.stylemodel.utils.RxBus;
import shinerich.com.stylemodel.utils.RxUtils;
import shinerich.com.stylemodel.utils.ThemeHelper;

/**
 * Created by Administrator on 2016/10/13.
 */
public class SubscriptionArrangementActivity
        extends SimpleActivity implements TabLayout.OnTabSelectedListener {


    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;

    @Override
    protected int getLayoutId() {
        return R.layout.activitiy_sub_arragement;
    }


    @Override
    protected void initEventAndData() {


        SubArrangeFragmentAdapter adapter = new SubArrangeFragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(2);
        tabs.setupWithViewPager(mViewPager);
        tabs.setOnTabSelectedListener(this);

        RxBus.getInstance().toObservable(NightModeEvent.class).
                compose(RxUtils.<NightModeEvent>rxSchedulerHelp()).subscribe(new Action1<NightModeEvent>() {
            @Override
            public void call(NightModeEvent nightModeEvent) {
                if (nightModeEvent.isNightMode()) {
                    setSystemBarColorRes(R.color.transparent);
                } else {
                    setSystemBarColorRes(R.color.white);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
        if (ThemeHelper.isNightMode(this)) {
            setSystemBarColorRes(R.color.transparent);
        } else {
            setSystemBarColorRes(R.color.white);
        }


    }

    @OnClick(value = {R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition(), true);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
