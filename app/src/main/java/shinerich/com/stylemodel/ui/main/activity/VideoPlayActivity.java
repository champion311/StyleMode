package shinerich.com.stylemodel.ui.main.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.base.SimpleActivity;
import shinerich.com.stylemodel.utils.NetWorkUtils;

/**
 * Created by Administrator on 2016/11/15.
 */
public class VideoPlayActivity extends Activity implements
        SurfaceHolder.Callback, View.OnTouchListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener,
        SeekBar.OnSeekBarChangeListener, Runnable, MediaPlayer.OnSeekCompleteListener {
    @BindView(R.id.mSurfaceView)
    SurfaceView mSurfaceView;
    @BindView(R.id.mProgressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.net_alert)
    TextView netAlert;
    @BindView(R.id.im_back)
    ImageView imBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.share_btn)
    ImageView shareBtn;
    @BindView(R.id.top_wrapper)
    RelativeLayout topWrapper;
    @BindView(R.id.pause_play_btn)
    ImageView pausePlayBtn;
    @BindView(R.id.video_time)
    TextView videoTime;
    @BindView(R.id.viedo_progressbar)
    SeekBar videoProgressbar;
    @BindView(R.id.bottom_wrapper)
    LinearLayout bottomWrapper;

    private String videoUrl;

    private SurfaceHolder mSurfaceHolder;

    //是否准备好了
    private boolean isPrepared = false;

    // 当player未准备好，并且当前activity经过onPause()生命周期时，此值为true
    private boolean isFreeze;

    private int currentPosition = 0;// 记录数上此播放的时间

    private boolean isPlaying = false;

    private boolean isSurfaceHasDestory = false;

    private boolean isShowView = false;

    private Handler handler = new Handler();

    private DelayeDismiss dimissThread;


    private MediaPlayer player;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.setContentView(R.layout.activity_video_play);
        ButterKnife.bind(this);
        //videoUrl = "http://tv.sohu.com/upload/static/share/share_play.html#85050837_9053735_0_9001_0";
        //videoUrl = "http://smf.stylemode.com.cn/vod/201611/dior.mp4";
        videoUrl = getIntent().getStringExtra("video_url");
        initEventAndData();
        initPlayer();

    }


    protected void initEventAndData() {


        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); // 2.3及以下使用，不然出现只有声音没有图像的问题
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.addCallback(this);
        pausePlayBtn.setClickable(false);
        videoTime.setText("00:00");
        videoProgressbar.setEnabled(false);//默认无法移动,需要加载完成
        topWrapper.setVisibility(View.GONE);
        bottomWrapper.setVisibility(View.GONE);
        mSurfaceView.setOnTouchListener(this);
        dimissThread = new DelayeDismiss();
        initPlayer();

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (isPrepared) {
            if (player != null) {
                player.start();
                handler.post(this);
            }
        }
        try {
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setOnBufferingUpdateListener(this);
            player.setOnPreparedListener(this);
            player.setOnCompletionListener(this);
            player.setDisplay(holder);
            if (isSurfaceHasDestory) {
                player.prepareAsync();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        holder.setFixedSize(width, height);

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (player == null) {
            return;
        }
        //移除线程
        handler.removeCallbacks(this);
        try {
            if (isPrepared) {
                // 记录播放的时间
                currentPosition = player.getCurrentPosition();
            } else {
                //player.reset();
                player.release();
                return;
            }
            isSurfaceHasDestory = true;
            player.pause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }

    }

    public void initPlayer() {
        //onCreate
        if (TextUtils.isEmpty(videoUrl)) {
            return;
        }
        if (!NetWorkUtils.isNetWorkConnected(this)) {
            mProgressBar.setVisibility(View.GONE);
            netAlert.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
            netAlert.setVisibility(View.GONE);
        }
        isPrepared = false;


        try {
            if (player == null) {
                player = new MediaPlayer();
            }
            player.reset();
            player.setLooping(false);
            player.setDataSource(this, Uri.parse(videoUrl));
            player.prepareAsync();
            player.setOnErrorListener(this);
            player.setOnInfoListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mProgressBar.setVisibility(View.GONE);
        return false;
    }


    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                if (player.isPlaying()) {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                mProgressBar.setVisibility(View.GONE);
                break;
            case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                mProgressBar.setVisibility(View.GONE);
                break;

        }


        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mProgressBar.setVisibility(View.GONE);
        if (!isFreeze) {
            player.start();
        }
        if (currentPosition > 0) {
            player.seekTo(currentPosition);
        }
        isPrepared = true;
        videoProgressbar.setEnabled(true);
        player.start();
        pausePlayBtn.setClickable(true);

        videoProgressbar.setMax(player.getDuration());
        videoProgressbar.setOnSeekBarChangeListener(this);
        handler.post(this);
        player.setOnSeekCompleteListener(this);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        if (player != null && isPrepared) {
            videoProgressbar.setSecondaryProgress(videoProgressbar.getMax()
                    * percent / 100);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        handler.removeCallbacks(this);
        if (isPrepared) {
            player.stop();
        }
        //player.reset();
    }


    private boolean isLanScape = true;

    @OnClick({R.id.im_back, R.id.pause_play_btn, R.id.net_alert})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.pause_play_btn:
                pauseOrPlayAction();
                break;
            case R.id.im_back:
//                if (isLanScape) {
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
//                } else {
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
//                }
//                isLanScape = !isLanScape;
                finish();
                break;
            case R.id.net_alert:
                initPlayer();
                break;

        }

    }


    /**
     * 暂停或者继续的动作
     */
    public void pauseOrPlayAction() {
        boolean selected = pausePlayBtn.isSelected();
        if (!selected) {
            // 当前播放中
            if (player.isPlaying()) {
                player.pause();
                isPlaying = false;
                handler.removeCallbacks(this);// 移除线程
            }
        } else {
            if (!player.isPlaying()) {
                try {
                    player.prepare();
                    player.start();
                    handler.post(this);
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                isPlaying = true;
            }
        }
        pausePlayBtn.setSelected(!selected);
    }


    /**
     * 是否显示View
     *
     * @param isShow 是否显示wrapper
     */
    public void disMissWrapper(boolean isShow) {
        isShowView = !isShowView;
        if (!isShow) {
            // 隐藏
            Animation bottomDisMiss = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.f,
                    Animation.RELATIVE_TO_SELF, 0.f,
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                    1f);
            bottomDisMiss.setDuration(500);
            bottomWrapper.startAnimation(bottomDisMiss);
            bottomDisMiss.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // TODO Auto-generated method stub
                    bottomWrapper.setVisibility(View.GONE);

                }
            });

            Animation TopDimss = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.f,
                    Animation.RELATIVE_TO_SELF, 0.f,
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                    -1f);
            TopDimss.setDuration(500);
            topWrapper.startAnimation(TopDimss);
            TopDimss.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // TODO Auto-generated method stub
                    topWrapper.setVisibility(View.GONE);
                    handler.removeCallbacks(dimissThread);

                }
            });
        } else {
            // 展开
            bottomWrapper.setVisibility(View.VISIBLE);
            Animation bottomShow = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.f,
                    Animation.RELATIVE_TO_SELF, 0.f,
                    Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF,
                    0f);
            bottomShow.setDuration(500);
            bottomWrapper.startAnimation(bottomShow);
            bottomShow.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // TODO Auto-generated method stub

                }
            });
            topWrapper.setVisibility(View.VISIBLE);
            Animation TopShow = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.f,
                    Animation.RELATIVE_TO_SELF, 0.f,
                    Animation.RELATIVE_TO_SELF, -1f,
                    Animation.RELATIVE_TO_SELF, 0f);
            TopShow.setDuration(500);
            topWrapper.startAnimation(TopShow);
            TopShow.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // TODO Auto-generated method stub

                }
            });

        }

    }

    private long lastTimePressDownTime;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 处理连续点情况
                if (System.currentTimeMillis() - lastTimePressDownTime <= 500) {
                    // TODO 暂停
                    if (isPrepared) {
                        // 暂停或者开始
                        pauseOrPlayAction();
                    }
                    lastTimePressDownTime = System.currentTimeMillis();
                    return false;
                }
                lastTimePressDownTime = System.currentTimeMillis();
                disMissWrapper(!isShowView);
                // 延时15s消失
                handler.postDelayed(dimissThread, 15 * 1000);
                break;
        }
        return false;
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            if (player != null) {
                player.seekTo(progress);
            }
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    @Override
    public void onSeekComplete(MediaPlayer mp) {
        setTime(player.getCurrentPosition());
    }

    @Override
    public void run() {
        if (player == null || !player.isPlaying()) {
            return;
        }
        if (player.isPlaying()) {
            handler.postDelayed(this, 50);
            videoProgressbar.setProgress(player.getCurrentPosition());
            setTime(player.getCurrentPosition());
        }
    }

    public void setTime(int duration) {
        if (duration <= 0) {
            return;
        }
        duration /= 1000;
        String ret = "";
        if (duration > 0 && duration < 60) {
            String secondStr = duration < 10 ? "0" + duration : duration + "";
            ret = "00:" + secondStr;
        } else {
            int min = duration / 60;
            int secound = duration % 60;
            String minStr = min < 10 ? "0" + min : min + "";
            String secondStr = secound < 10 ? "0" + secound : secound + "";
            ret = minStr + ":" + secondStr;
        }
        videoTime.setText(ret);

    }


    /*
     * 自动消失wrapper的线程
     */
    private class DelayeDismiss implements Runnable {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (isShowView) {
                disMissWrapper(false);
            }
        }
    }
}
