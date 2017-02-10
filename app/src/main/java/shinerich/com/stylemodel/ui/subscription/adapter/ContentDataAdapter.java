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
import shinerich.com.stylemodel.bean.ContentData;
import shinerich.com.stylemodel.utils.DensityUtils;
import shinerich.com.stylemodel.utils.GlideCircleTransform;

/**
 * Created by Administrator on 2016/10/17.
 */
public class ContentDataAdapter extends RecyclerView.Adapter<ContentDataAdapter.ContentDataViewHolder> {


    private Context mContext;

    private List<ContentData> data;

    private OnContentClickListener listener;

    public void setListener(OnContentClickListener listener) {
        this.listener = listener;
    }

    public ContentDataAdapter(Context mContext, List<ContentData> data) {
        this.mContext = mContext;
        this.data = data;
    }

    public ContentDataAdapter(Context mContext) {
        this.mContext = mContext;
    }


    public List<ContentData> getData() {
        return data;
    }

    @Override
    public ContentDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recommended_content, parent, false);
        return new ContentDataViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ContentDataViewHolder holder, final int position) {
        holder.mBloggerName.setText(data.get(position).getNickname());
        if (data.get(position).getType().equals(2)) {
            Glide.with(mContext).load(data.get(position).
                    getUsericon()).diskCacheStrategy(DiskCacheStrategy.SOURCE).
                    placeholder(R.drawable.subscirbe_arrangement_content).
                    error(R.drawable.subscirbe_arrangement_content).dontAnimate().
                    //transform(new GlideCircleTransform(mContext, true)).
                            into(holder.headImage);

        } else {
            Glide.with(mContext).load(data.get(position).
                    getUsericon()).diskCacheStrategy(DiskCacheStrategy.SOURCE).
                    placeholder(R.drawable.subscirbe_arrangement_content).
                    error(R.drawable.subscirbe_arrangement_content).dontAnimate().
                    transform(new GlideCircleTransform(mContext, true)).
                    into(holder.headImage);

        }

        boolean isSelected = data.get(position).getIs_select().equals("1");
        holder.selectorIcon.setSelected(isSelected);
        holder.selectorIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.OnContentClickListener(v, data.get(position), position);
                }
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


        setHeightWidth(holder.headImage);


    }


    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }


    public class ContentDataViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.head_image)
        ImageView headImage;
        @BindView(R.id.selector_icon)
        ImageView selectorIcon;
        @BindView(R.id.mBloggerName)
        TextView mBloggerName;

        public ContentDataViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnContentClickListener {
        void OnContentClickListener(View view, ContentData data, int position);

        void ItemClick(View view, ContentData data, int position);

    }


    public void setHeightWidth(ImageView imageView) {
        ViewGroup.LayoutParams param = imageView.getLayoutParams();
        if (param != null)

            param.width = DensityUtils.dip2px(mContext, 80);
        param.height = DensityUtils.dip2px(mContext, 80);
        imageView.setLayoutParams(param);


    }

}
