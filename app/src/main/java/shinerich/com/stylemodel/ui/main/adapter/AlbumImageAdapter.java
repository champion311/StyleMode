package shinerich.com.stylemodel.ui.main.adapter;

import android.content.Context;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.List;

import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.bean.FocusImageBean;
import shinerich.com.stylemodel.bean.ImageHideEvent;
import shinerich.com.stylemodel.bean.ImageListBean;
import shinerich.com.stylemodel.utils.RxBus;
import shinerich.com.stylemodel.utils.ToastUtils;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2016/11/15.
 */
public class AlbumImageAdapter extends PagerAdapter {

    private Context mContent;

    private List<ImageListBean> focuses;

    private List<View> views;

    private MatrixChangedListener listener;

    public void setListener(MatrixChangedListener listener) {
        this.listener = listener;
    }

    public AlbumImageAdapter(final Context mContent, List<ImageListBean> focuses) {
        this.mContent = mContent;
        this.focuses = focuses;
        views = new ArrayList<>();
        if (focuses != null && focuses.size() > 0) {
            for (ImageListBean bean : focuses) {
                View parentView = View.inflate(mContent, R.layout.item_image_loading, null);
                final ImageView view = (ImageView) parentView.findViewById(R.id.mImageView);
                final ProgressBar mProgressbar = (ProgressBar) parentView.findViewById(R.id.mProgressBar);
                view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                final PhotoViewAttacher mAttacher = new PhotoViewAttacher(view);
                Glide.with(mContent).load(bean.getThumb_origin()).
                        diskCacheStrategy(DiskCacheStrategy.SOURCE).
                        placeholder(R.drawable.ll_bg).error(R.drawable.ll_bg)
                        .into(new SimpleTarget<GlideDrawable>() {
                                  @Override
                                  public void onResourceReady(GlideDrawable resource,
                                                              GlideAnimation<? super GlideDrawable> glideAnimation) {
                                      view.setImageDrawable(resource);
                                      mProgressbar.setVisibility(View.GONE);
                                      mAttacher.update();
                                  }

                                  @Override
                                  public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                      super.onLoadFailed(e, errorDrawable);
                                      mProgressbar.setVisibility(View.GONE);
                                  }
                              }
                        );
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                    @Override
                    public void onPhotoTap(View view, float x, float y) {
                        //ToastUtils.show(mContent, "tapped");
                        ImageHideEvent event = new ImageHideEvent();
                        event.setShowView(false);
                        RxBus.getInstance().post(event);
                    }

                    @Override
                    public void onOutsidePhotoTap() {

                    }
                });
//                mAttacher.setOnMatrixChangeListener(new PhotoViewAttacher.OnMatrixChangedListener() {
//                    @Override
//                    public void onMatrixChanged(RectF rect) {
//                        if (listener != null) {
//                            listener.changedCall();
//                        }
//                    }
//                });
                views.add(parentView);
            }
        }
    }

    @Override
    public int getCount() {
        return focuses != null ? focuses.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewPager.LayoutParams params = new ViewPager.LayoutParams();
        params.gravity = Gravity.CENTER;
        container.addView(views.get(position), params);
        return views.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    public interface MatrixChangedListener {
        void changedCall();
    }

}
