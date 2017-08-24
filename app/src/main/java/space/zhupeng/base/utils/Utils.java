package space.zhupeng.base.utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;

/**
 * 通用工具方法
 *
 * @author zhupeng
 * @date 2017/1/14
 */

public final class Utils {

    private Utils() {
        throw new UnsupportedOperationException("this method can't be called");
    }

    /**
     * 任务是否空闲
     *
     * @param task
     * @return
     */
    public static boolean isTaskIdle(final AsyncTask task) {
        return null == task || AsyncTask.Status.FINISHED == task.getStatus();
    }

    /**
     * 取消异步任务
     *
     * @param task
     */
    public static void cancelTask(final AsyncTask task) {
        if (task != null && AsyncTask.Status.RUNNING == task.getStatus()) {
            task.cancel(true);
        }
    }

    /**
     * 在主线程安全执行
     *
     * @param handler
     * @param runnable
     */
    public static void postSafely(final Handler handler, final Runnable runnable) {
        if (null == handler) return;

        handler.post(new Runnable() {
            public void run() {
                try {
                    runnable.run();
                } catch (Exception e) {
                }
            }
        });
    }

    /**
     * 在主线程安全执行
     *
     * @param activity
     * @param runnable
     */
    public static void runOnUiThreadSafely(final Activity activity, final Runnable runnable) {
        if (null == runnable || null == activity || activity.isFinishing()) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed())
            return;

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } catch (Exception e) {
                }
            }
        });
    }
}
