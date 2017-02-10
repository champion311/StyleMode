package shinerich.com.stylemodel.ui.subscription.fragment;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.base.BaseFragment;
import shinerich.com.stylemodel.bean.BloggerInfoBean;
import shinerich.com.stylemodel.bean.MySubBloggerBean;
import shinerich.com.stylemodel.bean.MySubCateBean;
import shinerich.com.stylemodel.bean.MySubscribe;
import shinerich.com.stylemodel.bean.RecommendData;
import shinerich.com.stylemodel.presenter.MineSubPresenter;
import shinerich.com.stylemodel.presenter.contract.MineSubContract;
import shinerich.com.stylemodel.ui.subscription.adapter.MySubBloggersAdapter;
import shinerich.com.stylemodel.ui.subscription.adapter.MySubContentAdapter;
import shinerich.com.stylemodel.utils.CollectionsUtils;
import shinerich.com.stylemodel.utils.RxBus;

/**
 * Created by Administrator on 2016/10/14.
 */
public class MineSubFragment extends
        BaseFragment<MineSubPresenter> implements MineSubContract.View,
        MySubBloggersAdapter.OnItemClick, MySubContentAdapter.OnItemClickListener {

    @BindView(R.id.bloggers_recy)
    RecyclerView bloggersRecy;
    @BindView(R.id.content_recy)
    RecyclerView contentRecy;
    @BindView(R.id.arrorw_btn)
    ImageView arrorwBtn;
    @BindView(R.id.mImage)
    ImageView mImage;
    @BindView(R.id.alert_text)
    TextView alertText;
    @BindView(R.id.mParentView)
    LinearLayout mEmptyParentView;
    @BindView(R.id.content_view)
    NestedScrollView nestedScrollView;
    @BindView(R.id.fl_content)
    FrameLayout fl_content;


    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine_sub;
    }

    @Override
    protected void initEventAndData() {
        mPresenter.refreshData();
    }

    @Override
    public void showError() {

    }

    private MySubBloggersAdapter bloggersAdapter;

    private MySubContentAdapter contentAdapter;

    @Override
    public void showView(MySubscribe mySubscribe) {
        if (CollectionsUtils.isEmpty(mySubscribe.getBlogger()) &&
                CollectionsUtils.isEmpty(mySubscribe.getCategory())
                ) {
            mEmptyParentView.setVisibility(View.VISIBLE);
            nestedScrollView.setVisibility(View.GONE);
            fl_content.setVisibility(View.GONE);
        }
        bloggersAdapter = new MySubBloggersAdapter(getActivity(), mySubscribe.getBlogger());
        bloggersAdapter.setOnItemClick(this);
        bloggersRecy.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        bloggersRecy.setAdapter(bloggersAdapter);
        bloggersRecy.setItemAnimator(null);
        for (int i = 6; i < bloggersRecy.getChildCount(); i++) {
            bloggersRecy.getChildAt(i).setVisibility(View.GONE);
        }


        contentAdapter = new MySubContentAdapter(getActivity(), mySubscribe.getCategory());
        contentAdapter.setOnItemClickListener(this);
        contentRecy.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        contentRecy.setAdapter(contentAdapter);
        contentRecy.setItemAnimator(null);

    }

    @Override
    public void refreshSelectorIcon(String id, String type, View view, boolean isSelected) {
        //view.setSelected(isSelected);
        if ("2".equals(type) || "0".equals(type)) {
            //栏目内容或者编辑
            if (contentAdapter == null) {
                return;
            }
            MySubCateBean bean = contentAdapter.searchData(id, type);
            if (bean != null) {
                bean.setSelected(isSelected);
            }
            int position = contentAdapter.getData().indexOf(bean);
            if (position != -1) {
                contentAdapter.notifyItemRangeChanged(position, 2);
            }
            RecommendData recommendData = new RecommendData();
            recommendData.setId(id);
            recommendData.setType(type);
            if (isSelected) {
                recommendData.setIs_select("1");
            } else {
                recommendData.setIs_select("0");
            }

            RxBus.getInstance().post(recommendData);

        } else {
            //博主
            if (bloggersAdapter == null) {
                return;
            }
            MySubBloggerBean bean = bloggersAdapter.searchData(id);
            int position = bloggersAdapter.getData().indexOf(bean);
            if (bean != null) {
                bean.setSelected(isSelected);
            }
            if (position != -1) {
                bloggersAdapter.notifyItemRangeChanged(position, 2);
            }
            BloggerInfoBean bloggerInfoBean = new BloggerInfoBean();
            bloggerInfoBean.setId(id);
            bloggerInfoBean.setType(Integer.valueOf(type));
            if (isSelected) {
                bloggerInfoBean.setIs_select("1");
            } else {
                bloggerInfoBean.setIs_select("0");
            }
            RxBus.getInstance().post(bloggerInfoBean);

        }

    }

    @Override
    public void updateRecommendData(RecommendData data) {
        if (contentAdapter == null) {
            return;
        }
        MySubCateBean bean = contentAdapter.searchData(data.getId(), data.getType());
        int position = contentAdapter.getData().indexOf(bean);
        if (bean != null) {
            bean.setSelected("1".equals(data.getIs_select()));
        }
        if (position != -1) {
            contentAdapter.notifyItemRangeChanged(position, 2);
        }

    }

    @Override
    public void updateBloggerData(BloggerInfoBean data) {
        if (bloggersAdapter == null) {
            return;
        }
        MySubBloggerBean bean = bloggersAdapter.searchData(data.getId());
        int position = bloggersAdapter.getData().indexOf(bean);
        if (bean != null) {
            bean.setSelected("1".equals(data.getIs_select()));
        }
        if (position != -1) {
            bloggersAdapter.notifyItemRangeChanged(position, 2);
        }

    }


    @Override
    public void onBolggerItemClick(View view, boolean isSelected, MySubBloggerBean bean) {
        if (isSelected) {
            mPresenter.addMySub(view, bean.getId() + "", bean.getUtype() + "");
        } else {
            mPresenter.removeMySub(view, bean.getId() + "", bean.getUtype() + "");
        }

    }

    @Override
    public void OnContentItemListener(View view, boolean isSelected, MySubCateBean bean) {
        if (isSelected) {
            mPresenter.addMySub(view, bean.getId() + "", bean.getUtype() + "");
        } else {
            mPresenter.removeMySub(view, bean.getId() + "", bean.getUtype() + "");
        }
    }

    @OnClick({R.id.arrorw_btn})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.arrorw_btn:
                //TODO

                break;
        }
    }



}
