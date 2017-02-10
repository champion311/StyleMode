package shinerich.com.stylemodel.ui.main.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bilibili.magicasakura.widgets.TintRelativeLayout;
import com.jcodecraeer.xrecyclerview.ArrowRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.base.BaseFragment;
import shinerich.com.stylemodel.bean.ColumnItem;
import shinerich.com.stylemodel.bean.ContentIndexBean;
import shinerich.com.stylemodel.bean.FocusImageBean;
import shinerich.com.stylemodel.bean.ListContentBean;
import shinerich.com.stylemodel.bean.NightModeEvent;
import shinerich.com.stylemodel.presenter.InformationFragmentPresenter;
import shinerich.com.stylemodel.presenter.contract.InformationFragmentContract;
import shinerich.com.stylemodel.ui.main.activity.ArticleContentActivity;
import shinerich.com.stylemodel.ui.main.activity.ImageListActivity;
import shinerich.com.stylemodel.ui.main.adapter.CommonViewPageAdapter;
import shinerich.com.stylemodel.ui.main.adapter.HomePageRecommendAdapter;
import shinerich.com.stylemodel.utils.CollectionsUtils;
import shinerich.com.stylemodel.utils.DensityUtils;
import shinerich.com.stylemodel.utils.ThemeHelper;
import shinerich.com.stylemodel.utils.ToastUtils;
import shinerich.com.stylemodel.widget.MXRefreshView;

/**
 * Created by Administrator on 2016/10/8.
 */
