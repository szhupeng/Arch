package space.zhupeng.fxbase;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhupeng
 * @date 2017/9/16
 */

public final class ManifestParser {

    private static final String INJECTOR = "Injector";

    private ManifestParser() {
    }

    /**
     * 解析清单文件，拿到所有的注入器
     *
     * @param context
     * @return
     */
    public static List<Injector> parse(@NonNull final Context context) {
        final List<Injector> modules = new ArrayList<>();
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                for (String key : appInfo.metaData.keySet()) {
                    if (INJECTOR.equals(appInfo.metaData.get(key))) {
                        modules.add(parseModule(key));
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("There is no meta-data for parsing, do you forget to configure it in the manifest file", e);
        }

        return modules;
    }

    private static Injector parseModule(final String className) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("No injector implementation class", e);
        }

        Object injector;
        try {
            injector = clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Unable to instantiate Injector implementation for " + clazz, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to instantiate Injector implementation for " + clazz, e);
        }

        if (!(injector instanceof Injector)) {
            throw new RuntimeException("Expected instanceof Injector, but found: " + injector);
        }
        return (Injector) injector;
    }
}
