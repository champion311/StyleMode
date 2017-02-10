package shinerich.com.stylemodel.ui.main.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.base.BaseActivity;
import shinerich.com.stylemodel.bean.ListByCateBean;
import shinerich.com.stylemodel.bean.ListContentBean;
import shinerich.com.stylemodel.bean.db.CateDataBean;
import shinerich.com.stylemodel.inject.component.ActivityComponent;
import shinerich.com.stylemodel.presenter.ColumnActivityPresenter;
import shinerich.com.stylemodel.presenter.contract.ColumnActivityContract;
import shinerich.com.stylemodel.ui.login.LoginSelectActivity;
import shinerich.com.stylemodel.ui.main.adapter.ColumnAdapter;
import shinerich.com.stylemodel.utils.ThemeHelper;
import shinerich.com.stylemodel.widget.AppBarStateChangeListener;
import shinerich.com.stylemodel.widget.CircleImageView;

/**
 * Created by Administrator on 2016/10/24.
 */
public class ColumnActivity extends
        BaseActivity<ColumnActivityPresenter> implements
        ColumnActivityContract.View, XRecyclerView.LoadingListener, ColumnAdapter.ItemClickListener {


    @BindView(R.id.view_toolbar)
    Toolbar viewToolbar;
    @BindView(R.id.clp_toolbar)
    CollapsingToolbarLayout clpToolbar;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.mRecyclerView)
    XRecyclerView mRecyclerView;
    @BindView(R.id.sub_icon)
    TextView subIcon;
    @BindView(R.id.mTitle)
    TextView mTilte;


    @BindView(R.id.mImageBg)
    ImageView mImageBg;
    @BindView(R.id.mHead)
    CircleImageView mHead;
    @BindView(R.id.mEnName)
    TextView mEnName;
    @BindView(R.id.back_icon)
    ImageView backIcon;

    private ColumnAdapter columnAdapter;

    private List<ListContentBean> list;

    private int currentPage = 1;

    private String cate_id;

    private String subId;

    private boolean isSub = false;

    //栏目页面


    @Override
    protected int getContentViewID() {
        return R.layout.activity_blogger;
    }


    protected void injectDaggger(ActivityComponent activityComponent) {
        activityComponent.inject(this);

    }

    @Override
    protected void initViewAndEvents() {
        if (!ThemeHelper.isNightMode(this)) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        clpToolbar.setCollapsedTitleTextColor(Color.BLACK);
        clpToolbar.setTitle("");
        setToolBar(viewToolbar);
        appBar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @SuppressLint("NewApi")
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, int state) {
                switch (state) {
                    case EXPANDED:
                        //扩张
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                            getWindow().setStatusBarColor(Color.TRANSPARENT);
//                        }
                        setSystemBarColorRes(R.color.transparent);
                        mTilte.setVisibility(View.GONE);
                        viewToolbar.setNavigationIcon(R.drawable.left_back);
                        viewToolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
                        break;
                    case COLLAPSED:
                        //折叠
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                            getWindow().setStatusBarColor(Color.WHITE);
//                        }
                        setSystemBarColorRes(R.color.theme_color_primary_trans);
                        mTilte.setVisibility(View.VISIBLE);
                        viewToolbar.setNavigationIcon(R.drawable.left_back_);
                        viewToolbar.setBackgroundResource(R.color.theme_color_primary_trans);
                        break;
                    case IDLE:
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                            getWindow().setStatusBarColor(Color.WHITE);
//                        }
                        setSystemBarColorRes(R.color.transparent);
                        viewToolbar.setNavigationIcon(R.drawable.left_back);
                        mTilte.setVisibility(View.GONE);
                        viewToolbar.setBackgroundColor(getResources().getColor(R.color.transparent));

                        break;
                    default:
                        break;

                }
            }
        });
        cate_id = getIntent().getStringExtra("cate_id");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.offsetChildrenHorizontal(10);
        gridLayoutManager.offsetChildrenVertical(5);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        list = new ArrayList<>();
        columnAdapter = new ColumnAdapter(this, list);
        columnAdapter.setListener(this);
        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setAdapter(columnAdapter);
        mRecyclerView.setLoadingListener(this);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mPresenter.loadData(cate_id, 1);

    }

    @Override
    public void showError() {

    }

    public void setToolBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.left_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @OnClick({R.id.sub_icon, R.id.back_icon})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.sub_icon:
                if (!isSub) {
                    mPresenter.subscribeAction(subId, "2");
                } else {
                    mPresenter.cancelSubAction(subId, "2");
                }
                break;
            case R.id.back_icon:
                finish();
                break;
        }

    }

    @Override
    public void showData(ListByCateBean data, int page) {
        if (page == 1) {
            mRecyclerView.refreshComplete();
            currentPage = 1;
            CateDataBean bean = data.getCate_data();
            list.clear();
            list.addAll(data.getList());
            columnAdapter.notifyDataSetChanged();
            mTilte.setText(bean.getName());
            mEnName.setText(bean.getName());
            if (data.getCate_data().getIs_sub().equals("0")) {
                this.isSub = false;
                //未订阅
                subIcon.setSelected(true);
                subIcon.setText("订阅");
                subIcon.setTextColor(getResources().getColor(R.color.white));
            } else {
                this.isSub = true;
                subIcon.setSelected(false);
                subIcon.setText("已订阅");
                subIcon.setTextColor(getResources().getColor(R.color.white));

            }
            subId = bean.getId();
            subIcon.setClickable(true);
            Glide.with(mContext).load(data.getCate_data().getAvatar_big()).
                    diskCacheStrategy(DiskCacheStrategy.SOURCE).
                    placeholder(R.drawable.ll_bg).error(R.drawable.ll_bg).into(mImageBg);
            Glide.with(mContext).load(data.getCate_data().getAvatar()).
                    diskCacheStrategy(DiskCacheStrategy.SOURCE).
                    placeholder(R.drawable.default_blogger_head).
                    error(R.drawable.default_blogger_head)
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(
                                GlideDrawable resource,
                                GlideAnimation<? super GlideDrawable> glideAnimation) {
                            mHead.setImageDrawable(resource);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                        }
                    });

        } else {
            currentPage += 1;
            list.addAll(data.getList());
            columnAdapter.notifyDataSetChanged();
            mRecyclerView.loadMoreComplete();
        }
    }

    @Override
    public void selectorAction(boolean isSub) {
        if (isSub) {
            subIcon.setSelected(false);
            subIcon.setText("已订阅");
            subIcon.setTextColor(getResources().getColor(R.color.white));
            this.isSub = true;
        } else {
            subIcon.setSelected(true);
            //subIcon.setClickable(false);
            subIcon.setText("未订阅");
            subIcon.setTextColor(getResources().getColor(R.color.white));
            this.isSub = false;
        }

    }

//    /**
//     * 添加订阅成功
//     */
//    @Override
//    public void addSubSuccess() {
//
//
//    }

    @Override
    public void notLoginAction() {
        Intent intent = new Intent(this, LoginSelectActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    @Override
    public void onRefresh() {
        mPresenter.loadData(cate_id, 1);
    }

    @Override
    public void onLoadMore() {
        mPresenter.loadData(cate_id, currentPage + 1);
    }

    @Override
    public void Click(View view, int position, ListContentBean bean) {
        if (bean != null) {

            int type = bean.getType();
            if (type == 0 || type == 3) {
                Intent intent = new Intent(this, ArticleContentActivity.class);
                intent.putExtra("id", String.valueOf(bean.getId()));
                intent.putExtra("type", String.valueOf(bean.getType()));
                startActivity(intent);
            } else if (type == 1) {
                Intent intent = new Intent(this, ImageListActivity.class);
                intent.putExtra("id", String.valueOf(bean.getId()));
                intent.putExtra("type", String.valueOf(bean.getType()));
                startActivity(intent);
            }
        }
    }
}