public class CommonInformationFragment extends
        BaseFragment<InformationFragmentPresenter>
        implements InformationFragmentContract.View,
        MXRefreshView.LoadingListener,
        ViewPager.OnPageChangeListener, Runnable, HomePageRecommendAdapter.CommonInfoInterface {


    @BindView(R.id.mRecyclerView)
    MXRefreshView mRecyclerView;
    @BindView(R.id.new_error_icon)
    ImageView newErrorIcon;
    @BindView(R.id.new_error_text)
    TextView newErrorText;
    @BindView(R.id.refresh_button)
    TextView refreshButton;
    @BindView(R.id.empty_view)
    RelativeLayout emptyView;


    //@BindView(R.id.M)
    ViewPager mViewPager;
    //@BindView(R.id.currentPage)
    TextView currentPageView;
    //@BindView(R.id.mContainer)
    FrameLayout mContainer;
    @BindView(R.id.mParentView)
    TintRelativeLayout mParentView;

    private LinearLayout mIconsWrapper;//dot父布局

    private ImageView[] dots;

    private View headerView;

    private int typeId = 0;

    private String title;

    private int currentPage = 1;

    // ViewPager 当前相对位置
    private int currIndex = 0;
    // ViewPager 的绝对位置
    private int absPosition = 0;

    private List<ListContentBean> data = new ArrayList<>();


    private boolean isLoadSuccess = false;
    private int size = 0;

    private HomePageRecommendAdapter recyAdapter;

    CommonViewPageAdapter pageAdapter;

    public String getTitle() {
        return title;
    }

    private boolean firstLoaded = true;

    private ScheduledExecutorService scheduledExecutorService;

    private boolean isVisibleToUser = false;


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (isVisibleToUser && size > 0) {
                        mViewPager.setCurrentItem(absPosition);
                        currIndex = absPosition % size;
                        setCurDot(currIndex);
                    }
                    break;
            }
        }
    };


    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_common_info;
    }

    @Override
    protected void initEventAndData() {
        if (getArguments() != null) {
            ColumnItem item = (ColumnItem) getArguments().getSerializable("ColumnItem");
            if (item != null) {
                typeId = item.getId();
                title = item.getName();
            }
        }
        initHeader();
        recyAdapter = new HomePageRecommendAdapter(getActivity(), data);
        recyAdapter.setmInterface(this);
        recyAdapter.setFragment(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(recyAdapter);
        mRecyclerView.setEmptyView(View.inflate(getContext(), R.layout.item_empty_view, null));
        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setLoadingListener(this);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mPresenter.loadData(typeId, currentPage);
        //mRecyclerView.addFootView();
        getPrivateValue(false);
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    public void initHeader() {
        headerView = View.inflate(getActivity(), R.layout.item_refresh_header, null);
        mViewPager = ButterKnife.findById(headerView, R.id.mViewPager);
        currentPageView = ButterKnife.findById(headerView, R.id.currentPage);
        mContainer = ButterKnife.findById(headerView, R.id.mContainer);
        mIconsWrapper = ButterKnife.findById(headerView, R.id.mIconsWrapper);
        mRecyclerView.addHeaderView(headerView);

    }

    @Override
    public void showError() {

    }

    @Override
    public void showNetErrorView(int page) {
        if (page == 1) {
            mRecyclerView.refreshComplete();
        } else {
            mRecyclerView.loadMoreComplete();
        }
        emptyView.setVisibility(View.VISIBLE);
        newErrorText.setText("未知错误");

    }

    @Override
    public void refreshView(ContentIndexBean bean, int page) {
        if (page == 1) {
            initDot(bean.getFocus());
            currentPage = 1;
            isLoadSuccess = true;
            if (bean.getFocus().size() == 0) {
                mContainer.setVisibility(View.INVISIBLE);
            }
            data.clear();
            data.addAll(bean.getList());
            recyAdapter.notifyDataSetChanged();
            mRecyclerView.refreshComplete();
        } else {
            data.addAll(bean.getList());
            recyAdapter.notifyDataSetChanged();
            mRecyclerView.loadMoreComplete();
            currentPage += 1;
        }
    }

    //初始化轮播点
    public void initDot(List<FocusImageBean> beans) {
        if (CollectionsUtils.isEmpty(beans)) {
            return;
        }
        this.size = beans.size();
        pageAdapter = new
                CommonViewPageAdapter(getActivity(), beans);
        mViewPager.setAdapter(pageAdapter);
        mViewPager.addOnPageChangeListener(this);
//        if (size > 0) {
//            currentPageView.setText("1" + "/" + pageAdapter.getCount());
//        } else {
//            currentPageView.setVisibility(View.GONE);
//        }
        absPosition = 1000 * beans.size();
        mViewPager.setCurrentItem(absPosition);
        mIconsWrapper.removeAllViews();
        dots = new ImageView[size];
        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setClickable(false);
            imageView.setImageResource(R.drawable.dots);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i != 0) {
                params.leftMargin = DensityUtils.dip2px(getActivity(), 10);
            }
            mIconsWrapper.addView(imageView, params);
            dots[i] = imageView;
            dots[i].setSelected(false);
            //dots[i].setTag(i);
        }
        currIndex = 0;
        dots[0].setSelected(true);
        scheduledExecutorService.scheduleAtFixedRate(this, 1, 6, TimeUnit.SECONDS);
    }

    /**
     * 相对位置
     *
     * @param position
     */
    public void setCurDot(int position) {
//        if (currIndex == position) {
//            return;
//        }
        if (position < 0 || position > size - 1) {
            return;
        }
        for (int i = 0; i < dots.length; i++) {
            if (i == position) {
                dots[position].setSelected(true);
            } else {
                dots[i].setSelected(false);
            }
        }
        currIndex = position;
    }

    @Override
    public void run() {
        if (!isDragging) {
            absPosition += 1;
            Message msg = handler.obtainMessage();
            msg.what = 1;
            msg.sendToTarget();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        if (scheduledExecutorService != null)
//            scheduledExecutorService.shutdown();

    }

    @OnClick({R.id.refresh_button})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.refresh_button:
                mPresenter.loadData(typeId, currentPage);
                break;

        }
    }


    @Override
    public void onRefresh() {
        mPresenter.loadData(typeId, 1);
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.loadData(typeId, currentPage + 1);
            }
        }, 1 * 1000);

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        absPosition = position;
        // currIndex = ;
        if (size > 0) {
            setCurDot(absPosition % size);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (pageAdapter != null)
            currentPageView.setText(String.valueOf(position + 1) + "/" + size);

        for (int i = 0; i < mIconsWrapper.getChildCount(); i++) {
            if (i == position) {
                mIconsWrapper.getChildAt(i).setSelected(true);
            } else {
                mIconsWrapper.getChildAt(i).setSelected(false);
            }
        }

    }

    private boolean isDragging = false;


    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            isDragging = false;
        } else if (state == ViewPager.SCROLL_STATE_DRAGGING) {
            isDragging = true;
        }

    }


    @Override
    public void nightEvent(NightModeEvent event) {
        if (Build.VERSION.SDK_INT > 19) {
            return;
        }
        if (ThemeHelper.isNightMode(getActivity())) {
            mParentView.setBackgroundColor(Color.parseColor("#454547"));
            getPrivateValue(true);
        } else {
            mParentView.setBackgroundColor(Color.WHITE);
            getPrivateValue(false);
        }
        //resetHeadView();
    }

    public void getPrivateValue(boolean isNightMode) {
        try {
            Field fieldArrowHeader = MXRefreshView.class.getDeclaredField("mRefreshHeader");
            fieldArrowHeader.setAccessible(true);
            Object object = fieldArrowHeader.get(mRecyclerView);
            //ToastUtils.show(getActivity(), object.toString());
            if (object != null) {
                ArrowRefreshHeader refreshHeader = (ArrowRefreshHeader) object;
                Field fieldTextView = ArrowRefreshHeader.class.getDeclaredField("mStatusTextView");
                fieldTextView.setAccessible(true);
                Object textObj = fieldTextView.get(refreshHeader);
                if (textObj != null) {
                    TextView textView = (TextView) textObj;
                    if (isNightMode) {
                        textView.setTextColor(Color.WHITE);
                    } else {
                        textView.setTextColor(Color.parseColor("#23282d"));
                    }
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (firstLoaded) {
            firstLoaded = false;
            return;
        }
        recyAdapter = new HomePageRecommendAdapter(getActivity(), data);
        mRecyclerView.setAdapter(recyAdapter);

    }

    public void resetHeadView() {
        try {
            Field fieldMheaderView = XRecyclerView.class.getDeclaredField("mHeaderViews");
            fieldMheaderView.setAccessible(true);
            Object headObJ = fieldMheaderView.get(mRecyclerView);
            //ToastUtils.show(getActivity(), object.toString());
            Field fieldWrapperAdapter = XRecyclerView.class.getDeclaredField("mWrapAdapter");
            fieldWrapperAdapter.setAccessible(true);
            Object wrapperObj = fieldWrapperAdapter.get(mRecyclerView);

            if (headObJ != null) {
                ArrayList<View> headViews = (ArrayList<View>) headObJ;
                headViews.clear();
                RecyclerView.Adapter adapter = (RecyclerView.Adapter) wrapperObj;
                adapter.notifyDataSetChanged();

            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isLoadSuccess) {
            if (isVisibleToUser) {
                Log.i("CommonInformationFragment", getTitle());
            } else {
                Log.i("CommonInformationFragment", getTitle() + "dismiss");
            }
        }
        this.isVisibleToUser = isVisibleToUser;
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void infoClick(ListContentBean bean, int position) {
        int type = bean.getType();
        if (type != 1) {
            Intent intent = new Intent(mContext, ArticleContentActivity.class);
            intent.putExtra("id", bean.getId());
            intent.putExtra("type", String.valueOf(bean.getType()));
            mContext.startActivity(intent);
        } else if (type == 1) {
            Intent intent = new Intent(mContext, ImageListActivity.class);
            intent.putExtra("id", bean.getId());
            intent.putExtra("type", String.valueOf(bean.getType()));
            mContext.startActivity(intent);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
    }
}
