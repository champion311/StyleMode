package shinerich.com.stylemodel.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.BaseCacheStuffer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.util.AndroidUtils;
import master.flame.danmaku.danmaku.util.SystemClock;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.bean.Danmu;

/**
 * Created by Administrator on 2016/11/7.
 */
public class DanmuController {

    public static String TAG = DanmuController.class.getSimpleName();

    //弹幕显示的时间(如果是list的话，会 * i)，记得加上mDanmakuView.getCurrentTime()
    private static final long ADD_DANMU_TIME = 2000;
//
//    private static final int PINK_COLOR = 0xffff5a93;//粉红 楼主
//    private static final int ORANGE_COLOR = 0xffff815a;//橙色 我
//    private static final int BLACK_COLOR = 0xb2000000;//黑色 普通

    //这两个用来控制两行弹幕之间的间距
    private int DANMU_PADDING = 8;
    private int DANMU_PADDING_INNER = 10;
    private int DANMU_RADIUS = 22;//圆角半径

    public int BITMAP_WIDTH = 22;

    public int BITMAP_HEIGHT = 22;

    public float DANMU_TEXT_SIZE = 13f;

    public float MEASURE_HEIGHT = 44f;


    public OnDanmuClickListener onDanmuClickListener;

    public void setOnDanmuClicklistener(OnDanmuClickListener onDanmuClickListener) {
        this.onDanmuClickListener = onDanmuClickListener;
    }


    private Context mContext;
    private IDanmakuView mDanmakuView;
    private DanmakuContext mDanmakuContext;
    private List<Danmu> danmus;
    private Subscription mainSub;

    public DanmuController(Context mContext) {
        this.mContext = mContext;
        setValue(mContext);
        initDanmuConfig();

    }

    public void setDanmus(List<Danmu> danmus) {
        this.danmus = danmus;
    }

    private Handler mHandler;

