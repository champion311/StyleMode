package shinerich.com.stylemodel.ui.subscription.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.base.BaseActivity;
import shinerich.com.stylemodel.bean.RecommendData;
import shinerich.com.stylemodel.bean.RefreshDataEvent;
import shinerich.com.stylemodel.inject.component.ActivityComponent;
import shinerich.com.stylemodel.presenter.MoreContentActivityPresenter;
import shinerich.com.stylemodel.presenter.contract.MoreContentActivityContract;
import shinerich.com.stylemodel.ui.login.LoginActivity;
import shinerich.com.stylemodel.ui.subscription.adapter.RecommendAdapter;
import shinerich.com.stylemodel.utils.RxBus;

/**
 * Created by Administrator on 2016/11/18.
 */
public class MoreContentActivity extends
        BaseActivity<MoreContentActivityPresenter>
        implements MoreContentActivityContract.View, RecommendAdapter.OnItemClick {


    @BindView(R.id.back_icon)
    ImageView backIcon;
    @BindView(R.id.mRecycleView)
    XRecyclerView mRecycleView;
    @BindView(R.id.mTitle)
    TextView mTitle;
    @BindView(R.id.fl_content)
    FrameLayout fl_content;

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
        mPresenter.loadData();
        mTitle.setText("栏目内容");


    }

    @Override
    public void showError() {

    }

    private RecommendAdapter recommendAdapter;

    @Override
    public void showView(List<RecommendData> datas) {
        fl_content.setVisibility(View.VISIBLE);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mRecycleView.setLayoutManager(gridLayoutManager);
        recommendAdapter = new RecommendAdapter(this, datas);
        recommendAdapter.setOnItemClick(this);
        mRecycleView.setAdapter(recommendAdapter);
        mRecycleView.setItemAnimator(null);

    }

    @Override
    public void notLoginAction() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void updateSelectors(View view, String id, String type, int position, boolean isSelected) {
        if (recommendAdapter == null) {
            return;
        }
        recommendAdapter.getData().get(position).setIs_select(isSelected ? "1" : "0");
        recommendAdapter.notifyItemRangeChanged(position, 2);
        RefreshDataEvent refreshDataEvent = new RefreshDataEvent();
        RxBus.getInstance().post(refreshDataEvent);


    }

    @Override
    public void click(View view, boolean isSelected, RecommendData bean, int position) {
        if (bean.getIs_select().equals("1")) {
            mPresenter.removeMySub(view, bean.getId(), String.valueOf(bean.getType()), position);
        } else if (bean.getIs_select().equals("0")) {
            mPresenter.addMySub(view, bean.getId(), String.valueOf(bean.getType()), position);
        }

    }

    @Override
    public void allReClick(String id, String type) {

    }

    @OnClick({R.id.back_icon})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.back_icon:
                finish();
                break;

        }
    }


}

