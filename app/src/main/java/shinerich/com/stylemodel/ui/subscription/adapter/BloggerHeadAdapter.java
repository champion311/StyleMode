package shinerich.com.stylemodel.ui.subscription.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import shinerich.com.stylemodel.bean.BloggerDetailBean;
import shinerich.com.stylemodel.utils.GlideCircleTransform;

/**
 * Created by Administrator on 2016/10/17.
 */
public class BloggerHeadAdapter extends RecyclerView.Adapter<BloggerHeadAdapter.BloggerHeadHolder> {


    private List<BloggerDetailBean> data;

    private Context mContent;

    private OnBloggerHeadClickListener listener;

    public void setListener(OnBloggerHeadClickListener listener) {
        this.listener = listener;
    }

    public BloggerHeadAdapter(Context mContent, List<BloggerDetailBean> data) {
        this.mContent = mContent;
        this.data = data;
    }

    public List<BloggerDetailBean> getData() {
        return data;
    }

    @Override
    public BloggerHeadHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isUseBigView) {
            View view = LayoutInflater.from(mContent).inflate(R.layout.item_hot_bloggers, parent, false);
            return new BloggerHeadHolder(view);
        } else {
            View view = LayoutInflater.from(mContent).inflate(R.layout.item_hot_bloggers_small, parent, false);
            return new BloggerHeadHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(final BloggerHeadHolder holder, final int position) {

        Glide.with(mContent).load(data.get(position).
                getUsericon()).diskCacheStrategy(DiskCacheStrategy.SOURCE).
                transform(new GlideCircleTransform(mContent)).
                placeholder(R.drawable.subscribe_arrangement_bloggerhead).
                error(R.drawable.subscribe_arrangement_bloggerhead).into(holder.mImageView);
        holder.mBloggerName.setText(data.get(position).getNickname());
        boolean isSelected = "1".equals(data.get(position).getIs_select());
        holder.selectorIcon.setSelected(isSelected);
        holder.selectorIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.OnClick(v, data.get(position), position);

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.ItemClick(v, data.get(position), position);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class BloggerHeadHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.selector_icon)
        ImageView selectorIcon;
        @BindView(R.id.mBloggerName)
        TextView mBloggerName;
        @BindView(R.id.mImageView)
        ImageView mImageView;

        public BloggerHeadHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnBloggerHeadClickListener {
        void OnClick(View view, BloggerDetailBean detailBean, int position);

        void ItemClick(View view, BloggerDetailBean detailBean, int position);
    }

    public void setHeightWidth(ImageView imageView) {
        ViewGroup.LayoutParams param = imageView.getLayoutParams();
        param.width = ViewGroup.LayoutParams.MATCH_PARENT;
        param.height = ViewGroup.LayoutParams.MATCH_PARENT;
        imageView.setLayoutParams(param);


    }


    public boolean isUseBigView = false;

    public void setUseBigView(boolean useBigView) {
        isUseBigView = useBigView;
    }
}
