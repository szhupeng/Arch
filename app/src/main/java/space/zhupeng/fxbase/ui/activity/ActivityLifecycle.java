package space.zhupeng.fxbase.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import space.zhupeng.fxbase.Injector;
import space.zhupeng.fxbase.manager.ActivityManager;
import space.zhupeng.fxbase.ui.activity.delegate.ActivityDelegate;
import space.zhupeng.fxbase.ui.activity.delegate.ActivityDelegateImpl;

/**
 * @author zhupeng
 * @date 2017/9/16
 */

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    private ActivityManager mActivityManager;
    private Application mAppContext;
    private List<FragmentManager.FragmentLifecycleCallbacks> mFragmentLifecycles;
    private ActivityDelegate mDelegate;

    public ActivityLifecycle(ActivityManager manager, Application application, ActivityDelegate delegate) {
        this.mActivityManager = manager;
        this.mAppContext = application;
        this.mDelegate = delegate;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (mActivityManager != null) {
            mActivityManager.addActivity(activity);
        }

        //配置ActivityDelegate
        if (mDelegate == null) {
            mDelegate = new ActivityDelegateImpl(activity);
        }
        mDelegate.onCreate(savedInstanceState);

        registerFragmentCallbacks(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (mDelegate != null) {
            mDelegate.onStart();
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        mActivityManager.setCurrentActivity(activity);

        if (mDelegate != null) {
            mDelegate.onResume();
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (mDelegate != null) {
            mDelegate.onPause();
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (mActivityManager.getCurrentActivity() == activity) {
            mActivityManager.setCurrentActivity(null);
        }

        if (mDelegate != null) {
            mDelegate.onStop();
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        if (mDelegate != null) {
            mDelegate.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mActivityManager.removeActivity(activity);

        if (mDelegate != null) {
            mDelegate.onDestroy();
        }
    }

    /**
     * 给Activity的Fragment设置监听其生命周期
     *
     * @param activity
     */
    private void registerFragmentCallbacks(Activity activity) {
        if (activity instanceof FragmentActivity) {
            FragmentManager fm = ((FragmentActivity) activity).getSupportFragmentManager();
            if (null == fm.getFragments() || 0 == fm.getFragments().size()) {
                return;
            }

            if (mFragmentLifecycles == null) {
                mFragmentLifecycles = new ArrayList<>();
                try {
                    Method method = mAppContext.getClass().getMethod("getAppDelegate");
                    List<Injector> injectors = (List<Injector>) method.invoke(null);
                    for (Injector injector : injectors) {
                        injector.injectFragmentLifecycle(mAppContext, mFragmentLifecycles);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            for (FragmentManager.FragmentLifecycleCallbacks fragmentLifecycle : mFragmentLifecycles) {
                ((FragmentActivity) activity).getSupportFragmentManager().registerFragmentLifecycleCallbacks(fragmentLifecycle, true);
            }
        }
    }
}
