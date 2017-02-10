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
import shinerich.com.stylemodel.utils.DensityUtils;
import shinerich.com.stylemodel.utils.GlideCircleTransform;

/**
 * Created by Administrator on 2016/10/18.
 */
public class HotBloggerAdapter extends RecyclerView.Adapter<HotBloggerAdapter.HotBloggerViewHolder> {


    private Context mContext;

    private List<BloggerInfoBean> bloggerInfoBeen;
    private boolean isHideIcon = false;

    public void setHideIcon(boolean isHideIcon) {
        this.isHideIcon = isHideIcon;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    private OnItemClick onItemClick;


    public List<BloggerInfoBean> getBloggerInfoBeen() {
        return bloggerInfoBeen;
    }

    public HotBloggerAdapter(Context mContext, List<BloggerInfoBean> bloggerInfoBeen) {
        this.mContext = mContext;
        this.bloggerInfoBeen = bloggerInfoBeen;
    }

    public int getPosition(String id) {
        if (TextUtils.isEmpty(id)) {
            return -1;
        }
        for (int i = 0; i < bloggerInfoBeen.size(); i++) {
            if (id.equals(bloggerInfoBeen.get(i).getId())) {
                return i;
            }
        }
        return -1;

    }


    @Override
    public HotBloggerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isUseBigView) {
            View view = View.inflate(mContext, R.layout.item_hot_bloggers, null);
            return new HotBloggerViewHolder(view);
        } else {
            View view = View.inflate(mContext, R.layout.item_hot_bloggers_small, null);
            return new HotBloggerViewHolder(view);
        }


    }


    @Override
    public void onBindViewHolder(HotBloggerViewHolder holder, final int position) {
        final BloggerInfoBean bean = bloggerInfoBeen.get(position);
        boolean isSelected = "1".equals(bean.getIs_select());

        Glide.with(mContext).load(bean.getUsericon()).diskCacheStrategy
                (DiskCacheStrategy.SOURCE).bitmapTransform(new GlideCircleTransform(mContext)).error
                (R.drawable.default_blogger_head).into(holder.mImageView);
        holder.mBloggerName.setText(bean.getNickname());
        holder.selectorIcon.setSelected(isSelected);
        holder.selectorIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSelected = v.isSelected();
                //v.setSelected(!isSelected);
                if (onItemClick != null)
                    onItemClick.click(v, !isSelected, bean, position);
                return;

            }
        });
        //setHeightWidth(holder.mImageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onItemClick != null) {
                    onItemClick.allClick(bean);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return bloggerInfoBeen != null ? bloggerInfoBeen.size() : 0;
    }


    public class HotBloggerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.selector_icon)
        ImageView selectorIcon;
        @BindView(R.id.mBloggerName)
        TextView mBloggerName;
        @BindView(R.id.mImageView)
        ImageView mImageView;

        public HotBloggerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (isHideIcon) {
                selectorIcon.setVisibility(View.GONE);
            } else {
                selectorIcon.setVisibility(View.VISIBLE);
            }
        }
    }

    public interface OnItemClick {
        void click(View view, boolean isSelected, BloggerInfoBean bean, int position);

        void allClick(BloggerInfoBean bloggerInfoBean);
    }


    public void setHeightWidth(ImageView imageView) {
        ViewGroup.LayoutParams param = imageView.getLayoutParams();
        param.width = DensityUtils.dip2px(mContext, 60);
        param.height = DensityUtils.dip2px(mContext, 60);
        imageView.setLayoutParams(param);


    }

    private boolean isUseBigView = false;


    public void setUseBigView(boolean useBigView) {
        isUseBigView = useBigView;
    }


}
