package shinerich.com.stylemodel.ui.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.bean.FocusImageBean;
import shinerich.com.stylemodel.ui.main.activity.ArticleContentActivity;
import shinerich.com.stylemodel.ui.main.activity.ImageListActivity;
import shinerich.com.stylemodel.ui.main.activity.VideoPlayActivity;

/**
 * Created by Administrator on 2016/10/19.
 */
public class CommonViewPageAdapter extends PagerAdapter {

    private Context mContent;

    private List<FocusImageBean> focuses;

    //private List<View> views;

    public CommonViewPageAdapter(final Context mContent, final List<FocusImageBean> focuses) {
        this.mContent = mContent;
        this.focuses = focuses;

    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        position %= focuses.size();
        if (position < 0) {
            position += focuses.size();
        }
        final FocusImageBean bean = focuses.get(position);
        ImageView view = new ImageView(mContent);
        //view.setTag(position);
        ViewParent parent = view.getParent();
        if (parent != null) {
            ViewGroup viewGroup = (ViewGroup) parent;
            viewGroup.removeView(view);
        }

        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(mContent).load(bean.getThumbnals()).
                diskCacheStrategy(DiskCacheStrategy.SOURCE).
                placeholder(R.drawable.ll_bg).
                error(R.drawable.ll_bg).into(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch (Integer.valueOf(bean.getType())) {
                    case 1:
                        intent = new Intent(mContent, ImageListActivity.class);
                        intent.putExtra("id", bean.getId());
                        intent.putExtra("type", bean.getType());
                        mContent.startActivity(intent);
                        break;
                    case 0:
                    case 2:
                    case 3:
                        intent = new Intent(mContent, ArticleContentActivity.class);
                        intent.putExtra("id", bean.getId());
                        intent.putExtra("type", bean.getType());
                        mContent.startActivity(intent);
                        break;
                }
            }
        });


        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // container.removeView(views.get(position));
        //无需再这里removeView
    }

}
