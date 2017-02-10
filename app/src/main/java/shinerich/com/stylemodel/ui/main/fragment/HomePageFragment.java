package shinerich.com.stylemodel.ui.main.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EdgeEffect;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.base.BaseFragment;
import shinerich.com.stylemodel.bean.ColumnItem;
import shinerich.com.stylemodel.bean.OpenDrawerEvent;
import shinerich.com.stylemodel.bean.UserColumn;
import shinerich.com.stylemodel.presenter.HomePagePresenter;
import shinerich.com.stylemodel.presenter.contract.HomePageContract;
import shinerich.com.stylemodel.ui.main.adapter.HomePageTabAdapter;
import shinerich.com.stylemodel.utils.DensityUtils;
import shinerich.com.stylemodel.utils.RxBus;
import shinerich.com.stylemodel.utils.ThemeHelper;

/**
 * Created by Administrator on 2016/9/8.
 */
public class HomePageFragment extends BaseFragment<HomePagePresenter> implements
        HomePageContract.View, TabLayout.OnTabSelectedListener {


    //public String[] tabData = {"推荐", "时装", "生活家", "美妆", "视频"};

    public int currentTopTabPos = 0;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.menu_button)
    ImageView menuButton;
    @BindView(R.id.tab_wrapper)
    LinearLayout tabWrapper;
    @BindView(R.id.content_wrapper)
    ViewPager contentWrapper;
//    @BindView(R.id.mSwipeLayout)
//    SwipeRefreshLayout mSwipeLayout;

    private List<ColumnItem> columnItems = new ArrayList<>();

    private HomePageTabAdapter mainTabAdapter;

    private UserColumn userColumn;

    @Override
    protected void initInject() {
        if (getFragmentComponent() != null) {
            getFragmentComponent().inject(this);
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_homepage;
    }

    @Override
    protected void initEventAndData() {
//        mSwipeLayout.setEnabled(false);
//        mSwipeLayout.setColorSchemeColors(Color.GREEN);
//        mSwipeLayout.setOnRefreshListener(this);


    }


    @Override
    public void showError() {

    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        currentTopTabPos = tab.getPosition();

        contentWrapper.setCurrentItem(currentTopTabPos);

        View view = tab.getCustomView();
        if (view != null) {
            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setSelected(true);
            textView.setTextColor(getResources().getColor(R.color.home_page_selected_color));
            textView.setTextSize(17);
        }


    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        View view = tab.getCustomView();
        if (view != null) {
            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setSelected(false);
            textView.setTextColor(getResources().getColor(R.color.home_page_unselected_color));
            textView.setTextSize(15);

        }


    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    @Override
    public void showErrorMessage(String message) {

    }


    @Override
    public void showTopTabData(UserColumn userColumn) {
        this.userColumn = userColumn;
        if (columnItems.size() > 0) {
            try {
                columnItems.clear();
                columnItems.addAll(userColumn.getColumnItems());
                mainTabAdapter.notifyDataSetChanged();
                tabs.setupWithViewPager(contentWrapper);
                //tabs.setTabsFromPagerAdapter(mainTabAdapter);
                //tabs.setOnTabSelectedListener(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        columnItems.clear();
        columnItems.addAll(userColumn.getColumnItems());
        if (mainTabAdapter == null) {
            mainTabAdapter = new HomePageTabAdapter(getChildFragmentManager()
                    , columnItems);
        }
        mainTabAdapter.notifyDataSetChanged();
        contentWrapper.setAdapter(mainTabAdapter);
        contentWrapper.setOffscreenPageLimit(3);
        tabs.setupWithViewPager(contentWrapper);
        //tabs.setTabsFromPagerAdapter(mainTabAdapter);
        tabs.setOnTabSelectedListener(this);


        for (int i = 0; i < mainTabAdapter.getCount(); i++) {
            TabLayout.Tab tab = tabs.getTabAt(i);
            tab.setCustomView(R.layout.item_selected_tab);
            if (i == 0) {
                TextView textView = (TextView)
                        tab.getCustomView().findViewById(android.R.id.text1);
                textView.setSelected(true);
                textView.setTextColor(getResources().getColor(R.color.home_page_selected_color));
                textView.setTextSize(17);
            }
        }
    }

    @Override
    public void reStartAction() {
        //#23282d
        //#454547

        for (int i = 0; i < mainTabAdapter.getCount(); i++) {
            TabLayout.Tab tab = tabs.getTabAt(i);
            TextView textView = (TextView)
                    tab.getCustomView().findViewById(android.R.id.text1);
            if (textView.isSelected()) {
                textView.setSelected(true);
                if (ThemeHelper.isNightMode(getActivity())) {
                    textView.setTextColor(Color.parseColor("#b2b2b2"));
                } else {
                    textView.setTextColor(Color.parseColor("#e0e0e0"));
                }
                textView.setTextSize(17);
            }

            if (ThemeHelper.isNightMode(getActivity())) {
                textView.setBackgroundColor(Color.parseColor("#454547"));
            } else {
                textView.setBackgroundColor(Color.parseColor("#23282d"));
            }
        }
        //手动处理部分
        if (ThemeHelper.isNightMode(getActivity())) {
            tabs.setBackgroundColor(Color.parseColor("#454547"));
            if (Build.VERSION.SDK_INT <= 19) {

                tabWrapper.setBackgroundColor(Color.parseColor("#454547"));
            }

        } else {
            tabs.setBackgroundColor(Color.parseColor("#23282d"));
            if (Build.VERSION.SDK_INT <= 19) {

                tabWrapper.setBackgroundColor(Color.parseColor("#23282d"));
            }
        }

    }


    @OnClick({R.id.menu_button})
    public void OnButtonClick(View view) {
        switch (view.getId()) {
            case R.id.menu_button:

                OpenDrawerEvent event = new OpenDrawerEvent();
                event.setOpenDrawer(true);
                RxBus.getInstance().post(event);
        }
    }


}
