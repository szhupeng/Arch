package space.zhupeng.arch;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author zhupeng
 * @date 2018/1/4
 */

public class AppDelegate implements AppLifecycle {

    private LinkedList<Activity> mActivities;
    private Activity mCurrentActivity;

    @Override
    public void attachBaseContext(Context base) {
        MultiDex.install(base);
    }

    @Override
    public void onCreate(Application application) {
        mActivities = new LinkedList<>();

        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                mCurrentActivity = activity;
                mActivities.add(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                mActivities.remove(activity);
            }
        });
    }

    @Override
    public void onTerminate(Application application) {
    }

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
    public void exitApp(Context context) {
        try {
            killAll();
            release();
            ActivityManager mgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            mgr.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        mActivities.clear();
        mActivities = null;
        mCurrentActivity = null;
    }
}
