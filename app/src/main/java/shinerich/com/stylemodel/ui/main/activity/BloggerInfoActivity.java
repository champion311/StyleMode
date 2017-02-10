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
import shinerich.com.stylemodel.bean.BloggerDetailBean;
import shinerich.com.stylemodel.bean.BloggerIntroContentBean;
import shinerich.com.stylemodel.bean.ListContentBean;
import shinerich.com.stylemodel.inject.component.ActivityComponent;
import shinerich.com.stylemodel.presenter.BlogPresenter;
import shinerich.com.stylemodel.presenter.contract.BloggerInfoContract;
import shinerich.com.stylemodel.ui.login.LoginSelectActivity;
import shinerich.com.stylemodel.ui.main.adapter.ColumnAdapter;
import shinerich.com.stylemodel.utils.DensityUtils;
import shinerich.com.stylemodel.utils.ThemeHelper;
import shinerich.com.stylemodel.utils.ToastUtils;
import shinerich.com.stylemodel.widget.AppBarStateChangeListener;
import shinerich.com.stylemodel.widget.CircleImageView;

/**
 * Created by Administrator on 2016/9/14.
 */
public class BloggerInfoActivity extends BaseActivity<BlogPresenter>
        implements BloggerInfoContract.View,
        XRecyclerView.LoadingListener, ColumnAdapter.ItemClickListener {


    @BindView(R.id.view_toolbar)
    Toolbar viewToolbar;
    @BindView(R.id.clp_toolbar)
    CollapsingToolbarLayout clpToolbar;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.mRecyclerView)
    XRecyclerView mRecy;
    @BindView(R.id.mImageBg)
    ImageView mImageBg;
    @BindView(R.id.mHead)
    CircleImageView mHead;
    @BindView(R.id.mEnName)
    TextView mEnName;
    @BindView(R.id.sub_icon)
    TextView subIcon;
    @BindView(R.id.mTitle)
    TextView mTitle;


    private int bloggers_id;

    private boolean isSub = false;

    //博主编辑页面

    private List<ListContentBean> mList;

    private ColumnAdapter columnAdapter;

    private int currentPage = 1;

    private int type;//type==0为编辑 否则为博客主

    private BloggerDetailBean infoBean;

    @Override
    protected int getContentViewID() {
        return R.layout.activity_blogger;
    }

    @Override
    protected void injectDaggger(ActivityComponent activityComponent) {
        if (activityComponent != null) {
            activityComponent.inject(this);
        }

    }


    @Override
    protected void initViewAndEvents() {


        if (!ThemeHelper.isNightMode(this)) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        clpToolbar.setCollapsedTitleTextColor(Color.TRANSPARENT);
        setToolBar(viewToolbar);
        appBar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @SuppressLint("NewApi")
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, int state) {
                switch (state) {
                    case EXPANDED:
                        //扩张
                        //ToastUtils.show(BloggerInfoActivity.this, "top");
                        setSystemBarColorRes(R.color.transparent);
                        viewToolbar.setNavigationIcon(R.drawable.left_back);
                        mTitle.setVisibility(View.GONE);
                        viewToolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
                        break;
                    case COLLAPSED:
                        //折叠
                        if (!ThemeHelper.isNightMode(BloggerInfoActivity.this)) {
                            setSystemBarColorRes(R.color.white);
                        } else {
                            setSystemBarColorRes(R.color.theme_color_primary);
                        }
                        viewToolbar.setBackgroundResource(R.color.theme_color_primary_trans);
                        viewToolbar.setNavigationIcon(R.drawable.left_back_);
                        mTitle.setVisibility(View.VISIBLE);
                        break;
                    case IDLE:
                        //getWindow().setStatusBarColor(Color.WHITE);
                        setSystemBarColorRes(R.color.transparent);
                        viewToolbar.setNavigationIcon(R.drawable.left_back);
                        mTitle.setVisibility(View.GONE);
                        viewToolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
                        break;
                    default:
                        break;

                }
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.offsetChildrenHorizontal(10);
        gridLayoutManager.offsetChildrenVertical(5);
        mRecy.setLayoutManager(gridLayoutManager);
        mRecy.setPullRefreshEnabled(false);
        mRecy.setLoadingMoreEnabled(true);
        mRecy.setLoadingListener(this);
        mRecy.setEmptyView(View.inflate(mContext, R.layout.item_empty_view, null));
        mRecy.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mList = new ArrayList<>();
        columnAdapter = new ColumnAdapter(this, mList);
        columnAdapter.setListener(this);
        mRecy.setAdapter(columnAdapter);
        mRecy.setItemAnimator(null);
        if (getIntent() != null) {
            bloggers_id = getIntent().getIntExtra("blogger_id", -1);
            type = getIntent().getIntExtra("type", -1);
            if (bloggers_id != -1) {
                mPresenter.loadData(bloggers_id, 1, type);
            }
        }
    }

    @Override
    public void showError() {

    }

    public void setToolBar(Toolbar toolbar) {
        //toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //toolbar.setNavigationIcon(); 图标按钮
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewToolbar.setTitle("");

    }


    @Override
    public void showData(BloggerIntroContentBean data, int page) {
        if (page == 1) {
            mRecy.refreshComplete();
            currentPage = 1;

            if (data.getBlogger_data() != null) {
                infoBean = data.getBlogger_data();//博主
            } else {
                infoBean = data.getEditor_data();//编辑
            }
            if (infoBean == null) {
                return;
            }
            mList.clear();
            mList.addAll(data.getList());
            columnAdapter.notifyDataSetChanged();
            mEnName.setText(infoBean.getEname());
            if (infoBean.getIs_sub().equals("0")) {
                //未订阅
                isSub = false;
                subIcon.setSelected(true);
                subIcon.setText("订阅");
                subIcon.setTextColor(getResources().getColor(R.color.white));
            } else {
                isSub = true;
                subIcon.setSelected(false);
                subIcon.setText("已订阅");
                subIcon.setTextColor(getResources().getColor(R.color.white));

            }
            subIcon.setClickable(true);
            mTitle.setText(infoBean.getEname());
            Glide.with(mContext).load(infoBean.getAvatar_big()).
                    diskCacheStrategy(DiskCacheStrategy.SOURCE).
                    placeholder(R.drawable.ll_bg).error(R.drawable.ll_bg).into(mImageBg);
            Glide.with(mContext).load(infoBean.getAvatar()).
                    diskCacheStrategy(DiskCacheStrategy.SOURCE).
                    placeholder(R.drawable.default_blogger_head).
                    error(R.drawable.default_blogger_head).
                    into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            mHead.setImageDrawable(resource);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                        }
                    });

        } else {
            currentPage += 1;
            mList.addAll(data.getList());
            columnAdapter.notifyDataSetChanged();
            mRecy.loadMoreComplete();

        }
    }

    @Override
    public void selectorAction(boolean isSub) {
        if (isSub) {
            subIcon.setSelected(false);
            //subIcon.setClickable(false);
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


    @Override
    public void notLoginAction() {
        Intent intent = new Intent(this, LoginSelectActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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

    @Override
    public void onRefresh() {
        mPresenter.loadData(bloggers_id, 1, type);

    }

    @Override
    public void onLoadMore() {
        mPresenter.loadData(bloggers_id, currentPage + 1, type);
    }

    @OnClick({R.id.sub_icon})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.sub_icon:
                //"1"博客主
                if (infoBean == null) {
                    return;
                }
                if (!isSub) {
                    mPresenter.subscribeAction(String.valueOf(bloggers_id), "1");
                } else {
                    mPresenter.cancelSubAction(String.valueOf(bloggers_id), "1");
                }
                break;

        }
    }


}
