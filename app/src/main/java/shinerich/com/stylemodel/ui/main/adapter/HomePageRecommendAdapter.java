package shinerich.com.stylemodel.ui.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.bean.ListContentBean;
import shinerich.com.stylemodel.ui.main.activity.ArticleContentActivity;
import shinerich.com.stylemodel.ui.main.activity.ColumnActivity;
import shinerich.com.stylemodel.ui.main.activity.ImageListActivity;

/**
 * Created by Administrator on 2016/10/19.
 */
public class HomePageRecommendAdapter extends RecyclerView.Adapter<HomePageRecommendAdapter.RecommendHolder> {


    private List<ListContentBean> data;

    private Context mContext;

    private CommonInfoInterface mInterface;

    Fragment mFragment;

    public void setFragment(Fragment mFragment) {
        this.mFragment = mFragment;
    }

    public HomePageRecommendAdapter(Context mContext, List<ListContentBean> data) {

        this.mContext = mContext;
        this.data = data;
    }

    public void setmInterface(CommonInfoInterface mInterface) {
        this.mInterface = mInterface;
    }

    @Override
    public RecommendHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_home_page_recommend, null);
        return new RecommendHolder(view);
    }

    @Override
    public void onBindViewHolder(RecommendHolder holder, final int position) {
        final ListContentBean bean = data.get(position);
        holder.mTitle.setText(bean.getTitle());
        holder.mColumnName.setText(bean.getCate_name());
        int type = data.get(position).getType();
        if (type == 2) {
            holder.videoTag.setVisibility(View.VISIBLE);
        } else if (type == 1) {
            holder.albumTag.setVisibility(View.VISIBLE);
        } else {
            holder.videoTag.setVisibility(View.GONE);
            holder.albumTag.setVisibility(View.GONE);
        }
        //使用lamba表达式进行替换
        holder.mColumnName.setOnClickListener(view -> toColumnActivity(bean));
//        holder.mParentView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //TODO
//                //跳转到内容
//                //0"//类型 0文章1图集2视频3博文
//                if (mInterface != null) {
//                    mInterface.infoClick(bean, position);
//                }
//
//            }
//        });
        //           int type = bean.getType();
//                if (type != 1) {
//                    Intent intent = new Intent(mContext, ArticleContentActivity.class);
//                    intent.putExtra("id", bean.getId());
//                    intent.putExtra("type", String.valueOf(bean.getType()));
//                    mContext.startActivity(intent);
//                } else if (type == 1) {
//                    Intent intent = new Intent(mContext, ImageListActivity.class);
//                    intent.putExtra("id", bean.getId());
//                    intent.putExtra("type", String.valueOf(bean.getType()));
//                    mContext.startActivity(intent);
//                }

        if (mInterface != null) {
            holder.mParentView.setOnClickListener(view -> mInterface.infoClick(bean, position));
        }

        int mask = data.get(position).getMask();
        switch (mask) {
            case 0:
                holder.maskView.setImageResource(R.drawable.red_mask);
                break;
            case 1:
                holder.maskView.setImageResource(R.drawable.yellow_mask);
                break;
            case 2:
                holder.maskView.setImageResource(R.drawable.blue_mask);
                break;
            case 3:
                holder.maskView.setImageResource(R.drawable.green_mask);
                break;
            case 4:
                holder.maskView.setImageResource(R.drawable.nozomi_mask);
                break;
        }

        Glide.with(mContext).load(data.get(position).
                getThumbnals()).diskCacheStrategy(DiskCacheStrategy.ALL).
                placeholder(R.drawable.ll_bg).into(holder.showedView);


    }

    void toColumnActivity(ListContentBean bean) {
        Intent intent = new Intent(mContext, ColumnActivity.class);
        intent.putExtra("cate_id", bean.getCate_second_id());
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class RecommendHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.mColumn_name)
        TextView mColumnName;
        @BindView(R.id.mTitle)
        TextView mTitle;
        @BindView(R.id.mask_view)
        ImageView maskView;

        @BindView(R.id.video_tag)
        ImageView videoTag;
        @BindView(R.id.album_tag)
        ImageView albumTag;
        @BindView(R.id.mParentView)
        FrameLayout mParentView;
        @BindView(R.id.showed_view)
        ImageView showedView;


        public RecommendHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface CommonInfoInterface {
        void infoClick(ListContentBean bean, int position);
    }


}
