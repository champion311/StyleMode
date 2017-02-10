package shinerich.com.stylemodel.utils;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;

import java.io.File;

import shinerich.com.stylemodel.common.GloableValues;


public class GlideConfiguration implements GlideModule {


    @Override
    public void applyOptions(Context context, GlideBuilder builder) {


        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();
        builder.setMemoryCache(new LruResourceCache(defaultMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(defaultBitmapPoolSize));
        //可以修改为ARGB_8888调整代销
        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);
        //设置缓存路径
        String chachePath = SDCardUtils.getPath() + File.separator + "stylemode";
        File chacheDic = FileUtils.createDir(GloableValues.BASE_PATH + File.separator + "chache");
        //100M
        int cacheSize100MegaBytes = 100 * 1024 * 1024;
        builder.setDiskCache(new DiskLruCacheFactory(chacheDic.getAbsolutePath(), cacheSize100MegaBytes));
        //builder.setDiskCache(new InternalCacheDiskCacheFactory(context, 250 * 1024 * 1024));

    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        //glide.register(GlideConfiguration.class);


    }


//    //设置默认和出错时的图片
//    Glide.with(this).load(url).placeholder(resId).error(resId).into(mImageView)
////普通的图片加载
//    Glide.with(this).load(url).into(mImageView);
////可理解为加载动态图的第一帧的Bitmap,比如Gif
//    Glide.with(this).load(url).asBitmap().into(imageView);
////GIF加载，URL指向的资源必须是gif，如果是普通图片则不显示。
////相反，如果指向正确但没有执行asGif方法，则只是作为普通图片展示
//    Glide.with(this).asGif().load(url).into(mImageView)；
//            //缩略图的加载
//            Glide.with(yourFragment).load(yourUrl).thumbnail(0.1f).into(yourView)
}
