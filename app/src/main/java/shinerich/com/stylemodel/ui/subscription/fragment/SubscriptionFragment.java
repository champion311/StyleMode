package shinerich.com.stylemodel.ui.subscription.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bilibili.magicasakura.widgets.TintRelativeLayout;
import com.bilibili.magicasakura.widgets.TintTextView;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.base.BaseFragment;
import shinerich.com.stylemodel.bean.BloggerDetailBean;
import shinerich.com.stylemodel.bean.BloggersContentInfoBean;
import shinerich.com.stylemodel.bean.ContentData;
import shinerich.com.stylemodel.bean.SubHomePageContentBean;
import shinerich.com.stylemodel.bean.UserInfo;
import shinerich.com.stylemodel.engin.LoginUserProvider;
import shinerich.com.stylemodel.presenter.SubScriptionPresenter;
import shinerich.com.stylemodel.presenter.contract.SubScriptionContract;
import shinerich.com.stylemodel.ui.login.LoginSelectActivity;
import shinerich.com.stylemodel.ui.main.activity.ArticleContentActivity;
import shinerich.com.stylemodel.ui.main.activity.BloggerInfoActivity;
import shinerich.com.stylemodel.ui.main.activity.ColumnActivity;
import shinerich.com.stylemodel.ui.main.activity.ImageListActivity;
import shinerich.com.stylemodel.ui.subscription.activity.MoreBloggerActivity;
import shinerich.com.stylemodel.ui.subscription.activity.MoreContentActivity;
import shinerich.com.stylemodel.ui.subscription.activity.SubscriptionArrangementActivity;
import shinerich.com.stylemodel.ui.subscription.adapter.BloggerHeadAdapter;
import shinerich.com.stylemodel.ui.subscription.adapter.ContentDataAdapter;
import shinerich.com.stylemodel.ui.subscription.adapter.SubHomePageContentAdapter;
import shinerich.com.stylemodel.utils.CollectionsUtils;
import shinerich.com.stylemodel.utils.ThemeHelper;
import shinerich.com.stylemodel.utils.ToastUtils;
import shinerich.com.stylemodel.widget.wheelview.CustomListLayoutMananger;

/**
 * Created by Administrator on 2016/9/29.
 */
