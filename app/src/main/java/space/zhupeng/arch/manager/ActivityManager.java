package space.zhupeng.arch.manager;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.RestrictTo;
import android.support.design.widget.Snackbar;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;
import static android.support.design.widget.Snackbar.LENGTH_INDEFINITE;
import static android.support.design.widget.Snackbar.LENGTH_LONG;
import static android.support.design.widget.Snackbar.LENGTH_SHORT;

/**
 * 管理所有的activity
 *
 * @author zhupeng
 * @date 2017/9/16
 */

public class ActivityManager {

    @RestrictTo(LIBRARY_GROUP)
    @IntDef({LENGTH_INDEFINITE, LENGTH_SHORT, LENGTH_LONG})
    @IntRange(from = 1)
    @Retention(RetentionPolicy.SOURCE)
    @interface Duration {
    }

    private Application mAppContext;
    private List<Activity> mActivities;
    private Activity mCurrentActivity;

    public ActivityManager(Application application) {
        this.mAppContext = application;
    }

    /**
     * 让前台的activity,使用Snackbar显示文本提示
     *
     * @param message
     * @param duration
     */
    public void showSnackbar(final String message, @Duration int duration) {
        if (null == mCurrentActivity) {
            throw new NullPointerException("The variable mActivities in ActivityManager is null when you call removeActivity(int)");
        }
        final View view = mCurrentActivity.getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar.make(view, message, duration).show();
    }

    /**
     * 释放资源
     */
    public void release() {
        mActivities.clear();
        mActivities = null;
        mCurrentActivity = null;
        mAppContext = null;
    }

    /**
     * 设置当前可见的activity
     *
     * @param activity
     */
    public void setCurrentActivity(Activity activity) {
        this.mCurrentActivity = activity;
    }

    /**
     * 获取在前台activity
     *
     * @return
     */
    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public Activity getTopActivity() {
        if (null == mActivities) {
            throw new NullPointerException("The variable mActivities in ActivityManager is null when you call removeActivity(int)");
        }
        return mActivities.size() > 0 ? mActivities.get(mActivities.size() - 1) : null;
    }

    /**
     * 返回一个存储所有未销毁的activity的集合
     *
     * @return
     */
    public List<Activity> getActivityList() {
        if (null == mActivities) {
            mActivities = new LinkedList<>();
        }
        return mActivities;
    }


    /**
     * 添加activity到集合
     */
    public void addActivity(Activity activity) {
        if (null == mActivities) {
            mActivities = new LinkedList<>();
        }
        synchronized (ActivityManager.class) {
            if (!mActivities.contains(activity)) {
                mActivities.add(activity);
            }
        }
    }

    /**
     * 移除集合里的指定的activity实例
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        if (mActivities == null) {
            throw new NullPointerException("The variable mActivities in ActivityManager is null when you call removeActivity(int)");
        }
        synchronized (ActivityManager.class) {
            if (mActivities.contains(activity)) {
                mActivities.remove(activity);
            }
        }
    }

    /**
     * 移除集合里的指定位置的activity
     *
     * @param index
     */
    public Activity removeActivity(final int index) {
        if (null == mActivities) {
            throw new NullPointerException("The variable mActivities in ActivityManager is null when you call removeActivity(int)");
        }
        synchronized (ActivityManager.class) {
            if (index > 0 && index < mActivities.size()) {
                return mActivities.remove(index);
            }
        }
        return null;
    }

    /**
     * 关闭指定的Activity类的所有的实例
     *
     * @param cls
     */
    public void killActivity(Class<?> cls) {
        if (null == mActivities) {
            throw new NullPointerException("The variable mActivities in ActivityManager is null when you call killActivity(Class<Activity>)");
        }

        for (Activity activity : mActivities) {
            if (activity.getClass().equals(cls)) {
                activity.finish();
            }
        }
    }


    /**
     * 指定的activity实例是否存活
     *
     * @param activity
     * @return
     */
    public boolean isActivityAlive(Activity activity) {
        if (null == mActivities) {
            throw new NullPointerException("The variable mActivities in ActivityManager is null when you call isActivityAlive(Activity)");
        }
        return mActivities.contains(activity);
    }

    /**
     * 指定的Activity类是否有实例存活(同一个Activity类可能有多个实例)
     *
     * @param cls
     * @return
     */
    public boolean isActivityAlive(Class<?> cls) {
        if (null == mActivities) {
            throw new NullPointerException("The variable mActivities in ActivityManager is null when you call isActivityAlive(Class<?>)");
        }

        for (Activity activity : mActivities) {
            if (activity.getClass().equals(cls)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取指定Activity类的实例,没有则返回null(同一个Activity类有多个实例,则返回最早的实例)
     *
     * @param cls
     * @return
     */
    public Activity findActivity(Class<?> cls) {
        if (null == mActivities) {
            throw new NullPointerException("The variable mActivities in ActivityManager is null when you call findActivity(Class<?>)");
        }

        for (Activity activity : mActivities) {
            if (activity.getClass().equals(cls)) {
                return activity;
            }
        }
        return null;
    }

    /**
     * 关闭所有的activity
     */
    public void killAll() {
        Iterator<Activity> iterator = getActivityList().iterator();
        while (iterator.hasNext()) {
            Activity next = iterator.next();
            iterator.remove();
            next.finish();
        }
    }

    /**
     * 关闭所有activity, 排除指定的activity
     *
     * @param excludes
     */
    public void killAll(Class<?>... excludes) {
        List<Class<?>> excludeList = Arrays.asList(excludes);
        Iterator<Activity> iterator = getActivityList().iterator();
        while (iterator.hasNext()) {
            Activity next = iterator.next();
            if (excludeList.contains(next.getClass())) {
                continue;
            }
            iterator.remove();
            next.finish();
        }
    }

    /**
     * 关闭所有activity,排除指定的activity
     *
     * @param excludes activity的全路径
     */
    public void killAll(String... excludes) {
        List<String> excludeList = Arrays.asList(excludes);
        Iterator<Activity> iterator = getActivityList().iterator();
        while (iterator.hasNext()) {
            Activity next = iterator.next();

            if (excludeList.contains(next.getClass().getName()))
                continue;

            iterator.remove();
            next.finish();
        }
    }


    /**
     * 退出应用程序
     */
    public void exitApp() {
        try {
            killAll();
            release();
            android.app.ActivityManager mgr = (android.app.ActivityManager) mAppContext.getSystemService(Context.ACTIVITY_SERVICE);
            mgr.killBackgroundProcesses(mAppContext.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
