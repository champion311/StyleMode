package shinerich.com.stylemodel.common;

import android.os.CountDownTimer;

/**
 * 倒计时Task
 *
 * @author hunk
 */
public class DownTimerTask {

    private MyTask task;
    private OnCallbackListener listener;

    /***
     * DownTimerTask
     *
     * @param interval 毫秒
     * @param listener
     */
    public DownTimerTask(int interval, OnCallbackListener listener) {

        this.listener = listener;
        task = new MyTask((interval + 1000));
    }

    /**
     * 开始计时
     */
    public void start() {
        task.start();
    }

    /**
     * 计时器
     */
    class MyTask extends CountDownTimer {

        public MyTask(long millisInFuture) {
            super(millisInFuture, 1000);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int count = (int) (millisUntilFinished / 1000);
            if (listener != null) {
                listener.onTick(count);
            }
        }

        @Override
        public void onFinish() {
            if (listener != null) {
                listener.onFinish();
            }
        }

    }

    /**
     * 回调事件
     */
    public interface OnCallbackListener {
        /**
         * 当前秒数
         */
        void onTick(int interval);

        /**
         * 完成
         */
        void onFinish();

    }

    /**
     * 取消
     */
    public void cancel() {
        task.cancel();
    }
}
