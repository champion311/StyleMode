package shinerich.com.stylemodel.ui.main.adapter;

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
import shinerich.com.stylemodel.bean.ListContentBean;

/**
 * Created by Administrator on 2016/10/24.
 */
public class ColumnAdapter extends RecyclerView.Adapter<ColumnAdapter.ColumnViewHolder> {


    private Context mContent;

    private List<ListContentBean> list;

    private ItemClickListener listener;

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public ColumnAdapter(Context mContent, List<ListContentBean> list) {
        this.mContent = mContent;
        this.list = list;
    }

    @Override
    public ColumnViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContent, R.layout.item_column_view, null);
        return new ColumnViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ColumnViewHolder holder, final int position) {
        final ListContentBean bean = list.get(position);
        holder.title.setText(bean.getTitle());
        Glide.with(mContent).load(bean.getThumbnals()).
                diskCacheStrategy(DiskCacheStrategy.ALL).
                placeholder(R.drawable.ll_bg).
                error(R.drawable.ll_bg).dontAnimate().into(holder.thumbView);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.Click(v, position, bean);
                }

            }
        });

        int mask = bean.getMask();

        switch (mask) {
            case 0:
                holder.maskView.setBackgroundResource(R.drawable.red_mask);
                break;
            case 1:
                holder.maskView.setBackgroundResource(R.drawable.yellow_mask);
                break;
            case 2:
                holder.maskView.setBackgroundResource(R.drawable.blue_mask);
                break;
            case 3:
                holder.maskView.setBackgroundResource(R.drawable.green_mask);
                break;
            case 4:
                holder.maskView.setBackgroundResource(R.drawable.nozomi_mask);
                break;
        }


    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class ColumnViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.thumb_view)
        ImageView thumbView;
        @BindView(R.id.mask_view)
        ImageView maskView;
        @BindView(R.id.title)
        TextView title;

        public ColumnViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public interface ItemClickListener {
        void Click(View view, int position, ListContentBean bean);
    }


}
