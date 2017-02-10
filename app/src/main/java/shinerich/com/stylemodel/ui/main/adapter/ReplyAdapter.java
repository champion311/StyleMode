package shinerich.com.stylemodel.ui.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.bean.ReplyBean;
import shinerich.com.stylemodel.bean.ReplyDetailsBean;
import shinerich.com.stylemodel.utils.GlideCircleTransform;
import shinerich.com.stylemodel.utils.HDateUtils;
import shinerich.com.stylemodel.widget.CircleImageView;

/**
 * Created by Administrator on 2016/11/1.
 */
public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder> {


    private Context mContext;

    private List<ReplyBean> data;

    private ReplyClickInterface mClick;

    public void setmClick(ReplyClickInterface mClick) {
        this.mClick = mClick;
    }

    public ReplyAdapter(Context mContext, List<ReplyBean> data) {
        this.mContext = mContext;
        this.data = data;
    }

    public void setNewData(List<ReplyBean> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();

    }


    @Override
    public ReplyAdapter.ReplyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_comment_reply, null);
        return new ReplyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReplyAdapter.ReplyViewHolder holder, final int position) {
        final ReplyBean bean = data.get(position);
        Glide.with(mContext).load(bean.getUsericon()).error(R.drawable.default_comment_head).
                diskCacheStrategy(DiskCacheStrategy.SOURCE).transform(new GlideCircleTransform(mContext))
                .into(holder.replyHead);
        holder.mName.setText(bean.getNickname());
        holder.submitTime.setText(HDateUtils.getDateTime(Long.valueOf(bean.getAddtime())));

        holder.mParentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                if (mClick != null)
                    mClick.call(v, bean, position);
                //回复回复
            }
        });

        if ("0".equals(bean.getReply_id())) {
            holder.mContent.setText(bean.getContent());
        } else {
            holder.mContent.setText(createString(bean.getReply_id(),
                    bean.getReply_nickname(), bean.getContent()));
        }

    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class ReplyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.reply_head)
        CircleImageView replyHead;
        @BindView(R.id.mName)
        TextView mName;
        @BindView(R.id.submit_time)
        TextView submitTime;
        @BindView(R.id.mContent)
        TextView mContent;
        @BindView(R.id.mParentView)
        View mParentView;


        public ReplyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ReplyClickInterface {
        void call(View view, ReplyBean bean, int innerPosition);
    }

    public SpannableString createString(String replyId, String replyName, String content) {
        String text1 = "回复 ";
        String text2 = replyName + ": ";
        String text3 = "content";

        String finalContent = text1 + text2 + text3;

        UserSpan userSpan = new UserSpan(mContext, replyId);
        SpannableString spannableString = new SpannableString(finalContent);
        int start = 3;
        int end = finalContent.indexOf(":");
        spannableString.setSpan(userSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;

    }

    public class UserSpan extends ClickableSpan {

        String userId;

        Context mContext;

        public UserSpan(Context mContext, String userId) {
            this.mContext = mContext;
            this.userId = userId;
        }

        @Override
        public void onClick(View widget) {
            //TODO 个人详情跳转到个人详情


        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(mContext.getResources().getColor(R.color.green_color));
            ds.setUnderlineText(false);

        }
    }


}
