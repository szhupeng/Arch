package space.zhupeng.fxbase;

import android.app.Application;
import android.content.Context;

/**
 * @author zhupeng
 * @date 2017/9/16
 */

public interface AppLifecycle {

    void attachBaseContext(Context base);

    void onCreate(Application application);

    void onLowMemory(Application application);

    void onTerminate(Application application);
}
