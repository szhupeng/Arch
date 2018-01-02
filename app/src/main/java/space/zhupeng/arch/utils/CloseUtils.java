package space.zhupeng.arch.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * 关闭流相关工具类
 *
 * @author zhupeng
 * @date 2016/12/11
 */

public final class CloseUtils {

    private CloseUtils() {
        throw new UnsupportedOperationException("this method can't be called");
    }

    /**
     * 关闭IO
     *
     * @param closeables closeables
     */
    public static void closeIO(final Closeable... closeables) {
        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 安静关闭IO
     *
     * @param closeables closeables
     */
    public static void closeIOQuietly(final Closeable... closeables) {
        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
