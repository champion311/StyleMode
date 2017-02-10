package shinerich.com.stylemodel.ui.mine.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import shinerich.com.stylemodel.ui.mine.fragment.MyMessageCommentFragment;
import shinerich.com.stylemodel.ui.mine.fragment.MyMessageReplayFragment;
import shinerich.com.stylemodel.ui.mine.fragment.MySystemMessageFragment;

/***
 * 我的消息-PageTabAdapter
 *
 * @author hunk
 */
public class MessagePageTabAdapter extends FragmentPagerAdapter {


    private String[] mTitles;


    public MessagePageTabAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.mTitles = titles;

    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        int index = position % mTitles.length;
        switch (position) {
            case 0:
                fragment = new MyMessageReplayFragment();
                break;
            case 1:
                fragment = new MyMessageCommentFragment();
                break;
            case 2:
                fragment = new MySystemMessageFragment();
                break;
            default:
                fragment = new MyMessageReplayFragment();
                break;

        }
        Bundle bundle = new Bundle();
        bundle.putString("title", mTitles[0]);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position % mTitles.length];
    }
}
