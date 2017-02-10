package shinerich.com.stylemodel.ui.main.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.base.BaseActivity;
import shinerich.com.stylemodel.bean.CommentDetails;
import shinerich.com.stylemodel.bean.CommentResponseBean;
import shinerich.com.stylemodel.bean.ReplyBean;
import shinerich.com.stylemodel.inject.component.ActivityComponent;
import shinerich.com.stylemodel.presenter.CommentActivityPresenter;
import shinerich.com.stylemodel.presenter.contract.CommentActivityContract;
import shinerich.com.stylemodel.ui.login.LoginSelectActivity;
import shinerich.com.stylemodel.ui.main.adapter.CommentAdapter;
import shinerich.com.stylemodel.utils.ThemeHelper;
import shinerich.com.stylemodel.utils.ToastUtils;

/**
 * Created by Administrator on 2016/10/31.
 */
public class CommentActivity extends BaseActivity<CommentActivityPresenter>
        implements CommentActivityContract.View, XRecyclerView.LoadingListener,
        CommentAdapter.CommentAdapterClick {


    @BindView(R.id.back_icon)
    ImageView backIcon;
    @BindView(R.id.mTitle)
    TextView mTitle;
    @BindView(R.id.mEditText)
    EditText mEditText;
    @BindView(R.id.input_wrapper)
    LinearLayout input_wrapper;
    @BindView(R.id.mRecy)
    XRecyclerView mRecy;
    @BindView(R.id.mEmptyView)
    LinearLayout mEmptyView;

    private String id;

    private String type;

    private int currentPage = 1;

    private List<CommentDetails> glData = new ArrayList<>();

    private CommentAdapter adapter;


    @Override
    protected int getContentViewID() {
        return R.layout.activity_comment;
    }

    @Override
    protected void injectDaggger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }


    @Override
    protected void initViewAndEvents() {
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//        if (!ThemeHelper.isNightMode(this)) {
//            getWindow().setStatusBarColor(Color.WHITE);
//        } else {
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
        //}
        if (!ThemeHelper.isNightMode(this)) {
            setSystemBarColorRes(R.color.white);
        } else {
            setSystemBarColorRes(R.color.theme_color_primary);
        }
        if (!ThemeHelper.isNightMode(this)) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if (getIntent() != null) {
            id = getIntent().getStringExtra("id");
            type = getIntent().getStringExtra("type");
            mPresenter.loadData(id, type, "1");

            View emptyView = View.inflate(mContext, R.layout.item_comment_empty_view, null);
            emptyView.setVisibility(View.VISIBLE);
            mRecy.setLoadingMoreEnabled(true);
            mRecy.setLoadingListener(this);
            mRecy.setEmptyView(emptyView);
            mRecy.setLayoutManager(new LinearLayoutManager(this));
            adapter = new CommentAdapter(this, glData);
            mRecy.setPullRefreshEnabled(false);
            mRecy.setAdapter(adapter);
            mRecy.setItemAnimator(null);

            input_wrapper.setVisibility(View.GONE);

        }
    }

    @Override
    public void showError() {

    }


    @Override
    public void showData(CommentResponseBean bean, String page) {


        if ("1".equals(page)) {
            mTitle.setText("评论" + bean.getTotal_num());
            if ("0".equals(bean.getTotal_num())) {
                mEmptyView.setVisibility(View.VISIBLE);
            }
            glData.clear();
            if (bean.getHotlist().size() > 0) {
                bean.getHotlist().get(0).setHeadFlag(1);
                glData.addAll(bean.getHotlist());
            }

            if (bean.getNewlist().size() > 0) {
                bean.getNewlist().get(0).setHeadFlag(2);
                glData.addAll(bean.getNewlist());
            }
            // mRecy.setAdapter(new CommentAdapter(this, glData));
            adapter.setmClick(this);
            currentPage = 1;
            mRecy.refreshComplete();


        } else {
            if (bean.getNewlist().size() > 0) {
                glData.addAll(bean.getNewlist());
            } else {
                ToastUtils.show(this, "没有更多评论了");
            }
            currentPage += 1;
            mRecy.loadMoreComplete();
        }
        adapter.notifyDataSetChanged();


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
    public void onRefresh() {
        mPresenter.loadData(id, type, "1");
    }

    @Override
    public void onLoadMore() {
        mPresenter.loadData(id, type, String.valueOf(currentPage + 1));
    }

    @Override
    public void addPraise(View view, String comment_id, int position, boolean hasPraised) {
        //点赞或者取消点赞
        if (hasPraised) {
            mPresenter.delPraise(view, comment_id, position);
        } else {
            mPresenter.addPraise(view, comment_id, position);
        }

    }


    @Override
    public void showCommentIsPraised(View view, int position, boolean isPraised) {
        CommentDetails commentDetails = glData.get(position);
        if (isPraised) {
            //glData.get(position).setIs_praise("1");
            commentDetails.setIs_praise("1");
            commentDetails.setPraise_num(commentDetails.getPraise_num() + 1);
        } else {
            commentDetails.setIs_praise("0");
            if (commentDetails.getPraise_num() > 0) {
                commentDetails.setPraise_num(commentDetails.getPraise_num() - 1);
            }
        }
        glData.set(position, commentDetails);
        adapter.notifyItemRangeChanged(position, 2);
        //view.setSelected(isPraised);
    }


    @Override
    public void addReply(View view, final String comment_id,
                         final String reply_id,
                         final String reply_uid,
                         String nickName, final int position) {

        input_wrapper.setVisibility(View.VISIBLE);
        mEditText.setHint("回复:" + nickName);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    /**
                     * 回复
                     *
                     * @param comment_id 评论ID
                     * @param reply_id   回复id 回复评论时候传0
                     * @param reply_uid  所回复内容的用户id
                     * @param content
                     */
                    mPresenter.addReply(comment_id, reply_id, reply_uid, mEditText.getText().toString(),
                            position);
                }
                return false;
            }
        });
    }

    /**
     * 添加回复成功
     *
     * @param replyBeans
     * @param position
     */

    @Override
    public void addReplyData(List<ReplyBean> replyBeans, int position) {
        input_wrapper.setVisibility(View.GONE);
        glData.get(position).getReply().clear();
        glData.get(position).getReply().addAll(replyBeans);
        mEditText.setText("");
        adapter.notifyItemRangeChanged(position, 2, 1);
    }

    @Override
    public void notLoginAction() {
        Intent intent = new Intent(this, LoginSelectActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}


