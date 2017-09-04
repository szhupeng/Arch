package space.zhupeng.fxbase.config;

/**
 * @author zhupeng
 * @date 2016/12/21
 */

public class AppConfig {
    private static boolean isDebug = false;

    public static void setDebug(boolean debug) {
        isDebug = debug;
    }

    public static boolean isDebug() {
        return isDebug;
    }
}