    public void setmHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }

    public void setValue(Context mContext) {
        BITMAP_WIDTH = DpOrSp2PxUtil.dp2pxConvertInt(mContext, BITMAP_HEIGHT);
        BITMAP_HEIGHT = DpOrSp2PxUtil.dp2pxConvertInt(mContext, BITMAP_HEIGHT);
//        EMOJI_SIZE = DpOrSp2PxUtil.dp2pxConvertInt(context, EMOJI_SIZE);
        DANMU_PADDING = DpOrSp2PxUtil.dp2pxConvertInt(mContext, DANMU_PADDING);
        DANMU_PADDING_INNER = DpOrSp2PxUtil.dp2pxConvertInt(mContext, DANMU_PADDING_INNER);
        DANMU_RADIUS = DpOrSp2PxUtil.dp2pxConvertInt(mContext, DANMU_RADIUS);
        DANMU_TEXT_SIZE = DpOrSp2PxUtil.sp2px(mContext, DANMU_TEXT_SIZE);

    }


    /**
     * 初始化配置
     */
    private void initDanmuConfig() {
        // 设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<Integer, Integer>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 5); // 滚动弹幕最大显示5行
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);

        mDanmakuContext = DanmakuContext.create();
        mDanmakuContext
                .setDanmakuStyle(IDisplayer.DANMAKU_STYLE_NONE)
                .setDuplicateMergingEnabled(false)
                .setScrollSpeedFactor(1.2f)//越大速度越慢
                .setScaleTextSize(1.2f)
                .setCacheStuffer(new BackgroundCacheStuffer(), null)
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair);
    }

    private BaseCacheStuffer.Proxy mCacheStufferAdapter = new BaseCacheStuffer.Proxy() {

        @Override
        public void prepareDrawing(final BaseDanmaku danmaku, boolean fromWorkerThread) {
            if (danmaku.text instanceof Spanned) { // 根据你的条件检查是否需要需要更新弹幕
                Log.d("Tag", danmaku.index + "");
            }


        }


        @Override
        public void releaseResource(BaseDanmaku danmaku) {
            // TODO 重要:清理含有ImageSpan的text中的一些占用内存的资源 例如drawable
//            if (danmaku.text instanceof Spanned) {
//                danmaku.text = "";
//            }
        }
    };


    /**
     * 绘制背景(自定义弹幕样式)
     */
    private class BackgroundCacheStuffer extends SpannedCacheStuffer {
        // 通过扩展SimpleTextCacheStuffer或SpannedCacheStuffer个性化你的弹幕样式
        final Paint paint = new Paint();

        @Override
        public void measure(BaseDanmaku danmaku, TextPaint paint, boolean fromWorkerThread) {
            danmaku.padding = 20;  // 在背景绘制模式下增加padding
            super.measure(danmaku, paint, fromWorkerThread);

        }

        @Override
        public void drawBackground(BaseDanmaku danmaku, Canvas canvas, float left, float top) {
            paint.setAntiAlias(true);
//            paint.setColor(mContext.getResources().getColor(R.color.dan_mu_bg));
//            //danmaku.paintHeight = DensityUtils.dip2px(mContext, 22);
//            //canvas.translate(0, 100);
//            //canvas.setBitmap(getDefaultBitmap(R.drawable.danmu_slide_bg));
//            float finalTop = danmaku.paintHeight / 2 - DensityUtils.dip2px(mContext, 22) / 2;
//            float finalBottom = danmaku.paintHeight / 2 + DensityUtils.dip2px(mContext, 22) / 2;
////            canvas.drawRoundRect(new RectF(left + DANMU_PADDING_INNER, top + DANMU_PADDING_INNER
////                            , left + danmaku.paintWidth,
////                            top + danmaku.paintHeight),
////                    danmaku.paintHeight / 2, danmaku.paintHeight / 2, paint);
//            canvas.drawRoundRect(new RectF(left + DANMU_PADDING_INNER, finalTop + DANMU_PADDING_INNER
//                            , left + danmaku.paintWidth,
//                            finalBottom),
//                    2 * DensityUtils.dip2px(mContext, 22), 2 * DensityUtils.dip2px(mContext, 22), paint);
//            Log.d(TAG, "height=" + danmaku.paintHeight + " width=" + danmaku.paintWidth);
            canvas.drawRoundRect(new RectF(left + DANMU_PADDING_INNER - 15, top + DANMU_PADDING_INNER + 3
                            , left + danmaku.paintWidth - DANMU_PADDING_INNER + 25,
                            top + danmaku.paintHeight - DANMU_PADDING_INNER + 15),//+6 主要是底部被截得太厉害了，+6是增加padding的效果
                    DANMU_RADIUS, DANMU_RADIUS, paint);

        }

        @Override
        public void drawStroke(BaseDanmaku
                                       danmaku, String lineText, Canvas canvas, float left, float top, Paint paint) {
            // 禁用描边绘制
        }
    }


    public void setDanmakuView(IDanmakuView danmakuView) {
        this.mDanmakuView = danmakuView;
        initDanmuView();
    }

    private void initDanmuView() {
        if (mDanmakuView != null) {
            mDanmakuView.setCallback(new DrawHandler.Callback() {
                @Override
                public void prepared() {
                    mDanmakuView.start();
                }

                @Override
                public void updateTimer(DanmakuTimer timer) {

                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {

                }

                @Override
                public void drawingFinished() {

                }
            });
        }

        mDanmakuView.prepare(new BaseDanmakuParser() {

            @Override
            protected Danmakus parse() {
                return new Danmakus();
            }
        }, mDanmakuContext);
        mDanmakuView.enableDanmakuDrawingCache(true);
    }

    public void pause() {
        if (mDanmakuView != null && mDanmakuView.isPrepared()) {
            mDanmakuView.pause();
        }
    }

    public void hide() {
        if (mDanmakuView != null) {
            mDanmakuView.hide();
        }
    }

    public void show() {
        if (mDanmakuView != null) {
            mDanmakuView.show();
        }
    }

    public void resume() {
        if (mDanmakuView != null && mDanmakuView.isPrepared() && mDanmakuView.isPaused()) {
            mDanmakuView.resume();
        }
    }

    public void destroy() {
        if (mDanmakuView != null) {
            mDanmakuView.release();
            mDanmakuView = null;
            if (mainSub != null) {
                mainSub.unsubscribe();
            }

        }
    }


    @Deprecated
    public void addDanmuList(List<Danmu> danmuLists) {

        for (int i = 0; i < danmuLists.size(); i++) {

            try {
                Thread.sleep(50);
                addDanmu(danmuLists.get(i), i);
            } catch (InterruptedException e) {
                Log.d("Tag", e.toString());
                e.printStackTrace();
            }
        }
    }


    public void addDanmuListTest(List<Danmu> danmuLists) {
//        ArrayList<Danmu> testData = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            testData.addAll(danmuLists);
//        }

//        for (Danmu danmu : testData) {
//            loadDrawable(danmu, testData.indexOf(danmu));
//        }


        for (int i = 0; i < danmuLists.size() * 100; i++) {
            loadDrawable(danmuLists.get(i % danmuLists.size()), i);
        }

    }


    public void loadDrawable(final Danmu danmu, final int i) {
        Glide.with(mContext).load(danmu.getUsericon()).asBitmap().
                transform(new GlideCircleTransform(mContext)).
                diskCacheStrategy(DiskCacheStrategy.RESULT).
                placeholder(R.drawable.default_comment_head).
                into(new SimpleTarget<Bitmap>() {

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        //Drawable drawable = new BitmapDrawable(resource);
                        CirCleDrawableT cirCleDrawable = new CirCleDrawableT(resource);
                        cirCleDrawable.setBounds(0, 0, BITMAP_WIDTH, BITMAP_HEIGHT);
                        cirCleDrawable.setBITMAP_WIDTH(BITMAP_WIDTH);
                        cirCleDrawable.setBITMAP_HEIGHT(BITMAP_HEIGHT);
                        addDanmuTest(danmu, i, cirCleDrawable);

//                        Drawable drawable = mContext.getResources().getDrawable(R.drawable.default_danmu_head);
//                        drawable.setBounds(0, 0, BITMAP_WIDTH, BITMAP_HEIGHT);
//                        addDanmuTest(danmu, i, drawable);

                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        Drawable drawable = mContext.getResources().getDrawable(R.drawable.default_danmu_head);
                        drawable.setBounds(0, 0, BITMAP_WIDTH, BITMAP_HEIGHT);
                        addDanmuTest(danmu, i, drawable);
                    }


                });
    }

    public void addDanmuTest(final Danmu danmu, int i, Drawable drawable) {
        final BaseDanmaku danmaku = mDanmakuContext.mDanmakuFactory.
                createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);

        if (danmaku == null || mDanmakuView == null) {
            return;
        }

        danmaku.userId = danmu.user_id;
        danmaku.padding = DANMU_PADDING;
        danmaku.priority = 1;  // 1:一定会显示, 一般用于本机发送的弹幕,但会导致行数的限制失效
        danmaku.isLive = false;
        if (i == -1) {
            i = 0;
        }
        danmaku.time = mDanmakuView.getCurrentTime() + (i * ADD_DANMU_TIME);
        danmaku.textSize = DANMU_TEXT_SIZE/* * (mDanmakuContext.getDisplayer().getDensity() - 0.6f)*/;
        danmaku.textColor = Color.WHITE;
        danmaku.textShadowColor = 0;
        final String content = danmu.getContent();
        SpannableStringBuilder spannable =
                createSpannable(drawable, content);
        danmaku.text = spannable;
        mDanmakuView.addDanmaku(danmaku);

        // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
        mDanmakuView.setOnDanmakuClickListener(new IDanmakuView.OnDanmakuClickListener() {
            @Override
            public void onDanmakuClick(BaseDanmaku latest) {
                if (onDanmuClickListener != null)
                    onDanmuClickListener.onDanmuClick(latest);
            }

            @Override
            public void onDanmakuClick(IDanmakus danmakus) {


            }
        });

    }


    public void addDanmu(final Danmu danmu, int i) {
        final BaseDanmaku danmaku = mDanmakuContext.mDanmakuFactory.
                createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);

        if (danmaku == null || mDanmakuView == null) {
            return;
        }

        danmaku.userId = danmu.user_id;
        danmaku.padding = DANMU_PADDING;
        danmaku.priority = 1;  // 1:一定会显示, 一般用于本机发送的弹幕,但会导致行数的限制失效
        danmaku.isLive = false;
        if (i == -1) {
            i = 0;
        }
        danmaku.time = mDanmakuView.getCurrentTime() + (i * ADD_DANMU_TIME);
        danmaku.textSize = DANMU_TEXT_SIZE/* * (mDanmakuContext.getDisplayer().getDensity() - 0.6f)*/;
        danmaku.textColor = Color.WHITE;
        danmaku.textShadowColor = 0;
        final String content = danmu.getContent();
        // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
        mDanmakuView.setOnDanmakuClickListener(new IDanmakuView.OnDanmakuClickListener() {
            @Override
            public void onDanmakuClick(BaseDanmaku latest) {
                if (onDanmuClickListener != null)
                    onDanmuClickListener.onDanmuClick(latest);
            }

            @Override
            public void onDanmakuClick(IDanmakus danmakus) {


            }
        });

        Glide.with(mContext).load(danmu.usericon).asBitmap().
                diskCacheStrategy(DiskCacheStrategy.RESULT).
                //transform(new GlideCircleTransform(mContext)).
                        placeholder(R.drawable.default_comment_head).into(new SimpleTarget<Bitmap>() {

            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Bitmap defaultBitmap = getDefaultBitmap(resource);
                CirCleDrawable drawable = new CirCleDrawable(defaultBitmap);
                drawable.setBITMAP_HEIGHT(BITMAP_HEIGHT);
                drawable.setBITMAP_WIDTH(BITMAP_WIDTH);
                drawable.setBounds(0, 0, BITMAP_WIDTH, BITMAP_HEIGHT);
                SpannableStringBuilder spannable =
                        createSpannable(drawable, content);
                danmaku.text = spannable;
                mDanmakuView.addDanmaku(danmaku);

            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                Drawable drawable = mContext.getResources().getDrawable(R.drawable.default_danmu_head);
//                        getDrawable(R.drawable.default_danmu_head);;
                //Drawable drawable = mContext.getResources().getDrawable(R.drawable.default_danmu_head);
//                Bitmap bitmap = getDefaultBitmap(R.drawable.default_danmu_head);
//                CirCleDrawable drawable = new CirCleDrawable(bitmap);
                drawable.setBounds(0, 0, BITMAP_WIDTH, BITMAP_HEIGHT);
                SpannableStringBuilder spannable =
                        createSpannable(drawable, content);
                danmaku.text = spannable;
                mDanmakuView.addDanmaku(danmaku);
            }
        });
    }


    private Bitmap getDefaultBitmap(Bitmap bitmap) {
        Bitmap mDefauleBitmap = null;
        //Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), drawableId);
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Log.d(TAG, "width = " + width);
            Log.d(TAG, "height = " + height);
            Matrix matrix = new Matrix();
            matrix.postScale(((float) BITMAP_WIDTH * 1.2f) / width, ((float) BITMAP_HEIGHT) * 1.2f / height);
            mDefauleBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
            Log.d(TAG, "mDefauleBitmap getWidth = " + mDefauleBitmap.getWidth());
            Log.d(TAG, "mDefauleBitmap getHeight = " + mDefauleBitmap.getHeight());
        }
        return mDefauleBitmap;
    }

//    private SpannableStringBuilder createSpannable(Drawable drawable, String content) {
//        String text = "bitmap";
//        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
//        //CenteredImageSpan span = new CenteredImageSpan(drawable);
//        ImageSpan imageSpan = new ImageSpan(drawable, DynamicDrawableSpan.ALIGN_BOTTOM);
//        //CenteredImageSpan span = new CenteredImageSpan(drawable);
//        spannableStringBuilder.setSpan(imageSpan, 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//        if (!TextUtils.isEmpty(content)) {
//            spannableStringBuilder.append("   ");
//            spannableStringBuilder.append(content.trim());
//            spannableStringBuilder.append("   ");
//        }
//        return spannableStringBuilder;
//    }

    private SpannableStringBuilder createSpannable(Drawable drawable, String content) {
        String text = "bitmap";
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
        spannableStringBuilder.setSpan(span, 0, text.length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        if (!TextUtils.isEmpty(content)) {
            spannableStringBuilder.append(" ");
            spannableStringBuilder.append(content.trim());
        }
        return spannableStringBuilder;
    }


    public interface OnDanmuClickListener {
        void onDanmuClick(BaseDanmaku latest);
    }


}
