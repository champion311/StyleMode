package shinerich.com.stylemodel.ui.subscription.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import shinerich.com.stylemodel.ui.subscription.fragment.HotBloggersFragment;
import shinerich.com.stylemodel.ui.subscription.fragment.MineSubFragment;
import shinerich.com.stylemodel.ui.subscription.fragment.RecommendFragment;
import shinerich.com.stylemodel.ui.subscription.fragment.SubscriptionFragment;

/**
 * Created by Administrator on 2016/10/14.
 */
public class SubArrangeFragmentAdapter
        extends FragmentPagerAdapter {

    public SubArrangeFragmentAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new RecommendFragment();
                break;
            case 1:
                fragment = new HotBloggersFragment();
                break;
            case 2:
                fragment = new MineSubFragment();
                break;
            default:
                fragment = new MineSubFragment();
                break;

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        String ret;
        switch (position) {
            case 0:
                ret = "推荐内容";
                break;
            case 1:
                ret = "热门博主";
                break;
            case 2:
                ret = "我的订阅";
                break;
            default:
                ret = "我的订阅";
                break;

        }
        return ret;
    }
}
