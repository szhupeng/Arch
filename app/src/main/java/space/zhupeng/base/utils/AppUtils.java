package space.zhupeng.base.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;

import java.util.List;

/**
 * App相关工具类
 *
 * @author zhupeng
 * @date 2016/12/11
 */

public final class AppUtils {

    private AppUtils() {
        throw new UnsupportedOperationException("this method can't be called");
    }

    /**
     * 获取App包名
     *
     * @param context
     * @return App包名
     */
    public static String getAppPackageName(final Context context) {
        return context.getApplicationContext().getPackageName();
    }

    /**
     * 获取App名称
     *
     * @param context
     * @return App名称
     */
    public static String getAppName(final Context context) {
        return getAppName(context, context.getPackageName());
    }

    /**
     * 获取App名称
     *
     * @param context
     * @param packageName 包名
     * @return App名称
     */
    public static String getAppName(final Context context, final String packageName) {
        if (isSpace(packageName)) return null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.applicationInfo.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取App图标
     *
     * @param context
     * @return App图标
     */
    public static Drawable getAppIcon(final Context context) {
        return getAppIcon(context, context.getPackageName());
    }

    /**
     * 获取App图标
     *
     * @param context
     * @param packageName 包名
     * @return App图标
     */
    public static Drawable getAppIcon(final Context context, final String packageName) {
        if (isSpace(packageName)) return null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.applicationInfo.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取App路径
     *
     * @param context
     * @return App路径
     */
    public static String getAppPath(final Context context) {
        return getAppPath(context, context.getPackageName());
    }

    /**
     * 获取App路径
     *
     * @param context
     * @param packageName 包名
     * @return App路径
     */
    public static String getAppPath(final Context context, final String packageName) {
        if (isSpace(packageName)) return null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.applicationInfo.sourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取App版本号
     *
     * @param context
     * @return App版本号
     */
    public static String getAppVersionName(final Context context) {
        return getAppVersionName(context, context.getPackageName());
    }

    /**
     * 获取App版本号
     *
     * @param context
     * @param packageName 包名
     * @return App版本号
     */
    public static String getAppVersionName(final Context context, final String packageName) {
        if (isSpace(packageName)) return null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取App版本码
     *
     * @param context
     * @return App版本码
     */
    public static int getAppVersionCode(final Context context) {
        return getAppVersionCode(context, context.getPackageName());
    }

    /**
     * 获取App版本码
     *
     * @param context
     * @param packageName 包名
     * @return App版本码
     */
    public static int getAppVersionCode(final Context context, final String packageName) {
        if (isSpace(packageName)) return -1;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? -1 : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 判断App是否是系统应用
     *
     * @param context
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isSystemApp(final Context context) {
        return isSystemApp(context, context.getPackageName());
    }

    /**
     * 判断App是否是系统应用
     *
     * @param context
     * @param packageName 包名
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isSystemApp(final Context context, final String packageName) {
        if (isSpace(packageName)) return false;
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            return ai != null && (ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断App是否是Debug版本
     *
     * @param context
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isAppDebug(final Context context) {
        return isAppDebug(context, context.getPackageName());
    }

    /**
     * 判断App是否是Debug版本
     *
     * @param context
     * @param packageName 包名
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isAppDebug(final Context context, final String packageName) {
        if (isSpace(packageName)) return false;
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            return ai != null && (ai.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取App签名
     *
     * @param context
     * @return App签名
     */
    public static Signature[] getAppSignature(final Context context) {
        return getAppSignature(context, context.getPackageName());
    }

    /**
     * 获取App签名
     *
     * @param context
     * @param packageName 包名
     * @return App签名
     */
    public static Signature[] getAppSignature(final Context context, final String packageName) {
        if (isSpace(packageName)) return null;
        try {
            PackageManager pm = context.getPackageManager();
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            return pi == null ? null : pi.signatures;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取应用签名的的SHA1值
     * <p>可据此判断高德，百度地图key是否正确</p>
     *
     * @param context
     * @return 应用签名的SHA1字符串, 比如：53:FD:54:DC:19:0F:11:AC:B5:22:9E:F1:1A:68:88:1B:8B:E8:54:42
     */
    public static String getAppSignatureSHA1(final Context context) {
        return getAppSignatureSHA1(context, context.getPackageName());
    }

    /**
     * 获取应用签名的的SHA1值
     * <p>可据此判断高德，百度地图key是否正确</p>
     *
     * @param context
     * @param packageName 包名
     * @return 应用签名的SHA1字符串, 比如：53:FD:54:DC:19:0F:11:AC:B5:22:9E:F1:1A:68:88:1B:8B:E8:54:42
     */
    public static String getAppSignatureSHA1(final Context context, final String packageName) {
        Signature[] signature = getAppSignature(context, packageName);
        if (signature == null) return null;
        return EncryptUtils.encryptSHA1ToString(signature[0].toByteArray()).
                replaceAll("(?<=[0-9A-F]{2})[0-9A-F]{2}", ":$0");
    }

    /**
     * 判断App是否处于前台
     *
     * @param context
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isAppForeground(final Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> info = manager.getRunningAppProcesses();
        if (info == null || info.size() == 0) return false;
        for (ActivityManager.RunningAppProcessInfo aInfo : info) {
            if (aInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return aInfo.processName.equals(context.getPackageName());
            }
        }
        return false;
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}