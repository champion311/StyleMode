package shinerich.com.stylemodel.ui.mine.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.bean.MyComment;
import shinerich.com.stylemodel.utils.GlideUtils;
import shinerich.com.stylemodel.utils.HDateUtils;
import shinerich.com.stylemodel.widget.CircleImageView;

/**
 * 我的评论-RecyllerView-Adapter
 *
 * @author hunk
 */
public class MyMessageCommentAdapter extends RecyclerView.Adapter<MyMessageCommentAdapter.MyViewHolder> {

    private OnItemClickListener onItemClickListener;
    private GlideUtils glideUtils = GlideUtils.getInstance();
    private Context context;
    private List<MyComment> datas;
    private Fragment fragment;

    public MyMessageCommentAdapter(Context context, Fragment fragment, List<MyComment> datas) {
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

        MyComment data = datas.get(position);
        //初始数据
        if (data.getUserinfo() != null) {
            glideUtils.load(fragment, holder.iv_user_head, data.getUserinfo().getUsericon(),
                    R.drawable.default_blogger_head);
            holder.tv_name.setText(data.getUserinfo().getNickname());
            if (!TextUtils.isEmpty(data.getUserinfo().getAddtime())) {
                String strTime = HDateUtils.getDateTime(Long.parseLong(data.getUserinfo().getAddtime()));
                holder.tv_time.setText(strTime);
            }
        }


        holder.tv_content.setText(data.getCommentinfo().getContent());
        holder.tv_article_title.setText(data.getArticleinfo().getTitle());
        if (position == datas.size() - 1) {
            holder.v_line.setVisibility(View.GONE);
        } else {
            holder.v_line.setVisibility(View.VISIBLE);
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

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_my_message_comment, null);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        LinearLayout ll_item;
        CircleImageView iv_user_head;
        TextView tv_name;
        TextView tv_time;
        TextView tv_content;
        TextView tv_article_title;
        View v_line;

        public MyViewHolder(View view) {
            super(view);

            //初始化视图
            ll_item = (LinearLayout) view.findViewById(R.id.ll_item);
            iv_user_head = (CircleImageView) view.findViewById(R.id.iv_user_head);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            tv_article_title = (TextView) view.findViewById(R.id.tv_article_title);
            v_line = view.findViewById(R.id.v_line);

        }

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}
