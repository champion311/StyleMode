package shinerich.com.stylemodel.common;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 下载APK任务
 *
 * @author hunk
 */
public class DownApkTask {

    public static final int DOWN_UPDATE = 101;     //更新
    public static final int DOWN_OVER = 102;       //完成
    private Thread downLoadThread;
    private ProgressBar progressBar;
    private boolean interceptFlag = false;
    private File downDir; // APK下载目录
    private String downUrl;
    private String apkPath;
    private int progress;
    private Context context;
    private OnCallbackListener listener;

    /**
     * @param progressBar
     * @param downUrl
     * @param downDir
     */
    public DownApkTask(ProgressBar progressBar, String downUrl, File downDir) {
        this.progressBar = progressBar;
        this.context = progressBar.getContext();
        this.downDir = downDir;
        this.downUrl = downUrl;
        downLoadThread = new Thread(downApkRunnable);
    }

    /**
     * 设置回调事件
     */
    public void setOnCallbackListener(OnCallbackListener listener) {
        this.listener = listener;
    }


    /**
     * 下载
     */
    public void download() {
        downLoadThread.start();
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    progressBar.setProgress(progress);
                    break;
                case DOWN_OVER:
                    progressBar.setProgress(100);
                    if (listener != null) {
                        listener.onCompete();
                    }
                    break;
            }
        }
    };

    /**
     * 安装包下载线程
     */
    private Runnable downApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(downUrl);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();
                File ApkFile = getFile(downUrl);
                apkPath = ApkFile.getAbsolutePath();
                FileOutputStream fos = new FileOutputStream(ApkFile);

                int count = 0;
                byte buf[] = new byte[1024];
                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        // 下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!interceptFlag);// 点击取消就停止下载.
                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };


    /**
     * 取消
     */
    public void cancel() {
        interceptFlag = true;
    }

    /**
     * APK保存路径
     */
    public String getApkPath() {
        return apkPath;
    }

    // 获取当前文件
    private File getFile(String url) {
        // 检查目录是否存在
        if (downDir != null && !downDir.exists()) {
            downDir.mkdirs();
        }
        File file = new File(downDir.getAbsolutePath(), getAPKName(url));
        return file;
    }

    /**
     * 截取出url后面的apk的文件名
     *
     * @param url
     */
    private String getAPKName(String url) {

        return url.substring(url.lastIndexOf("/"), url.length());
    }

    public interface OnCallbackListener {
        void onCompete();
    }
}