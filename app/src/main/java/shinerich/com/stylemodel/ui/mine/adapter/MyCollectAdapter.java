package shinerich.com.stylemodel.ui.mine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.bean.MyCollect;
import shinerich.com.stylemodel.utils.GlideUtils;
import shinerich.com.stylemodel.widget.CircleImageView;

/**
 * 搜索列表-RecyclerView.Adapter
 *
 * @author hunk
 */
public class MyCollectAdapter extends RecyclerView.Adapter<MyCollectAdapter.MyViewHolder> {

    private Context context;
    private List<MyCollect> datas;
    private GlideUtils glideUtils = GlideUtils.getInstance();
    private OnItemClickListener onItemClickListener;

    public MyCollectAdapter(Context context, List<MyCollect> datas) {
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
        View view = View.inflate(context, R.layout.item_discovery_search_list, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        MyCollect data = datas.get(position);
        //初始数据
        glideUtils.load(context, holder.iv_thumb, data.getThumb(), R.drawable.collect_img_def);
        holder.tv_title.setText(Html.fromHtml(data.getTitle()));
        glideUtils.load(context, holder.iv_cthumb, data.getCate_thumb(), R.drawable.user_img_def);
        holder.tv_cname.setText(data.getCate_name());

        //点击Item事件
        holder.rl_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, position);
                }

            }
        });

        //栏目头像
        holder.iv_cthumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onItemClickListener != null) {
                    onItemClickListener.onCateClick(v, position);
                }

            }
        });

        //栏目
        holder.tv_cname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onItemClickListener != null) {
                    onItemClickListener.onCateClick(v, position);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_thumb;
        RelativeLayout rl_item;
        TextView tv_title;
        CircleImageView iv_cthumb;
        TextView tv_cname;

        public MyViewHolder(View view) {
            super(view);
            //初始化视图
            rl_item = (RelativeLayout) view.findViewById(R.id.rl_item);
            iv_cthumb = (CircleImageView) view.findViewById(R.id.iv_cthumb);
            iv_thumb = (ImageView) view.findViewById(R.id.iv_thumb);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_cname = (TextView) view.findViewById(R.id.tv_cname);


        }

    }

    public interface OnItemClickListener {
        //Item点击
        void onItemClick(View view, int position);

        //栏目
        void onCateClick(View view, int position);
    }
}
