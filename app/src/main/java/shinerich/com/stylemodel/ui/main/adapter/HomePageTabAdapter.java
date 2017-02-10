package shinerich.com.stylemodel.ui.main.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shinerich.com.stylemodel.bean.ColumnItem;
import shinerich.com.stylemodel.constant.AppConstants;
import shinerich.com.stylemodel.ui.main.fragment.CommonInformationFragment;

/**
 * Created by Administrator on 2016/9/8.
 */
public class HomePageTabAdapter extends FragmentStatePagerAdapter {


    public static String TAG = "HomePageTabAdapter";


    //private List<Fragment> mFragments = new ArrayList<>();
    private List<ColumnItem> mFragmentTitles;
    //1推荐 2时装 3生活家4美妆 5视频 6嗨国 7博主

    private List<Fragment> mFragments;

    public HomePageTabAdapter(FragmentManager fm, List<ColumnItem> mFragmentTitles) {
        super(fm);
        this.mFragmentTitles = mFragmentTitles;
        mFragments = new ArrayList<>();
        for (ColumnItem item : mFragmentTitles) {
            CommonInformationFragment fragment = new CommonInformationFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("ColumnItem", item);
            fragment.setArguments(bundle);
            mFragments.add(fragment);
        }

    }


    @Override
    public Fragment getItem(int position) {

//        ColumnItem item = mFragmentTitles.get(position);
//        CommonInformationFragment fragment = new CommonInformationFragment();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("ColumnItem", item);
//        fragment.setArguments(bundle);
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles.get(position).getName();
    }


    private List<String> preIds = new ArrayList<>();


    Map<String, Integer> idsMaps = new HashMap<>();

    @Override
    public void notifyDataSetChanged() {

        for (ColumnItem items : mFragmentTitles) {
            idsMaps.put(items.getName(), items.getId());
        }
        preIds.clear();
        int size = getCount();
        for (int i = 0; i < size; i++) {
            preIds.add((String) getPageTitle(i));
        }
        super.notifyDataSetChanged();

    }

    @Override
    public int getItemPosition(Object object) {
        CommonInformationFragment fragment = (CommonInformationFragment) object;
        String title = fragment.getTitle();
        int preId = preIds.indexOf(title);
        int newId = -1;
        int size = getCount();
        int i = 0;
        for (; i < size; i++) {
            if (getPageTitle(i).equals(fragment.getTitle())) {
                newId = i;
                break;
            }
        }
        if (newId != -1 && newId == preId) {
            //没有变换
            Log.i(TAG, "title=" + title + " POSITION_UNCHANGED");
            return POSITION_UNCHANGED;
        }
        if (newId != -1) {
            Log.i(TAG, "title=" + title + "newId=" + newId);
            return newId;
        }
        Log.i(TAG, "title=" + title + " POSITION_NONE");
        return POSITION_NONE;

    }

//    @Override
//    public long getItemId(int position) {
//        return idsMaps.get(getPageTitle(position));
//    }


}
