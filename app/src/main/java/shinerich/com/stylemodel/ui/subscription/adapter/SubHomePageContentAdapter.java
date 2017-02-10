package shinerich.com.stylemodel.ui.subscription.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import shinerich.com.stylemodel.bean.SubHomePageContentBean;
import shinerich.com.stylemodel.utils.GlideCircleTransform;
import shinerich.com.stylemodel.utils.HDateUtils;
import shinerich.com.stylemodel.widget.CircleImageView;

/**
 * Created by Administrator on 2016/11/23.
 */
public class SubHomePageContentAdapter extends
        RecyclerView.Adapter<SubHomePageContentAdapter.SubHomePageContentHolder> {

    private Context mContext;

    private List<SubHomePageContentBean> datas;

    private OnDataClickListener listener;

    public void setListener(OnDataClickListener listener) {
        this.listener = listener;
    }

    public SubHomePageContentAdapter(Context mContext, List<SubHomePageContentBean> datas) {
        this.mContext = mContext;
        this.datas = datas;
    }

    @Override
    public SubHomePageContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_sub_content, null);
        return new SubHomePageContentHolder(view);
    }

    @Override
    public void onBindViewHolder(SubHomePageContentHolder holder, final int position) {
        if (datas.size() == 0) {
            return;
        }
        final SubHomePageContentBean dataBean = datas.get(position);
        Glide.with(mContext).load(dataBean.getUser().getUsericon()).
                diskCacheStrategy(DiskCacheStrategy.RESULT).
                transform(new GlideCircleTransform(mContext)).
                placeholder(R.drawable.default_comment_head).
                into(holder.contentProviderHead);

        holder.subCotentTime.setText
                (HDateUtils.getDate(Long.valueOf(dataBean.getArticle().getAddtime())));
        holder.mColumnName.setText(dataBean.getUser().getName());

        holder.mDescribe.setText(dataBean.getArticle().getTitle());

        Glide.with(mContext).load(dataBean.getArticle().getThumb()).
                diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.mImageView);

        holder.contentProviderHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.OnHeadClickListener(v, dataBean.getUser().getId(), dataBean.getUser().getType());
                }

            }
        });

        holder.mColumnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.OnHeadClickListener(v,
                            dataBean.getUser().getId(), dataBean.getUser().getType());
                }
            }
        });

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listener != null) {
                    listener.OnContentClickListener(v,
                            dataBean.getArticle().getId(), dataBean.getArticle().getType(),position);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return datas != null ? datas.size() : 0;
    }

    public class SubHomePageContentHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.content_provider_head)
        CircleImageView contentProviderHead;
        @BindView(R.id.mColumn_name)
        TextView mColumnName;
        @BindView(R.id.sub_cotent_time)
        TextView subCotentTime;
        @BindView(R.id.mImageView)
        ImageView mImageView;
        @BindView(R.id.mDescribe)
        TextView mDescribe;


        public SubHomePageContentHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnDataClickListener {

        void OnHeadClickListener(View view, String id, String type);

        void OnContentClickListener(View view, String id, String type,int position);

    }

}
