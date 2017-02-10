package shinerich.com.stylemodel.ui.subscription.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.bean.ContentData;
import shinerich.com.stylemodel.widget.CircleImageView;

/**
 * Created by Administrator on 2016/10/17.
 */
public class SubContentAdapter extends RecyclerView.Adapter<SubContentAdapter.SubContentViewHolder> {


    private Context mContext;

    private List<ContentData> data;

    public SubContentAdapter(Context mContext, List<ContentData> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public SubContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_sub_content, null);
        return new SubContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SubContentViewHolder holder, int position) {
        ContentData bean=data.get(position);
        holder.mColumnName.setText(bean.getNickname());
        holder.subCotentTime.setText(bean.getAddtime());

        //TODO 待修改
       // holder.mDescribe.setText(bean.g);
    }


    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class SubContentViewHolder extends RecyclerView.ViewHolder {

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

        public SubContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
