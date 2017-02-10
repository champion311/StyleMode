package shinerich.com.stylemodel.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;

import shinerich.com.stylemodel.R;

/***
 * GlideUtils
 *
 * @author hunk
 */
public class GlideUtils {

    //全局占位图
    private int placeHolder = R.drawable.collect_img_def;
    //全局网络请求错误占位图
    private int errorHolder = R.drawable.collect_img_def;

    private boolean skipMemoryCache = false;
    //磁盘缓存策略(仅仅缓存最终的图像，即，降低分辨率后的（或者是转换后的）)
    private DiskCacheStrategy diskCacheStrategy = DiskCacheStrategy.RESULT;


    public GlideUtils() {

    }


    /**
     * 单例模式(多线程  静态内部类)
     */
    public static GlideUtils getInstance() {
        return GlideHolder.instance;
    }


    private static class GlideHolder {
        private static final GlideUtils instance = new GlideUtils();

    }

    public boolean isSkipMemoryCache() {
        return skipMemoryCache;
    }

    public void setSkipMemoryCache(boolean skipMemoryCache) {
        this.skipMemoryCache = skipMemoryCache;
    }

    public DiskCacheStrategy getDiskCacheStrategy() {
        return diskCacheStrategy;
    }

    public void setDiskCacheStrategy(DiskCacheStrategy diskCacheStrategy) {
        this.diskCacheStrategy = diskCacheStrategy;
    }

    public int getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(int placeHolder) {
        this.placeHolder = placeHolder;
    }

    public int getErrorHolder() {
        return errorHolder;
    }

    public void setErrorHolder(int errorHolder) {
        this.errorHolder = errorHolder;
    }


    /**
     * 初始化参数
     */
    private DrawableRequestBuilder initOptions(Context context, Object obj) {
        DrawableRequestBuilder<Object> objectDrawableRequestBuilder;
        objectDrawableRequestBuilder = Glide.with(context).load(obj).skipMemoryCache(skipMemoryCache);
        return objectDrawableRequestBuilder;
    }


    /**
     * 加载网络图片
     */
    public void load(Context context, ImageView imageView, String url) {
        DrawableRequestBuilder builder = initOptions(context, url);
        builder.centerCrop().diskCacheStrategy(diskCacheStrategy).crossFade().placeholder(placeHolder).error(errorHolder).into(imageView);
    }

    /**
     * 加载网络图片
     */
    public void load(Context context, ImageView imageView, String url, int errorRes) {
        DrawableRequestBuilder builder = initOptions(context, url);
        builder.centerCrop().diskCacheStrategy(diskCacheStrategy).crossFade().placeholder(errorRes).error(errorRes).into(imageView);
    }

    /**
     * 加载网络图片
     */
    public void load(Fragment fragment, ImageView imageView, String url, int errorRes) {
        Glide.with(fragment).load(url).skipMemoryCache(skipMemoryCache).
                centerCrop().diskCacheStrategy(diskCacheStrategy).crossFade().
                placeholder(errorRes).error(errorRes).into(imageView);
    }


    /**
     * 加载网络图片
     */
    public void load(Context context, ImageView imageView, String url, boolean crossFade) {
        if (crossFade) {
            load(context, imageView, url);
        } else {
            DrawableRequestBuilder builder = initOptions(context, url);
            builder.centerCrop().diskCacheStrategy(diskCacheStrategy).dontAnimate().placeholder(placeHolder).error(errorHolder).into(imageView);
        }
    }


    /**
     * 加载网络图片
     */
    public void load(Context context, String imgUrl, final OnDownLoadBitmapListener onDownLoadBitmapListener) {
        SimpleTarget target = new SimpleTarget<Bitmap>() {

            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (onDownLoadBitmapListener != null) {
                    onDownLoadBitmapListener.getBitmap(resource);
                }
            }
        };
        Glide.with(context).load(imgUrl).asBitmap().skipMemoryCache(skipMemoryCache)
                .error(errorHolder).placeholder(placeHolder).into(target);

    }

    public void loadRoundImage(Context context, String imgUrl, final OnDownLoadBitmapListener onDownLoadBitmapListener) {
        SimpleTarget target = new SimpleTarget<Bitmap>() {

            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (onDownLoadBitmapListener != null) {
                    onDownLoadBitmapListener.getBitmap(resource);
                }
            }
        };
        Glide.with(context).load(imgUrl).asBitmap().skipMemoryCache(skipMemoryCache).transform(new GlideCircleTransform(context))
                .error(errorHolder).placeholder(placeHolder).into(target);
    }


    /**
     * 加载资源
     */
    public void load(Context context, ImageView imageView, int resId) {
        DrawableRequestBuilder builder = initOptions(context, resId);
        builder.centerCrop().diskCacheStrategy(diskCacheStrategy).dontAnimate().crossFade().
                placeholder(placeHolder).error(errorHolder).into(imageView);
    }

    /**
     * 加载资源
     */
    public void load(Context context, ImageView imageView, int resId, boolean crossFade) {
        if (crossFade) {
            load(context, imageView, resId);
        } else {
            DrawableRequestBuilder builder = initOptions(context, resId);
            builder.centerCrop().diskCacheStrategy(diskCacheStrategy)
                    .dontAnimate().placeholder(placeHolder).error(errorHolder).into(imageView);
        }
    }


    /**
     * 加载Gif图片
     */
    public void loadGif(Context context, ImageView imageView, String gifUrl) {
        DrawableRequestBuilder builder = initOptions(context, gifUrl);
        Glide.with(context).load(gifUrl).asGif().skipMemoryCache(skipMemoryCache).diskCacheStrategy(diskCacheStrategy)
                .placeholder(placeHolder).error(errorHolder).into(imageView);
    }

    /**
     * 加载Gif图片(显示Gift图片第一帧)
     */
    public void loadGif(Context context, ImageView imageView, String gifUrl, boolean showFirstFrame) {
        if (!showFirstFrame) {
            loadGif(context, imageView, gifUrl);
        } else {
            Glide.with(context).load(gifUrl).asBitmap().skipMemoryCache(skipMemoryCache).diskCacheStrategy(diskCacheStrategy).
                    placeholder(placeHolder).error(errorHolder).into(imageView);
        }
    }

    /**
     * 加载本地视频缩略图
     */
    public void loadVideo(Context context, ImageView imageView, String videoPath) {
        DrawableRequestBuilder builder = initOptions(context, Uri.fromFile(new File(videoPath)));
        builder.centerCrop().diskCacheStrategy(diskCacheStrategy).placeholder(placeHolder).error(errorHolder).into(imageView);
    }


    /**
     * 清除请求
     */
    public void cleanViewRequest(View view) {

        Glide.clear(view);
    }


    /**
     * 恢复请求
     */
    public void resumeRequests(Context context) {

        Glide.with(context).resumeRequests();
    }

    /**
     * 恢复请求
     */
    public void resumeRequests(Fragment fragment) {

        Glide.with(fragment).resumeRequests();
    }

    /**
     * 暂定请求
     */
    public void pauseRequests(Context context) {

        Glide.with(context).pauseRequests();
    }

    /**
     * 暂定请求
     */
    public void pauseRequests(Fragment fragment) {

        Glide.with(fragment).pauseRequests();
    }

    /**
     * 获取Bitmap回调接口
     */
    public interface OnDownLoadBitmapListener {
        public void getBitmap(Bitmap bitmap);
    }

}
