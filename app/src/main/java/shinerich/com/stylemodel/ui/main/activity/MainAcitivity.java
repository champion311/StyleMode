package shinerich.com.stylemodel.ui.main.activity;


import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bilibili.magicasakura.utils.ThemeUtils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.base.BaseActivity;
import shinerich.com.stylemodel.bean.ColumnItem;
import shinerich.com.stylemodel.bean.UpdateBean;
import shinerich.com.stylemodel.bean.UserColumn;
import shinerich.com.stylemodel.inject.component.ActivityComponent;
import shinerich.com.stylemodel.presenter.MainPresenter;
import shinerich.com.stylemodel.presenter.contract.MainContract;
import shinerich.com.stylemodel.ui.discovery.fragment.DiscoveryFragment;
import shinerich.com.stylemodel.ui.main.adapter.DragAdapter;
import shinerich.com.stylemodel.ui.main.fragment.HomePageFragment;
import shinerich.com.stylemodel.ui.mine.dialog.VersionUpdateDialog;
import shinerich.com.stylemodel.ui.mine.fragment.MineFragment;
import shinerich.com.stylemodel.ui.subscription.fragment.SubscriptionFragment;
import shinerich.com.stylemodel.utils.ApplicationUtils;
import shinerich.com.stylemodel.utils.CollectionsUtils;
import shinerich.com.stylemodel.utils.RxBus;
import shinerich.com.stylemodel.utils.ThemeHelper;
import shinerich.com.stylemodel.utils.ToastUtils;
import shinerich.com.stylemodel.widget.DraggerView.ItemDragHelperCallBack;
import shinerich.com.stylemodel.widget.MySlidingUpPaneLayout;

/**
 * Created by Administrator on 2016/9/8.
 */
