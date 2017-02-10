package shinerich.com.stylemodel.ui.subscription.fragment;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.base.BaseFragment;
import shinerich.com.stylemodel.bean.RecommendData;
import shinerich.com.stylemodel.presenter.RecommendPresenter;
import shinerich.com.stylemodel.presenter.contract.RecommendContentContract;
import shinerich.com.stylemodel.ui.main.activity.BloggerInfoActivity;
import shinerich.com.stylemodel.ui.main.activity.ColumnActivity;
import shinerich.com.stylemodel.ui.subscription.adapter.RecommendAdapter;
import shinerich.com.stylemodel.utils.RxBus;

/**
 * Created by Administrator on 2016/10/14.
 */
public class RecommendFragment extends BaseFragment<RecommendPresenter>
        implements RecommendContentContract.View, RecommendAdapter.OnItemClick {


    @BindView(R.id.mRecyclerView)
    XRecyclerView mRecyclerView;

    private View emptyView;

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recommend_content;
    }

    @Override
    protected void initEventAndData() {
        //mRecyclerView.setLoadMoreView();
        emptyView = View.inflate(getContext(), R.layout.item_sub_empty_view, null);
        //ContentDataAdapter adapter = new ContentDataAdapter(getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        mRecyclerView.setEmptyView(emptyView);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        //mRecyclerView.setAdapter(adapter);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        //mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        mRecyclerView.refreshComplete();

                    }
                }, 1 * 1000);

            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        mRecyclerView.loadMoreComplete();

                    }
                }, 1 * 1000);

            }
        });


        mPresenter.loadData();

    }


    @Override
    public void showError() {

    }


    private RecommendAdapter recommendAdapter;

    @Override
    public void showView(List<RecommendData> recommendDatas) {
        recommendAdapter = new RecommendAdapter(getActivity(), recommendDatas);
        recommendAdapter.setOnItemClick(this);
        mRecyclerView.setAdapter(recommendAdapter);
        mRecyclerView.setItemAnimator(null);
    }

    @Override
    public void refreshSelectorIcon(View view, int position, boolean isSelected) {
        List<RecommendData> datas = recommendAdapter.getData();
        if (isSelected) {
            datas.get(position).setIs_select("1");
        } else {
            datas.get(position).setIs_select("0");
            //只减不加
            RxBus.getInstance().post(datas.get(position));
        }
        recommendAdapter.notifyItemRangeChanged(position, 2);
    }


    @Override
    public void click(View view, boolean isSelected, RecommendData bean, int position) {
        if (isSelected) {
            mPresenter.addRecommend(view, bean, position);
        } else {
            mPresenter.removeRecommend(view, bean, position);
        }
    }

    @Override
    public void allReClick(String id, String type) {

        //2是栏目  0 是编辑


        switch (Integer.valueOf(type)) {

            case 2:
                //2是栏目
                Intent intent = new Intent(getActivity(), ColumnActivity.class);
                intent.putExtra("cate_id", id);
                startActivity(intent);

                break;
            case 0:
                //o为编辑
                Intent intent2 = new Intent(getActivity(), BloggerInfoActivity.class);
                intent2.putExtra("blogger_id", Integer.valueOf(id));
                intent2.putExtra("type", 1);
                startActivity(intent2);
                break;


        }


    }

    @Override
    public void updateDataFromMideSub(RecommendData data) {
        if (recommendAdapter == null) {
            return;
        }
        int pos = recommendAdapter.getDataPos(data.getId(), data.getType());
        if (pos != -1) {
            recommendAdapter.getData().get(pos).setIs_select(data.getIs_select());
            recommendAdapter.notifyItemRangeChanged(pos, 2);
        }
    }
}
