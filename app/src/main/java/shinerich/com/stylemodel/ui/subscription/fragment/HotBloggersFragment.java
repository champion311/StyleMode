package shinerich.com.stylemodel.ui.subscription.fragment;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.base.BaseFragment;
import shinerich.com.stylemodel.bean.BloggerInfoBean;
import shinerich.com.stylemodel.presenter.HotBloggersPresenter;
import shinerich.com.stylemodel.presenter.contract.HotBloggerContract;
import shinerich.com.stylemodel.ui.main.activity.BloggerInfoActivity;
import shinerich.com.stylemodel.ui.subscription.adapter.HotBloggerAdapter;
import shinerich.com.stylemodel.utils.RxBus;

/**
 * Created by Administrator on 2016/10/14.
 */
public class HotBloggersFragment extends BaseFragment<HotBloggersPresenter>
        implements HotBloggerContract.View, HotBloggerAdapter.OnItemClick {

    @BindView(R.id.mRecyclerView)
    XRecyclerView mRecyclerView;

    private View emptyView;

    private List<BloggerInfoBean> data = new ArrayList<>();


    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_hot_bloggers;
    }

    @Override
    protected void initEventAndData() {
        //mRecyclerView.setLoadMoreView();
        emptyView = View.inflate(getContext(), R.layout.item_sub_empty_view, null);

        mRecyclerView.setEmptyView(emptyView);
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mPresenter.loadData();
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadData();
            }

            @Override
            public void onLoadMore() {

            }
        });


    }

    @Override
    public void showError() {

    }

    private HotBloggerAdapter adapter;

    @Override
    public void showView(List<BloggerInfoBean> bloggerInfoBeen) {
        mRecyclerView.refreshComplete();
        adapter = new HotBloggerAdapter(getActivity(), bloggerInfoBeen);
        adapter.setUseBigView(true);
        adapter.setOnItemClick(this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemAnimator(null);

    }

    @Override
    public void refreshSelectorIcon(View view, boolean isSelected, int position) {
        if (adapter == null) {
            return;
        }
        List<BloggerInfoBean> datas = adapter.getBloggerInfoBeen();
        if (isSelected) {
            datas.get(position).setIs_select("1");
        } else {
            datas.get(position).setIs_select("0");
            RxBus.getInstance().post(datas.get(position));
        }
        adapter.notifyItemRangeChanged(position, 2);


    }


    @Override
    public void click(View view, boolean isSelected, BloggerInfoBean bean, int position) {
        //记录选中情况
        //bean.setToAddOrRemore(isSelected);
        //data.add(bean);
        if (isSelected) {
            mPresenter.addRecommend(view, bean, position);
        } else {
            mPresenter.removeRecommend(view, bean, position);
        }
    }

    @Override
    public void allClick(BloggerInfoBean bloggerInfoBean) {
        Intent intent = new Intent(getActivity(), BloggerInfoActivity.class);
        intent.putExtra("blogger_id", Integer.valueOf(bloggerInfoBean.getId()));
        startActivity(intent);


    }

    @Override
    public void updateFromMineSub(BloggerInfoBean infoBean) {
        if (adapter == null) {
            return;
        }
        int pos = adapter.getPosition(infoBean.getId());
        if (pos != -1) {
            adapter.getBloggerInfoBeen().get(pos).setIs_select(infoBean.getIs_select());
            adapter.notifyItemRangeChanged(pos, 2);
        }
    }

}
