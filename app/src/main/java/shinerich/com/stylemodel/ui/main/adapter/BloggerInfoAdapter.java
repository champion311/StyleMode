package shinerich.com.stylemodel.ui.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import shinerich.com.stylemodel.R;

/**
 * Created by Administrator on 2016/9/19.
 */
public class BloggerInfoAdapter extends RecyclerView.Adapter<BloggerInfoAdapter.BloggerInfoViewHolder> {

    private Context mContext;


    public BloggerInfoAdapter(Context mContext) {
        this.mContext = mContext;

    }


    @Override
    public BloggerInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_blogger_info, null);
        return new BloggerInfoViewHolder(view);

    }

    @Override
    public void onBindViewHolder(BloggerInfoViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class BloggerInfoViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.mImageView)
        ImageView mImageView;
        @BindView(R.id.mIntroduce)
        TextView mIntroduce;


        public BloggerInfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this.itemView);
        }

    }
}
