package shinerich.com.stylemodel.ui.subscription.adapter;

import android.content.Context;
import android.content.SyncStats;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.bean.BloggerDetailBean;
import shinerich.com.stylemodel.bean.MySubBloggerBean;
import shinerich.com.stylemodel.utils.GlideCircleTransform;

/**
 * Created by Administrator on 2016/10/28.
 */
public class MySubBloggersAdapter extends RecyclerView.Adapter<MySubBloggersAdapter.MySubBloggersViewHolder> {

    private List<MySubBloggerBean> data;



    private Context mContent;

    private OnItemClick onItemClick;

    public long lastMills;

    private long currentMills;

    private boolean isHideExtraView = true;

    public boolean isHideExtraView() {
        return isHideExtraView;
    }

    public void setHideExtraView(boolean hideExtraView) {
        isHideExtraView = hideExtraView;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public MySubBloggersAdapter(Context mContent, List<MySubBloggerBean> data) {
        this.mContent = mContent;
        this.data = data;
    }


    public List<MySubBloggerBean> getData() {
        return data;
    }

    public MySubBloggerBean searchData(String id) {
        if (TextUtils.isEmpty(id)) {
            return null;
        }
        for (int i = 0; i < data.size(); i++) {
            if (id.equals(data.get(i).getId())) {
                return data.get(i);
            }
        }
        return null;
    }


    @Override
    public MySubBloggersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContent, R.layout.item_hot_bloggers, null);
        return new MySubBloggersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MySubBloggersViewHolder holder, final int position) {
        //holder.setVisible();
        final boolean isSelected = data.get(position).isSelected();
        Glide.with(mContent).load(data.get(position).
                getUsericon()).transform(new GlideCircleTransform(mContent)).
                placeholder(R.drawable.subscribe_arrangement_bloggerhead).
                error(R.drawable.subscribe_arrangement_bloggerhead).into(holder.mImageView);
        holder.mBloggerName.setText(data.get(position).getNickname());
        holder.selectorIcon.setSelected(isSelected);
        holder.selectorIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //避免重复的点击
                currentMills = System.currentTimeMillis();
                if (currentMills - lastMills < 500) {
                    //防止短时间重复点击
                    return;
                } else {
                    lastMills = currentMills;
                }
                //holder.selectorIcon.setSelected(!isSelected);
                if (onItemClick != null) {
                    onItemClick.onBolggerItemClick(v, !isSelected, data.get(position));
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;

    }

    public class MySubBloggersViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.selector_icon)
        ImageView selectorIcon;
        @BindView(R.id.mBloggerName)
        TextView mBloggerName;
        @BindView(R.id.mImageView)
        ImageView mImageView;
        @BindView(R.id.mParentView)
        LinearLayout mParentView;

        public MySubBloggersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            // setVisible();
        }


    }


    public interface OnItemClick {
        void onBolggerItemClick(View view, boolean isSelected, MySubBloggerBean bean);
    }



}
