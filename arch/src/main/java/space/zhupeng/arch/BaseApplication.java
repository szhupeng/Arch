package space.zhupeng.arch;

import android.app.Application;
import android.content.Context;

/**
 * @author zhupeng
 * @date 2018/1/3
 */

public class BaseApplication extends Application {

    private AppLifecycle mAppLifecycle;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        if (null == mAppLifecycle) {
            mAppLifecycle = new AppDelegate();
        }
        mAppLifecycle.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (mAppLifecycle != null) {
            mAppLifecycle.onCreate(this);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        if (mAppLifecycle != null) {
            mAppLifecycle.onTerminate(this);
        }
    }

    public AppLifecycle getAppDelegate() {
        return mAppLifecycle;
    }
}