public class MainAcitivity extends BaseActivity<MainPresenter>
        implements MainContract.View,
        SlidingUpPanelLayout.PanelSlideListener,
        MySlidingUpPaneLayout.MyMenuDragUpListener, DragAdapter.MyColumnItemClick {


    public String[] tabData = {"推荐", "时装", "生活家", "美妆", "视频"};


    @BindView(R.id.home_page)
    TextView homePage;
    @BindView(R.id.subscribe)
    TextView subscribe;
    @BindView(R.id.discover)
    TextView discover;
    @BindView(R.id.mine)
    TextView mine;
    @BindView(R.id.mFragmentWrapper)
    FrameLayout mFragmentWrapper;


    public int currentBottomTabPos = 0;//下方tab位置

    //public int currentTopTabPos = 0;

    public ArrayList<TextView> bottomTab = new ArrayList<>();
    @BindView(R.id.bottomTabsWrapper)
    LinearLayout bottomTabsWrapper;
    @BindView(R.id.mColumn)
    TextView mColumn;
    @BindView(R.id.alert_text)
    TextView alertText;
    @BindView(R.id.mColumnRecy)
    RecyclerView mColumRecy;
    @BindView(R.id.certain_btn)
    TextView certainBtn;
    @BindView(R.id.cancel_button)
    RelativeLayout cancelButton;
    @BindView(R.id.mslideguppanel)
    MySlidingUpPaneLayout mslideguppanel;




    private HomePageFragment homePageFragment;

    private DiscoveryFragment discoveryFragment;

    private SubscriptionFragment subscriptionFragment;

    private MineFragment mineFragment;

    public static int showFragmentPos = 0;

    public static int hideFragmentPos = 0;


    private DragAdapter dragAdapter;
    private static boolean isFisrtOpen = true;
    private UserColumn userColumn;

    private VersionUpdateDialog updateDialog;


    /**
     * 版本更新
     */
    public void vesionUpdate() {
        String version = ApplicationUtils.getVersion(this);
        mPresenter.getVersionData(version);

    }

    @Override
    protected void initViewAndEvents() {
        certainBtn.setVisibility(View.GONE);
        bottomTab.add(homePage);
        bottomTab.add(subscribe);
        bottomTab.add(discover);
        bottomTab.add(mine);
        homePage.setSelected(true);
        //TODO 待修改
        mPresenter.getCacheTopData();
        //处理Fragment的事务
        //处理Fragment的事务
        homePageFragment = new HomePageFragment();
        discoveryFragment = new DiscoveryFragment();
        subscriptionFragment = new SubscriptionFragment();
        mineFragment = new MineFragment();
        //loadMultipleRootFragment(R.id.mFragmentWrapper, 0,
        // homePageFragment, discoveryFragment, subscriptionFragment, mineFragment);
//
//        showHideFragment(homePageFragment, subscriptionFragment);
        loadFragments();
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mslideguppanel.setEnAbleDrag(false);
                mslideguppanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                //返回之前的记录状态
                if (dragAdapter != null) {
                    if (!dragAdapter.isDataEqual(currentColumnData, dragAdapter.getmItems())) {
                        dragAdapter.setData(currentColumnData);
                        dragAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        vesionUpdate();

    }

    public void loadFragments() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mFragmentWrapper, subscriptionFragment);
        fragmentTransaction.add(R.id.mFragmentWrapper, discoveryFragment);
        fragmentTransaction.add(R.id.mFragmentWrapper, mineFragment);
        fragmentTransaction.add(R.id.mFragmentWrapper, homePageFragment);
        fragmentTransaction.hide(subscriptionFragment);
        fragmentTransaction.hide(discoveryFragment);
        fragmentTransaction.show(homePageFragment);
        fragmentTransaction.hide(mineFragment);
        fragmentTransaction.commit();


    }

    private boolean isClickAble = true;

    @OnClick({R.id.home_page, R.id.subscribe, R.id.discover, R.id.mine})
    public void OnBottomTabClick(View view) {
        if (view.isSelected()) {
            //当前已经是选中状态
            return;
        }
        if (!isClickAble) {
            return;
        }
        switch (view.getId()) {

            case R.id.home_page:
                changeTabState(0);
                showFragmentPos = ThemeHelper.TYPE_HOMEPAGE;
                break;
            case R.id.subscribe:
                changeTabState(1);
                showFragmentPos = ThemeHelper.TYPE_SUBSCRIPTION;
                break;
            case R.id.discover:
                changeTabState(2);
                showFragmentPos = ThemeHelper.TYPE_DISCOVERY;
                break;
            case R.id.mine:
                changeTabState(3);
                showFragmentPos = ThemeHelper.TYPE_MINE;
                break;
        }
        ThemeHelper.setCurrentFragmentPos(this, showFragmentPos);
        //showHideFragment(getTargetFragment(showFragmentPos), getTargetFragment(hideFragmentPos));
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(getTargetFragment(showFragmentPos));
        fragmentTransaction.hide(getTargetFragment(hideFragmentPos));
        fragmentTransaction.commit();
        hideFragmentPos = showFragmentPos;

    }


    public void changeTabState(int pos) {
        if (currentBottomTabPos == pos) {
            return;
        }
        bottomTab.get(currentBottomTabPos).setSelected(false);
        bottomTab.get(pos).setSelected(true);
        currentBottomTabPos = pos;
    }


    @Override
    protected int getContentViewID() {
        return R.layout.activity_main_text;
    }

    @Override
    protected void injectDaggger(ActivityComponent activityComponent) {
        getActivityComponent().inject(this);
    }


    @Override
    public void showError() {
        //TODO 网络连接出错等

    }


    @Override
    public void showErrorMessage(String message) {

    }


    private List<ColumnItem> currentColumnData = new ArrayList<>();

    /**
     * 处理我的栏目数据
     *
     * @param userColumn
     */
    @Override
    public void showListOfTopTab(UserColumn userColumn) {
        //初始化数据
        this.userColumn = userColumn;
        mslideguppanel.addPanelSlideListener(this);
        //初始化上拉菜单，触发按钮
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        mColumRecy.setLayoutManager(manager);
        ItemDragHelperCallBack callBack = new ItemDragHelperCallBack();
        ItemTouchHelper helper = new ItemTouchHelper(callBack);
        helper.attachToRecyclerView(mColumRecy);
        //上拉菜单
        dragAdapter = new DragAdapter(this, userColumn.getColumnItems(), helper);
        dragAdapter.setMyColumnItemClick(this);
        mColumRecy.setAdapter(dragAdapter);
        //发送给HomePageFragment
        currentColumnData.clear();
        currentColumnData.addAll(userColumn.getColumnItems());
        RxBus.getInstance().post(userColumn);


    }


    private Fragment getTargetFragment(int item) {
        Fragment supportFragment = null;
        switch (item) {
            case 0:
                supportFragment = homePageFragment;
                break;
            case 1:
                if (subscriptionFragment == null) {
                    subscriptionFragment = new SubscriptionFragment();
                }
                supportFragment = subscriptionFragment;
                break;
            case 2:
                supportFragment = discoveryFragment;
                break;
            case 3:
                supportFragment = mineFragment;
                break;
            default:
                return homePageFragment;
        }
        return supportFragment;
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {


    }

    /**
     * 将我的栏目页面收缩
     *
     * @param panel         The child view that was slid to an collapsed position
     * @param previousState
     * @param newState
     */


    @Override
    public void onPanelStateChanged(View panel,
                                    SlidingUpPanelLayout.PanelState previousState,
                                    SlidingUpPanelLayout.PanelState newState) {
        Log.d(tag, newState.toString());
        if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            //上拉菜单已经收缩
            mslideguppanel.setEnAbleDrag(false);
            //重置上拉菜单状态
            certainBtn.setVisibility(View.GONE);
            dragAdapter.setEditMode(false);
            endAnimation();
        }

    }

    //手动滑动上啦窗口监听
    @Override
    public void call() {
        mslideguppanel.setEnAbleDrag(false);
    }

    @Override
    public void showTopMenu(boolean flag) {
        //显示上拉菜单
        mslideguppanel.setEnAbleDrag(true);
        mslideguppanel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    /**
     * 夜间模式的返回
     */
    @Override
    public void reCreateAction() {

        final boolean isNight = ThemeHelper.isNightMode(this);
        if (!isNight) {
            ThemeUtils.updateNightMode(getResources(), true);
        } else {
            ThemeUtils.updateNightMode(getResources(), false);
        }
        ThemeHelper.setNightMode(this, !isNight);
        ThemeUtils.refreshUI(this, new ThemeUtils.ExtraRefreshable() {
            @Override
            public void refreshGlobal(Activity activity) {

            }

            @Override
            public void refreshSpecificView(View view) {

            }
        });
        startActivity(new Intent(this, TransitionActivity.class));

        if (Build.VERSION.SDK_INT <= 19) {
            //需要手动更新
            if (ThemeHelper.isNightMode(this)) {
                bottomTabsWrapper.setBackgroundColor(Color.parseColor("#454547"));

            } else {
                bottomTabsWrapper.setBackgroundColor(getResources().getColor(R.color.white));
            }
        }

        isClickAble = false;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isClickAble = true;
            }
        }, 500);


    }

    @Override
    public void showUpdateDialog(UpdateBean data) {
        if (data.getType() == 1) {
            if (updateDialog == null) {
                updateDialog = new VersionUpdateDialog(mContext);
                updateDialog.setUpdateBean(data);
                updateDialog.setCancel(false);
                updateDialog.show();
                updateDialog.hideCloseBtn();
            } else {
                updateDialog.show();
            }
        } else if (data.getType() == 2) {
            if (updateDialog == null) {
                updateDialog = new VersionUpdateDialog(mContext);
                updateDialog.setUpdateBean(data);
                updateDialog.show();
            } else {
                updateDialog.show();
            }
        }

    }


    @OnClick({R.id.certain_btn})
    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.certain_btn:
                //收缩我的栏目页面
                mslideguppanel.setEnAbleDrag(false);
                mslideguppanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                if (userColumn != null) {
                    mPresenter.saveCacheTopData(userColumn.getId(),
                            userColumn.getColumnItems());
                    RxBus.getInstance().post(userColumn);
                    //发送给HomePageFragment
                }
                currentColumnData.clear();
                currentColumnData.addAll(dragAdapter.getmItems());
                certainBtn.setVisibility(View.GONE);
                break;
        }
    }


    private List<ObjectAnimator> animators = new ArrayList<>();

    private void startShakeByPropertyAnim(View view, float scaleSmall,
                                          float scaleLarge, float shakeDegrees, long duration) {
        if (view == null) {
            return;
        }
        //TODO 验证参数的有效性

        //先变小后变大
        PropertyValuesHolder scaleXValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );
        PropertyValuesHolder scaleYValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );

        //先往左再往右
        PropertyValuesHolder rotateValuesHolder = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(0.1f, -shakeDegrees),
                Keyframe.ofFloat(0.2f, shakeDegrees),
                Keyframe.ofFloat(0.3f, -shakeDegrees),
                Keyframe.ofFloat(0.4f, shakeDegrees),
                Keyframe.ofFloat(0.5f, -shakeDegrees),
                Keyframe.ofFloat(0.6f, shakeDegrees),
                Keyframe.ofFloat(0.7f, -shakeDegrees),
                Keyframe.ofFloat(0.8f, shakeDegrees),
                Keyframe.ofFloat(0.9f, -shakeDegrees),
                Keyframe.ofFloat(1.0f, 0f)
        );

        ObjectAnimator objectAnimator = ObjectAnimator.
                ofPropertyValuesHolder(view, scaleXValuesHolder, scaleYValuesHolder, rotateValuesHolder);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
        objectAnimator.setRepeatMode(Animation.REVERSE);
        //无尽次重复播放
        objectAnimator.setRepeatCount(Animation.INFINITE);
        animators.add(objectAnimator);

    }

    @Override
    public void callAction() {
        //全部抖动
        certainBtn.setVisibility(View.VISIBLE);
        for (int i = 0; i < mColumRecy.getChildCount(); i++) {
            View view = mColumRecy.getChildAt(i);
            if (i != 0) {
                startShakeByPropertyAnim(view, 0.9f, 1.1f, 10f, 1000);
            }
            mslideguppanel.setEnAbleDrag(false);
        }
    }

    public void endAnimation() {
        if (CollectionsUtils.isEmpty(animators)) {
            return;
        }
        for (ObjectAnimator animator : animators) {
            animator.end();
        }
    }

    private long currentMills;

    @Override
    public void onBackPressed() {
        // 1.获取当前毫秒数
        long ct = System.currentTimeMillis();

        if (ct - currentMills <= 3000) {
            super.onBackPressed();
        } else {
            ToastUtils.show(this, "再次点击退出");
            currentMills = System.currentTimeMillis();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ToastUtils.cancel();
        //取消更新dialog
        if (updateDialog != null) {
            updateDialog.cancel();
        }
    }


}

