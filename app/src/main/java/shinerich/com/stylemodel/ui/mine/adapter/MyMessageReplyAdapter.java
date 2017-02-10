package shinerich.com.stylemodel.ui.mine.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.bean.MessageArticle;
import shinerich.com.stylemodel.bean.MessageComment;
import shinerich.com.stylemodel.bean.MessageReply;
import shinerich.com.stylemodel.bean.MyReply;
import shinerich.com.stylemodel.bean.UserInfo;
import shinerich.com.stylemodel.engin.LoginUserProvider;
import shinerich.com.stylemodel.utils.GlideUtils;
import shinerich.com.stylemodel.utils.HDateUtils;
import shinerich.com.stylemodel.widget.CircleImageView;

/**
 * 收到回复--RecyllerView-Adapter
 *
 * @author hunk
 */
public class MyMessageReplyAdapter extends RecyclerView.Adapter<MyMessageReplyAdapter.MyViewHolder> {


    private OnItemClickListener onItemClickListener;
    private GlideUtils glideUtils = GlideUtils.getInstance();
    private Context context;
    private List<MyReply> datas;
    private Fragment fragment;

    public MyMessageReplyAdapter(Context context, Fragment fragment, List<MyReply> datas) {
        this.context = context;
        this.datas = datas;
        this.fragment = fragment;
    }

    /**
     * 设置OnItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.onItemClickListener = mOnItemClickListener;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        MyReply data = datas.get(position);
        //回复信息
        MessageReply reply = data.getReply();
        if (reply != null) {
            glideUtils.load(context, holder.iv_reply_head, reply.getUsericon());
            holder.tv_reply_name.setText(reply.getNickname());
            if (!TextUtils.isEmpty(reply.getAddtime())) {
                String strTime = HDateUtils.getDateTime(Long.parseLong(reply.getAddtime()));
                holder.tv_reply_time.setText(strTime);
            }
            holder.tv_reply_content.setText(reply.getContent());

        }

        //评论信息
        MessageComment comment = data.getComment();
        if (comment != null) {
            UserInfo info = LoginUserProvider.getUser(context);
            if (info != null) {
                glideUtils.load(fragment, holder.iv_comment_head, info.getUsericon(), R.drawable.default_blogger_head);
            }
            holder.tv_comment_name.setText("我");

            if (!TextUtils.isEmpty(comment.getAddtime())) {
                String strTime = HDateUtils.getDateTime(Long.parseLong(comment.getAddtime()));
                holder.tv_comment_time.setText(strTime);
            }
            holder.tv_comment_content.setText(comment.getContent());
        }

        //文章信息
        MessageArticle article = data.getArticle();
        if (article != null) {
            holder.tv_article_title.setText(article.getTitle());
        }

        //设置点击事件
        holder.ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, position);
                }
            }
        });

        //回复
        holder.btn_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onReplyClick(v, position);
                }
            }
        });

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_my_message_reply, null);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        LinearLayout ll_item;
        CircleImageView iv_comment_head;
        CircleImageView iv_reply_head;
        TextView tv_reply_name;
        TextView tv_comment_name;
        TextView tv_comment_time;
        TextView tv_reply_time;
        TextView tv_comment_content;
        TextView tv_reply_content;
        TextView tv_article_title;
        Button btn_reply;
        View v_line;

        public MyViewHolder(View view) {
            super(view);

            //初始化视图

            ll_item = (LinearLayout) view.findViewById(R.id.ll_item);
            iv_comment_head = (CircleImageView) view.findViewById(R.id.iv_comment_head);
            iv_reply_head = (CircleImageView) view.findViewById(R.id.iv_reply_head);
            tv_reply_name = (TextView) view.findViewById(R.id.tv_reply_name);
            tv_comment_name = (TextView) view.findViewById(R.id.tv_comment_name);
            tv_comment_time = (TextView) view.findViewById(R.id.tv_comment_time);
            tv_reply_time = (TextView) view.findViewById(R.id.tv_reply_time);
            tv_comment_content = (TextView) view.findViewById(R.id.tv_comment_content);
            tv_reply_content = (TextView) view.findViewById(R.id.tv_reply_content);
            tv_article_title = (TextView) view.findViewById(R.id.tv_article_title);
            btn_reply = (Button) view.findViewById(R.id.btn_reply);
            v_line = view.findViewById(R.id.v_line);

        }

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onReplyClick(View view, int position);
    }
}
