package space.zhupeng.fxbase.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

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
     * 判断Activity是否被销毁
     *
     * @param activity
     * @return
     */
    public static boolean isActivityDestroyed(final Activity activity) {
        if (null == activity) return true;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            return activity.isDestroyed();
        }

        if (activity instanceof AppCompatActivity) {
            return ((AppCompatActivity) activity).getSupportFragmentManager().isDestroyed();
        }

        return activity.isFinishing();
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
    private static String getAppMetaData(Context context, String key, String defValue) {
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
    private static Bundle getAppMetaDataBundle(PackageManager packageManager, String packageName) {
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

    /**
     * 调用系统相机拍照
     *
     * @param activity    当前activity
     * @param imageUri    拍照后照片存储路径
     * @param requestCode 请求码
     */
    public static void takePicture(Activity activity, Uri imageUri, int requestCode) {
        //调用系统相机
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        //将拍照结果保存至photo_file的Uri中，不保留在相册中
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        if (activity != null) {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 打开系统相册
     *
     * @param activity    当前activity
     * @param requestCode 请求码
     */
    public static void openAlbum(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        activity.startActivityForResult(intent, requestCode);
    }
}
