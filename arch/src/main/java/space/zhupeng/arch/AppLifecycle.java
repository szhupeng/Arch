package space.zhupeng.arch;

import android.app.Application;
import android.content.Context;

/**
 * @author zhupeng
 * @date 2018/1/4
 */

public interface AppLifecycle {

    void attachBaseContext(Context base);

    void onCreate(Application application);

    void onTerminate(Application application);
}
