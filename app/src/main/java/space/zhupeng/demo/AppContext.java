package space.zhupeng.demo;

import android.app.Application;

import space.zhupeng.arch.manager.DataManager;

/**
 * Created by zhupeng on 2018/1/16.
 */

public class AppContext extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DataManager.getInstance().initialize(this, Api.BASE_URL);
    }
}
