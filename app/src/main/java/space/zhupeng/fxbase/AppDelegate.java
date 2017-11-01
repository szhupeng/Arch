package space.zhupeng.fxbase;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

import space.zhupeng.fxbase.image.ImageConfig;

/**
 * 代理Application的生命周期
 *
 * @author zhupeng
 * @date 2017/9/16
 */

@SuppressWarnings("all")
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public final class AppDelegate implements AppLifecycle, App {

    private Application mAppContext;
    private final List<Injector> mInjectors;
    private List<Application.ActivityLifecycleCallbacks> mActivityLifecycles = new ArrayList<>();
    private ComponentCallbacks2 mComponentCallback;

    private AppComponent mAppComponent;

    public AppDelegate(Context context) {
        mInjectors = ManifestParser.parse(context);
        for (Injector injector : mInjectors) {
            injector.injectActivityLifecycle(context, mActivityLifecycles);
            injector.injectAppComponent(context, mAppComponent);
        }
    }

    @Override
    public void attachBaseContext(Context base) {
    }

    @Override
    public void onCreate(Application application) {
        this.mAppContext = application;

        for (Application.ActivityLifecycleCallbacks lifecycle : mActivityLifecycles) {
            this.mAppContext.registerActivityLifecycleCallbacks(lifecycle);
        }

        this.mComponentCallback = new AppComponentCallbacks(mAppContext, mAppComponent);
        this.mAppContext.registerComponentCallbacks(mComponentCallback);
    }

    @Override
    public void onLowMemory(Application application) {
    }

    @Override
    public void onTerminate(Application application) {
        if (mComponentCallback != null) {
            mAppContext.unregisterComponentCallbacks(mComponentCallback);
        }

        if (mActivityLifecycles != null && mActivityLifecycles.size() > 0) {
            for (Application.ActivityLifecycleCallbacks lifecycle : mActivityLifecycles) {
                mAppContext.unregisterActivityLifecycleCallbacks(lifecycle);
            }
        }

        this.mAppComponent = null;
        this.mActivityLifecycles = null;
        this.mComponentCallback = null;
        this.mAppContext = null;
    }

    public List<Injector> getInjectors() {
        return mInjectors;
    }

    @Override
    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    private static class AppComponentCallbacks implements ComponentCallbacks2 {
        private Application mApplication;
        private AppComponent mAppComponent;

        public AppComponentCallbacks(Application application, AppComponent appComponent) {
            this.mApplication = application;
            this.mAppComponent = appComponent;
        }

        @Override
        public void onTrimMemory(int level) {
        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {
        }

        @Override
        public void onLowMemory() {
            //内存不足时清理图片请求框架的内存缓存
            mAppComponent.provideImageLoader().clear(mApplication, new ImageConfig());
        }
    }
}
