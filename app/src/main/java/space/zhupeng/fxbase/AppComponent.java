package space.zhupeng.fxbase;

import android.app.Application;

import java.io.File;
import java.util.Map;

import space.zhupeng.fxbase.image.ImageLoader;
import space.zhupeng.fxbase.manager.ActivityManager;
import space.zhupeng.fxbase.mvp.model.Repository;

/**
 * @author zhupeng
 * @date 2017/9/16
 */

public interface AppComponent {

    Application provideAppContext();

    //用于管理所有的activity
    ActivityManager provideActivityManager();

    //图片管理器,用于加载图片的管理类,使用策略模式,可在运行时替换框架
    ImageLoader provideImageLoader();

    //缓存文件根目录,应该将所有缓存都放到这个根目录下,便于管理和清理
    File provideCacheFile();

    //用来存取一些整个App公用的数据,切勿大量存放大容量数据
    Map<String, Object> provideExtras();
}
