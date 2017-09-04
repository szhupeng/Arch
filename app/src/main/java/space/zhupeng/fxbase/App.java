package space.zhupeng.fxbase;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * @author zhupeng
 * @date 2016/12/11
 */

public class App extends Application {

    private static Application sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public static final Application get() {
        return sContext;
    }
}
