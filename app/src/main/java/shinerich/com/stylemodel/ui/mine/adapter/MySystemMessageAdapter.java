package shinerich.com.stylemodel.ui.mine.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.bean.SystemMessage;
import shinerich.com.stylemodel.utils.HDateUtils;

/**
 * 系统消息-RecyllerView-Adapter
 *
 * @author hunk
 */
public class MySystemMessageAdapter extends RecyclerView.Adapter<MySystemMessageAdapter.MyViewHolder> {

    private Context context;
    private List<SystemMessage> datas;
    private OnItemClickListener onItemClickListener;
    private  Fragment fragment;


    public MySystemMessageAdapter(Context context, List<SystemMessage> datas) {
        this.context = context;
        this.datas = datas;
    }

    /**
     * 设置OnItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.onItemClickListener = mOnItemClickListener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = View.inflate(context, R.layout.item_my_message_system, null);

        return new MyViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        SystemMessage data = datas.get(position);

        holder.tv_name.setText(data.getUsername());
        holder.tv_content.setText(data.getContent());
        //设置时间
        if (!TextUtils.isEmpty(data.getAddtime())) {
            String strTime = HDateUtils.getDateTime(Long.parseLong(data.getAddtime()));
            holder.tv_time.setText(strTime);
        } else {
            holder.tv_time.setText("");
        }

        if (position == datas.size() - 1) {
            holder.v_line.setVisibility(View.GONE);
        } else {
            holder.v_line.setVisibility(View.VISIBLE);
        }
        //未读
        if ("1".equals(data.getStatus())) {
            holder.iv_dot.setVisibility(View.VISIBLE);
        } else {
            holder.iv_dot.setVisibility(View.GONE);
        }


        //设置点击事件
        holder.rl_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, position);
                }
            }
        });


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rl_item;
        TextView tv_name;
        TextView tv_time;
        TextView tv_content;
        ImageView iv_dot;
        View v_line;

        public MyViewHolder(View view) {
            super(view);

            //初始化视图
            rl_item = (RelativeLayout) view.findViewById(R.id.rl_item);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            iv_dot = (ImageView) view.findViewById(R.id.iv_dot);
            v_line = view.findViewById(R.id.v_line);

        }

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}
