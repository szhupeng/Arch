package space.zhupeng.fxbase;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentManager;

import java.util.List;

/**
 * @author zhupeng
 * @date 2017/9/16
 */
@SuppressWarnings("all")
public interface Injector {

    /**
     * 注入Application生命周期
     *
     * @param context
     * @param lifecycles
     */
    void injectAppLifecycle(Context context, List<AppLifecycle> lifecycles);

    /**
     * 注入Activity生命周期
     *
     * @param context
     * @param lifecycles
     */
    void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles);

    /**
     * 注入Fragment生命周期
     *
     * @param context
     * @param lifecycles
     */
    void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles);

    /**
     * 注入AppComponent
     *
     * @param context
     * @param component
     */
    void injectAppComponent(Context context, AppComponent component);
}
