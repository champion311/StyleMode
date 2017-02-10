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
import shinerich.com.stylemodel.bean.BloggerInfoBean;
import shinerich.com.stylemodel.bean.ContentData;
import shinerich.com.stylemodel.bean.RecommendData;
import shinerich.com.stylemodel.utils.GlideCircleTransform;
import shinerich.com.stylemodel.utils.GlideUtils;

/**
 * Created by Administrator on 2016/10/27.
 */
public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.RecommendViewHolder> {

    private Context mContext;

    private List<RecommendData> data;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    private OnItemClick onItemClick;

    public RecommendAdapter(Context mContext, List<RecommendData> data) {
        this.mContext = mContext;
        this.data = data;
    }

    public List<RecommendData> getData() {
        return data;
    }


    public RecommendAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public int getDataPos(String id, String type) {
        if (TextUtils.isEmpty(id)) {
            return -1;
        }
        for (int i = 0; i < data.size(); i++) {
            if (id.equals(data.get(i).getId()) && type.equals(data.get(i).getType())) {
                return i;
            }
        }
        return -1;

    }


    @Override
    public RecommendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_recommended_content, null);
        return new RecommendViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final RecommendViewHolder holder, final int position) {
        holder.mBloggerName.setText(data.get(position).getNickname());

//        GlideUtils.getInstance().load(mContext, holder.headImage,
//                data.get(position).getUsericon(), R.drawable.subscirbe_arrangement_content);
        Glide.with(mContext).load(data.get(position).getUsericon()).
                diskCacheStrategy(DiskCacheStrategy.SOURCE).
                transform(new GlideCircleTransform(mContext, true)).into(holder.headImage);
        boolean isSelected = data.get(position).getIs_select().equals("1");
        holder.selectorIcon.setSelected(isSelected);
        holder.selectorIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean isSelected = v.isSelected();
                //holder.selectorIcon.setSelected(!isSelected);
                if (onItemClick != null)
                    onItemClick.click(v, !isSelected, data.get(position), position);

                //订阅栏目操作
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick != null) {
                    onItemClick.allReClick(data.get(position).getId(), data.get(position).getType());
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }


    public class RecommendViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.head_image)
        ImageView headImage;
        @BindView(R.id.selector_icon)
        ImageView selectorIcon;
        @BindView(R.id.mBloggerName)
        TextView mBloggerName;

        public RecommendViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClick {

        void click(View view, boolean isSelected, RecommendData bean, int position);

        void allReClick(String id, String type);
    }
}
