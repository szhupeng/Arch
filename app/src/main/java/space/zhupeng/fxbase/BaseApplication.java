package space.zhupeng.fxbase;

import android.app.Application;
import android.content.Context;

/**
 * @author zhupeng
 * @date 2016/12/11
 */

public class BaseApplication extends Application implements App {

    private AppLifecycle mAppDelegate;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        mAppDelegate = new AppDelegate(base);
        mAppDelegate.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAppDelegate.onCreate(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        if (mAppDelegate != null) {
            mAppDelegate.onLowMemory(this);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        if (mAppDelegate != null) {
            mAppDelegate.onTerminate(this);
        }
    }

    /**
     * 获取注入的AppComponent
     *
     * @return
     */
    @Override
    public AppComponent getAppComponent() {
        return ((App) mAppDelegate).getAppComponent();
    }
}
