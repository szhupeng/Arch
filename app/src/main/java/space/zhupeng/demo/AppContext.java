package space.zhupeng.demo;

import android.app.Application;

import space.zhupeng.arch.manager.DataManager;

/**
 * Created by zhupeng on 2018/1/16.
 */

public class AppContext extends Application {

    private static AppContext sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

        DataManager.getInstance().initialize(this, Api.BASE_URL);
    }

    public static AppContext get() {
        return sInstance;
    }
}
