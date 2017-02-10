package shinerich.com.stylemodel.ui.main.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.bean.CommentDetails;
import shinerich.com.stylemodel.bean.ReplyBean;
import shinerich.com.stylemodel.utils.CollectionsUtils;
import shinerich.com.stylemodel.utils.GlideCircleTransform;
import shinerich.com.stylemodel.utils.HDateUtils;
import shinerich.com.stylemodel.widget.CircleImageView;


/**
 * Created by Administrator on 2016/11/1.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {


    private List<CommentDetails> data;

    private Context mContext;

    private CommentAdapterClick mClick;

    private ReplyAdapter.ReplyClickInterface mReplyClick;

    public ReplyAdapter.ReplyClickInterface getmReplyClick() {
        return mReplyClick;
    }

    public void setmReplyClick(ReplyAdapter.ReplyClickInterface mReplyClick) {
        this.mReplyClick = mReplyClick;
    }

    public void setmClick(CommentAdapterClick mClick) {
        this.mClick = mClick;
    }

    public CommentAdapter(Context mContext, List<CommentDetails> data) {
        this.data = data;
        this.mContext = mContext;
    }

    public List<CommentDetails> getData() {
        return data;
    }

    public void setData(List<CommentDetails> data) {
        this.data = data;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_comment, null);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CommentViewHolder holder, final int position) {
        final CommentDetails bean = data.get(position);

        Glide.with(mContext).load(bean.getUsericon()).error(R.drawable.default_comment_head).
                diskCacheStrategy(DiskCacheStrategy.SOURCE).transform
                (new GlideCircleTransform(mContext)).placeholder(R.drawable.default_comment_head).
                into(holder.commentHead);

        if (bean.getReply().size() > 0) {
            holder.dismissBtn.setVisibility(View.VISIBLE);
            holder.mReplyContent.setVisibility(View.VISIBLE);
            holder.dismissBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (holder.mReplyContent.getVisibility() == View.VISIBLE) {
                        holder.mReplyContent.setVisibility(View.GONE);
                        holder.dismissBtn.setText("显示回复");
                    } else {
                        holder.mReplyContent.setVisibility(View.VISIBLE);
                        holder.dismissBtn.setText("隐藏回复");
                    }
                }
            });

        } else {
            holder.dismissBtn.setVisibility(View.GONE);
            holder.mReplyContent.setVisibility(View.GONE);
        }
        holder.commentName.setText(bean.getNickname());
        holder.mCommentContent.setText(bean.getContent());
        holder.submitTime.setText(HDateUtils.getDate(Long.valueOf(bean.getAddtime())));
        //holder.addPraiseText.setText();
        ReplyAdapter replyAdapter = new ReplyAdapter(mContext, bean.getReply());
        replyAdapter.setmClick(new ReplyAdapter.ReplyClickInterface() {
            @Override
            public void call(View view, ReplyBean replyBean, int innerPosition) {
                if (mClick != null) {
                    //回复回复
                    mClick.addReply(view,
                            bean.getId(),
                            replyBean.getReply_id(), replyBean.getUser_id(), replyBean.getNickname(), position);
                }
            }
        });
        holder.mReplyContent.setLayoutManager(new LinearLayoutManager(mContext));
        holder.mReplyContent.setAdapter(replyAdapter);
        replyAdapter.notifyDataSetChanged();

        if (bean.getHeadFlag() == 1) {
            holder.headViewParent.setVisibility(View.VISIBLE);
            holder.mheaderText.setText("最热评论");
            holder.mReplyContent.setVisibility(View.GONE);
        } else if (bean.getHeadFlag() == 2) {
            holder.headViewParent.setVisibility(View.VISIBLE);
            holder.mheaderText.setText("最新评论");
            holder.mReplyContent.setVisibility(View.GONE);
        } else {
            holder.headViewParent.setVisibility(View.GONE);
            holder.mReplyContent.setVisibility(View.VISIBLE);
        }

        holder.addPraiseText.setText(bean.getPraise_num() + "赞");
        final boolean hasPraised = "1".equals(bean.getIs_praise());
        holder.addPraiseIcon.setSelected(hasPraised);
        holder.addPraiseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClick != null) {
                    //点赞按钮
                    mClick.addPraise(v, String.valueOf(bean.getId()), position,
                            hasPraised);
                }
            }
        });
        holder.mRlcontainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //回复评论
                if (mClick != null) {
                    mClick.addReply(v, bean.getId(), "0", bean.getUser_id(), bean.getNickname(), position);
                }
            }
        });
    }


    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position, List<Object> payloads) {
        //用来更新回复
        if (payloads == null) {
            onBindViewHolder(holder, position);
        } else {
            for (Object object : payloads) {
                if (object instanceof Integer) {
                    RecyclerView recyclerView = holder.mReplyContent;
                    if (recyclerView.getAdapter() != null) {
                        ReplyAdapter replyAdapter = (ReplyAdapter) recyclerView.getAdapter();
                        replyAdapter.notifyDataSetChanged();
                    }
                }
            }
        }

        //super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }


    public class CommentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.mheader_text)
        TextView mheaderText;
        @BindView(R.id.head_view_parent)
        RelativeLayout headViewParent;
        @BindView(R.id.comment_head)
        CircleImageView commentHead;
        @BindView(R.id.comment_name)
        TextView commentName;
        @BindView(R.id.submit_time)
        TextView submitTime;
        @BindView(R.id.add_praise_icon)
        ImageView addPraiseIcon;
        @BindView(R.id.add_praise_text)
        TextView addPraiseText;
        @BindView(R.id.mCommentContent)
        TextView mCommentContent;
        @BindView(R.id.dismissBtn)
        TextView dismissBtn;
        @BindView(R.id.mReplyContent)
        RecyclerView mReplyContent;
        @BindView(R.id.mRlcontainer)
        RelativeLayout mRlcontainer;


        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }

    public interface CommentAdapterClick {
        void addPraise(View view, String comment_id, int position, boolean hasPraised);

        /**
         * @param view
         * @param comment_id 评论id
         * @param reply_id   向评论为0，向回复回复床reply_id
         * @param reply_uid
         * @param position
         */
        void addReply(View view,
                      String comment_id, String reply_id, String reply_uid, String name, int position);
    }


}
