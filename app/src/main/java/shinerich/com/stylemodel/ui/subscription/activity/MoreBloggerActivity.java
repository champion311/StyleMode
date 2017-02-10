package shinerich.com.stylemodel.ui.subscription.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.base.BaseActivity;
import shinerich.com.stylemodel.bean.BloggerInfoBean;
import shinerich.com.stylemodel.bean.RefreshDataEvent;
import shinerich.com.stylemodel.inject.component.ActivityComponent;
import shinerich.com.stylemodel.presenter.MoreBolggersPresenter;
import shinerich.com.stylemodel.presenter.contract.MoreBloggersContract;
import shinerich.com.stylemodel.ui.login.LoginActivity;
import shinerich.com.stylemodel.ui.main.activity.BloggerInfoActivity;
import shinerich.com.stylemodel.ui.subscription.adapter.HotBloggerAdapter;
import shinerich.com.stylemodel.utils.RxBus;

/**
 * Created by Administrator on 2016/10/27.
 */
public class MoreBloggerActivity
        extends BaseActivity<MoreBolggersPresenter>
        implements MoreBloggersContract.View, XRecyclerView.LoadingListener, HotBloggerAdapter.OnItemClick {


    @BindView(R.id.mRecycleView)
    XRecyclerView mRecycleView;
    @BindView(R.id.back_icon)
    ImageView backIcon;
    @BindView(R.id.fl_content)
    FrameLayout fl_content;


    private int currentPage = 1;

    private boolean hasChangeDataSuccess = false;

    @Override
    protected int getContentViewID() {
        return R.layout.activitiy_more_bloggers;
    }

    @Override
    protected void injectDaggger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initViewAndEvents() {
        mRecycleView.setLoadingMoreEnabled(false);
        mRecycleView.setEmptyView(View.inflate(this, R.layout.item_empty_view, null));
        mRecycleView.setPullRefreshEnabled(false);
        mRecycleView.setLoadingListener(this);
        mPresenter.loadData();

    }

    @Override
    public void showError() {

    }

    private HotBloggerAdapter adapter;

    @Override
    public void showView(List<BloggerInfoBean> bloggerInfoBeen) {
        fl_content.setVisibility(View.VISIBLE);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        mRecycleView.setLayoutManager(gridLayoutManager);
        adapter = new HotBloggerAdapter(this, bloggerInfoBeen);
        adapter.setHideIcon(true);
        adapter.setUseBigView(false);
        adapter.setOnItemClick(this);

        mRecycleView.setAdapter(adapter);
        mRecycleView.setItemAnimator(null);
    }

    @Override
    public void notLoginAction() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void updateSelectors(View view, String id, String type, int position, boolean isSelected) {
        hasChangeDataSuccess = true;


        if ("1".equals(type)) {
            adapter.getBloggerInfoBeen().get(position).setIs_select(isSelected ? "1" : "0");
            adapter.notifyItemRangeChanged(position, 2);
        }
        RefreshDataEvent refreshDataEvent = new RefreshDataEvent();
        RxBus.getInstance().post(refreshDataEvent);


    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    @OnClick({R.id.back_icon})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.back_icon:
                finish();
                break;

        }
    }

    @Override
    public void click(View view, boolean isSelected, BloggerInfoBean bean, int position) {
        if (bean.getIs_select().equals("1")) {
            mPresenter.removeMySub(view, bean.getId(), String.valueOf(bean.getType()), position);
        } else if (bean.getIs_select().equals("0")) {
            mPresenter.addMySub(view, bean.getId(), String.valueOf(bean.getType()), position);
        }
    }

    @Override
    public void allClick(BloggerInfoBean bloggerInfoBean) {
        Intent intent = new Intent(this, BloggerInfoActivity.class);
        intent.putExtra("blogger_id", Integer.valueOf(bloggerInfoBean.getId()));
        startActivity(intent);


    }
}
