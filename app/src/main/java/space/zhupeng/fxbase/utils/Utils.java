package space.zhupeng.fxbase.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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

    /**
     * 在后台安全执行
     *
     * @param runnable
     */
    public static void runOnBackgroundSafely(final Runnable runnable) {
        if (null == runnable) return;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } catch (Exception e) {
                }
            }
        }).start();
    }

    /**
     * 根据key从Activity中返回的Bundle中获取value
     *
     * @param key
     * @param defValue
     * @return
     */
    public static String getActivityMetaData(Activity activity, String key, String defValue) {
        Bundle bundle = getActivityMetaDataBundle(activity.getPackageManager(), activity.getComponentName());
        if (bundle != null && bundle.containsKey(key)) {
            return bundle.getString(key);
        }
        return defValue;
    }

    /**
     * 获取Activity中的meta-data
     *
     * @param packageManager
     * @param component
     * @return
     */
    public static Bundle getActivityMetaDataBundle(PackageManager packageManager, ComponentName component) {
        Bundle bundle = null;
        try {
            ActivityInfo ai = packageManager.getActivityInfo(component,
                    PackageManager.GET_META_DATA);
            bundle = ai.metaData;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return bundle;
    }

    /**
     * 根据key从Application中返回的Bundle中获取value
     *
     * @param key
     * @param defValue
     * @return
     */
    private String getAppMetaData(Context context, String key, String defValue) {
        Bundle bundle = getAppMetaDataBundle(context.getPackageManager(), context.getPackageName());
        if (bundle != null && bundle.containsKey(key)) {
            return bundle.getString(key);
        }
        return defValue;
    }

    /**
     * 获取Application中的meta-data
     *
     * @param packageManager
     * @param packageName
     * @return
     */
    private Bundle getAppMetaDataBundle(PackageManager packageManager, String packageName) {
        Bundle bundle = null;
        try {
            ApplicationInfo ai = packageManager.getApplicationInfo(packageName,
                    PackageManager.GET_META_DATA);
            bundle = ai.metaData;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return bundle;
    }
}
