package space.zhupeng.fxbase.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import space.zhupeng.fxbase.Injector;
import space.zhupeng.fxbase.ui.activity.delegate.ActivityDelegate;
import space.zhupeng.fxbase.ui.activity.delegate.ActivityDelegateImpl;
import space.zhupeng.fxbase.ui.fragment.FragmentLifecycle;
import space.zhupeng.fxbase.manager.ActivityManager;

/**
 * @author zhupeng
 * @date 2017/9/16
 */

@SuppressWarnings("all")
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    private ActivityManager mActivityManager;
    private Application mAppContext;
    private FragmentManager.FragmentLifecycleCallbacks mFragmentLifecycle;
    private List<FragmentManager.FragmentLifecycleCallbacks> mFragmentLifecycles;
    private ActivityDelegate mDelegate;

    public ActivityLifecycle(ActivityManager manager, Application application, ActivityDelegate delegate) {
        this.mActivityManager = manager;
        this.mAppContext = application;
        this.mDelegate = delegate;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        //如果intent包含了此字段,并且为true说明不加入到list进行统一管理
//        boolean isAddEnable = true;
//        if (activity.getIntent() != null) {
//            isAddEnable = activity.getIntent().getBooleanExtra(ActivityManager.IS_ADD_ENABLE, true);
//        }
//
//        if (isAddEnable) {
//            mActivityManager.addActivity(activity);
//        }

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
     * 给每个 Activity 的所有 Fragment 设置监听其生命周期, Activity 可以通过 {@link IActivity#useFragment()}
     * 设置是否使用监听,如果这个 Activity 返回 false 的话,这个 Activity 下面的所有 Fragment 将不能使用 {@link FragmentDelegate}
     * 意味着 {@link BaseFragment} 也不能使用
     *
     * @param activity
     */
    private void registerFragmentCallbacks(Activity activity) {
//        boolean useFragment = activity instanceof IActivity ? ((IActivity) activity).useFragment() : true;
//        if (activity instanceof FragmentActivity && useFragment) {
//
//            if (mFragmentLifecycle == null) {
//                mFragmentLifecycle = new FragmentLifecycle();
//            }
//
//            ((FragmentActivity) activity).getSupportFragmentManager().registerFragmentLifecycleCallbacks(mFragmentLifecycle, true);
//
//            if (mFragmentLifecycles == null && mExtras.containsKey(Injector.class.getName())) {
//                mFragmentLifecycles = new ArrayList<>();
//                List<Injector> injectors = (List<Injector>) mExtras.get(Injector.class.getName());
//                for (Injector injector : injectors) {
//                    injector.injectFragmentLifecycle(mAppContext, mFragmentLifecycles);
//                }
//                mExtras.put(Injector.class.getName(), null);
//            }
//
//            for (FragmentManager.FragmentLifecycleCallbacks fragmentLifecycle : mFragmentLifecycles) {
//                ((FragmentActivity) activity).getSupportFragmentManager().registerFragmentLifecycleCallbacks(fragmentLifecycle, true);
//            }
//        }
    }
}