public class SubscriptionFragment extends
        BaseFragment<SubScriptionPresenter>
        implements SubScriptionContract.View,
        BloggerHeadAdapter.OnBloggerHeadClickListener,
        ContentDataAdapter.OnContentClickListener,
        SubHomePageContentAdapter.OnDataClickListener, XRecyclerView.LoadingListener {


    @BindView(R.id.sub_arrangement_button)
    TextView subArrangementButton;
    @BindView(R.id.mSubscribeContentRecy)
    XRecyclerView mSubscribeContentRecy;
    @BindView(R.id.mParentView)
    LinearLayout mParentView;
    @BindView(R.id.new_error_icon)
    ImageView newErrorIcon;
    @BindView(R.id.new_error_text)
    TintTextView newErrorText;
    @BindView(R.id.refresh_button)
    TextView refreshButton;
    @BindView(R.id.empty_view)
    TintRelativeLayout emptyView;


    private SubHomePageContentAdapter mineContentAdapter;

    View headView;

    private List<SubHomePageContentBean> datas;

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_subcription;
    }

    @Override
    protected void initEventAndData() {
        initHeader();
        datas = new ArrayList<>();
        initRecycleView();
        reloadData();
    }

    public void initRecycleView() {
        mineContentAdapter = new SubHomePageContentAdapter(getActivity(), datas);
        mineContentAdapter.setListener(this);
        mSubscribeContentRecy.setItemAnimator(null);
        mSubscribeContentRecy.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSubscribeContentRecy.setAdapter(mineContentAdapter);
        mSubscribeContentRecy.addHeaderView(headView);
        mSubscribeContentRecy.setPullRefreshEnabled(false);
        mSubscribeContentRecy.setLoadingListener(this);
    }

    public void reloadData() {
        mParentView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        mPresenter.loadData();
        if (LoginUserProvider.currentStatus) {
            UserInfo info = LoginUserProvider.getUser(getActivity());
            mPresenter.loadContentData(info.getId(), info.getKey(), 1);
            subArrangementButton.setVisibility(View.VISIBLE);
        } else {
            mPresenter.loadContentData("0", "0", 1);
            subArrangementButton.setVisibility(View.GONE);
        }
        mSubscribeContentRecy.setPullRefreshEnabled(true);
        mSubscribeContentRecy.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mSubscribeContentRecy.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mSubscribeContentRecy.setLoadingMoreEnabled(true);
        subArrangementButton.setVisibility(View.VISIBLE);
        bloggerForMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MoreBloggerActivity.class));
            }
        });
        contentForMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MoreContentActivity.class));
            }
        });
    }


    TextView bloggerForMore;

    RecyclerView mRecommendBloggerRecy;

    TextView contentForMore;

    RecyclerView mRecommendContentRecy;

    public void initHeader() {
        //headView = //LayoutInflater.from(mContext).inflate(R.layout., mParentView, false);
        headView = View.inflate(mContext, R.layout.item_sub_top_view, null);
        bloggerForMore = ButterKnife.findById(headView, R.id.blogger_for_more);
        mRecommendBloggerRecy = ButterKnife.findById(headView, R.id.mRecommendBloggerRecy);
        contentForMore = ButterKnife.findById(headView, R.id.content_for_more);
        contentForMore.setVisibility(View.GONE);
        mRecommendContentRecy = ButterKnife.findById(headView, R.id.mRecommendContentRecy);


    }


    @Override
    public void showError() {
        emptyView.setVisibility(View.VISIBLE);
        mSubscribeContentRecy.setVisibility(View.GONE);
        mRecommendBloggerRecy.setVisibility(View.GONE);
        mRecommendContentRecy.setVisibility(View.GONE);
        newErrorText.setText("网络连接错误");
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadData();
            }
        });


    }

    @OnClick({R.id.sub_arrangement_button})
    public void OnClick(View view) {
        if (!LoginUserProvider.currentStatus) {
            return;
        }
        switch (view.getId()) {
            case R.id.sub_arrangement_button:
                Intent intent = new Intent(getActivity(), SubscriptionArrangementActivity.class);
                startActivity(intent);
                break;
        }
    }


    private BloggerHeadAdapter hotBloggerAdapter;

    private ContentDataAdapter contentAdapter;

    @Override
    public void setViews(BloggersContentInfoBean bean) {
        //mSubscribeContentRecy.setRefreshing(false);

        if (!CollectionsUtils.isEmpty(bean.getBlogger_data())) {
            hotBloggerAdapter =
                    new BloggerHeadAdapter(getActivity(), bean.getBlogger_data());
            hotBloggerAdapter.setListener(this);
            mRecommendBloggerRecy.setLayoutManager(new CustomListLayoutMananger(getActivity(), 4, false));
            mRecommendBloggerRecy.setItemAnimator(null);
            mRecommendBloggerRecy.setAdapter(hotBloggerAdapter);

        }

        if (!CollectionsUtils.isEmpty(bean.getCategory())) {
            contentAdapter = new
                    ContentDataAdapter(getActivity(), bean.getCategory());
            contentAdapter.setListener(this);
            mRecommendContentRecy.setLayoutManager(new CustomListLayoutMananger(getActivity(), 3, false));
            mRecommendContentRecy.setAdapter(contentAdapter);
            mRecommendContentRecy.setItemAnimator(null);
        }
    }


    @Override
    public void updateSelectors(View view, String id, String type, int position,
                                boolean isSelected) {

        if ("1".equals(type)) {
            //博主
            hotBloggerAdapter.getData().get(position).
                    setIs_select(isSelected ? "1" : "0");
            hotBloggerAdapter.notifyItemRangeChanged(position, 2);

        } else if ("2".equals(type)) {
            //栏目
            contentAdapter.getData().get(position).setIs_select(isSelected ? "1" : "0");
            contentAdapter.notifyItemRangeChanged(position, 2);
        }


    }


    @Override
    public void OnClick(View view, BloggerDetailBean detailBean, int position) {
        if ("1".equals(detailBean.getIs_select())) {
            //当前选中
            mPresenter.removeMySub(view, detailBean.getId(), detailBean.getType(), position);
        } else {
            mPresenter.addMySub(view, detailBean.getId(), detailBean.getType(), position);
        }


    }


    @Override
    public void OnContentClickListener(View view, ContentData data, int position) {
        if ("1".equals(data.getIs_select())) {
            mPresenter.removeMySub(view, data.getId(), data.getType(), position);
        } else {
            mPresenter.addMySub(view, data.getId(), data.getType(), position);
        }

    }

    @Override
    public void ItemClick(View view, BloggerDetailBean detailBean, int position) {
        Intent intent = new Intent(getActivity(), BloggerInfoActivity.class);
        intent.putExtra("blogger_id", Integer.valueOf(detailBean.getId()));
        startActivity(intent);

    }


    @Override
    public void ItemClick(View view, ContentData data, int position) {
        int type = Integer.valueOf(data.getType());
        if (type == 2) {
            Intent intent = new Intent(getActivity(), ColumnActivity.class);
            intent.putExtra("cate_id", data.getId());
            startActivity(intent);
        } else {
            Intent intent = new Intent(getActivity(), BloggerInfoActivity.class);
            intent.putExtra("blogger_id", Integer.valueOf(data.getId()));
            intent.putExtra("type", type);
            startActivity(intent);
        }


    }


    private int currentPage;

    @Override
    public void updateContentData(List<SubHomePageContentBean> datas, int page) {

        mSubscribeContentRecy.refreshComplete();
        mSubscribeContentRecy.loadMoreComplete();
        mSubscribeContentRecy.setRefreshing(false);
        if (page == 1) {
            mSubscribeContentRecy.refreshComplete();
        } else {
            mSubscribeContentRecy.loadMoreComplete();
        }
//        mSubscribeContentRecy.setLoadingMoreEnabled(true);
//        mSubscribeContentRecy.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
//        mSubscribeContentRecy.setPullRefreshEnabled(false);
        if (page == 1) {
            this.datas.clear();
            this.datas.addAll(datas);
            currentPage = 1;

        } else {
            this.datas.addAll(datas);
            currentPage += 1;
            if (datas.size() == 0) {
                mSubscribeContentRecy.setLoadingMoreEnabled(false);
                ToastUtils.show(getActivity(), "没有更多内容了");
            }

        }
        mineContentAdapter.notifyDataSetChanged();
    }

    @Override
    public void notLoginAction() {
        //未登录的时候访问操作
        Intent intent = new Intent(getActivity(), LoginSelectActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    @Override
    public void nightEventAction() {
        if (ThemeHelper.isNightMode(getActivity())) {
            mParentView.setBackgroundColor(Color.parseColor("#454547"));
        } else {
            mParentView.setBackgroundColor(Color.parseColor("#ededed"));
        }
//        initRecycleView();
//        reloadData();
        mineContentAdapter = new SubHomePageContentAdapter(getActivity(), datas);
        mineContentAdapter.setListener(this);
        mSubscribeContentRecy.setAdapter(mineContentAdapter);
        reloadData();


    }



    @Override
    public void OnHeadClickListener(View view, String id, String type) {
//        "id": "11",
//                "type": "2",//类型  0编辑 1博主2栏目
        //头像点击事件
        switch (Integer.valueOf(type)) {
            case 0:
                Intent intent = new Intent(getActivity(), BloggerInfoActivity.class);
                intent.putExtra("blogger_id", Integer.valueOf(id));
                intent.putExtra("type", type);
                startActivity(intent);
                break;
            case 1:
                Intent intent2 = new Intent(getActivity(), BloggerInfoActivity.class);
                intent2.putExtra("blogger_id", Integer.valueOf(id));
                intent2.putExtra("type", type);
                startActivity(intent2);
                break;
            case 2:
                Intent intent3 = new Intent(getActivity(), ColumnActivity.class);
                intent3.putExtra("cate_id", id);
                startActivity(intent3);
                break;
        }


    }

    @Override
    public void OnContentClickListener(View view, String id, String type, int position) {
//        "id": "9097",
//                "type": "0",// 0文章1图集2视频
        //内容点击事件
        switch (Integer.valueOf(type)) {
            case 0:
                Intent intent = new Intent(getActivity(), ArticleContentActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("type", type);
                startActivity(intent);

                break;
            case 1:
                Intent intent2 = new Intent(getActivity(), ImageListActivity.class);
                intent2.putExtra("id", id);
                intent2.putExtra("type", type);
                startActivity(intent2);
                break;
            case 2:
                Intent intent3 = new Intent(getActivity(), ArticleContentActivity.class);
                intent3.putExtra("id", id);
                intent3.putExtra("type", type);
                startActivity(intent3);
                break;

            case 3:
                Intent intent4 = new Intent(getActivity(), ArticleContentActivity.class);
                intent4.putExtra("id", id);
                intent4.putExtra("type", type);
                startActivity(intent4);
                break;
        }
    }

    @Override
    public void onRefresh() {
        mPresenter.loadData();
        if (LoginUserProvider.currentStatus) {
            UserInfo info = LoginUserProvider.getUser(getActivity());
            mPresenter.loadContentData(info.getId(), info.getKey(), 1);
        } else {
            mPresenter.loadContentData("0", "0", 1);
        }

    }

    @Override
    public void onLoadMore() {
        if (LoginUserProvider.currentStatus) {
            UserInfo info = LoginUserProvider.getUser(getActivity());
            mPresenter.loadContentData(info.getId(), info.getKey(), currentPage + 1);
        } else {
            mPresenter.loadContentData("0", "0", 1);
        }

    }

    @Override
    public void refreshData() {
        //刷新订阅博主和内容部分
        onRefresh();
    }


    @Override
    public void onResume() {
        //reloadData();
        if (LoginUserProvider.currentStatus) {
            subArrangementButton.setVisibility(View.VISIBLE);
        } else {
            subArrangementButton.setVisibility(View.GONE);
        }
        super.onResume();
    }

    public void removeHeaderAndFooter() {
        try {
            Field filedWrapAdapter = XRecyclerView.class.getDeclaredField("mWrapAdapter");
            filedWrapAdapter.setAccessible(true);
            Object object = filedWrapAdapter.get(mSubscribeContentRecy);

            Field fieldMheaderView = XRecyclerView.class.getDeclaredField("mHeaderViews");
            fieldMheaderView.setAccessible(true);
            Object headObJ = fieldMheaderView.get(mSubscribeContentRecy);
            Type type = new TypeToken<ArrayList<View>>() {
            }.getType();
            if (headObJ != null) {
                ArrayList<View> views = (ArrayList<View>) headObJ;
                if (object != null) {
                    //views.removeAll(views.subList(1, views.size() - 1));
                    views.clear();
                    RecyclerView.Adapter<RecyclerView.ViewHolder> recyAdapter =
                            (RecyclerView.Adapter<RecyclerView.ViewHolder>) object;
                    recyAdapter.notifyDataSetChanged();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
