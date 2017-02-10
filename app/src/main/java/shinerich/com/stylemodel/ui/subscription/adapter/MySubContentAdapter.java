package shinerich.com.stylemodel.ui.subscription.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.bean.ContentData;
import shinerich.com.stylemodel.bean.MySubCateBean;
import shinerich.com.stylemodel.utils.GlideUtils;

/**
 * Created by Administrator on 2016/10/28.
 */
public class MySubContentAdapter extends RecyclerView.Adapter<MySubContentAdapter.MySubCateBeanViewHolder> {

    private Context mContext;

    private List<MySubCateBean> data;

    private OnItemClickListener onItemClickListener;

    public long lastMills;

    private long currentMills;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MySubContentAdapter(Context mContext, List<MySubCateBean> data) {
        this.mContext = mContext;
        this.data = data;
    }

    public MySubContentAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public List<MySubCateBean> getData() {
        return data;
    }


    @Override
    public MySubCateBeanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_recommended_content, null);
        return new MySubCateBeanViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final MySubCateBeanViewHolder holder, final int position) {
        holder.mBloggerName.setText(data.get(position).getNickname());
//        GlideUtils.getInstance().load(mContext, holder.headImage,
//                data.get(position).getUsericon(), R.drawable.subscirbe_arrangement_content);
        Glide.with(mContext).load(data.get(position).getUsericon()).
                diskCacheStrategy(DiskCacheStrategy.SOURCE).
                placeholder(R.drawable.subscirbe_arrangement_content).
                error(R.drawable.subscirbe_arrangement_content).into(holder.headImage);
        boolean isSelected = data.get(position).getIs_select().equals("1");
        holder.selectorIcon.setSelected(isSelected);
        holder.selectorIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                currentMills = System.currentTimeMillis();
                if (currentMills - lastMills < 500) {
                    //防止短时间重复点击
                    return;
                } else {
                    lastMills = currentMills;
                }
                boolean isSelected = v.isSelected();
                //holder.selectorIcon.setSelected(!isSelected);
                if (onItemClickListener != null) {
                    onItemClickListener.OnContentItemListener(v, !isSelected, data.get(position));
                }
            }
        });
    }

    public MySubCateBean searchData(String id, String type) {
        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(type)) {
            return null;
        }

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId().equals(id) && data.get(i).getUtype().equals(type)) {
                return data.get(i);
            }
        }
        return null;
    }


    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }


    public class MySubCateBeanViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.head_image)
        ImageView headImage;
        @BindView(R.id.selector_icon)
        ImageView selectorIcon;
        @BindView(R.id.mBloggerName)
        TextView mBloggerName;

        public MySubCateBeanViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void OnContentItemListener(View view, boolean isSelected, MySubCateBean bean);
    }

}
