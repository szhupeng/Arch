package space.zhupeng.arch.utils;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.math.BigDecimal;

/**
 * 机身存储空间相关的工具类
 *
 * @author zhupeng
 * @date 2017/8/18
 */
@SuppressWarnings("all")
public class MemoryUtils {

    private MemoryUtils() {
        throw new UnsupportedOperationException("this method can't be called");
    }

    /**
     * 计算剩余空间
     *
     * @param path
     * @return
     */
    public static double getAvailableSize(String path) {
        StatFs fileStats = new StatFs(path);
        fileStats.restat(path);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return fileStats.getAvailableBlocksLong() * fileStats.getBlockSizeLong();
        } else {
            return fileStats.getAvailableBlocks() * fileStats.getBlockSize();
        }
    }

    /**
     * 计算总空间
     *
     * @param path
     * @return
     */
    public static double getTotalSize(String path) {
        StatFs fileStats = new StatFs(path);
        fileStats.restat(path);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return fileStats.getBlockCountLong() * fileStats.getBlockSizeLong();
        } else {
            return fileStats.getBlockCount() * fileStats.getBlockSize();
        }
    }

    /**
     * 计算SD卡的剩余空间
     *
     * @return 剩余空间
     */
    public static double getSDAvailableSize() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return getAvailableSize(Environment.getExternalStorageDirectory().getPath());
        }

        return 0;
    }

    /**
     * 计算系统的剩余空间
     *
     * @return 剩余空间
     */
    public static double getSystemAvailableSize() {
        return getAvailableSize("/data");
    }

    /**
     * 是否有足够的空间
     *
     * @param filePath 文件路径，不是目录的路径
     * @return
     */
    public static boolean hasEnoughMemory(String filePath) {
        File file = new File(filePath);
        long length = file.length();
        if (filePath.startsWith("/sdcard") || filePath.startsWith("/mnt/sdcard")) {
            return getSDAvailableSize() > length;
        } else {
            return getSystemAvailableSize() > length;
        }
    }

    /**
     * 获取SD卡的总空间
     *
     * @return
     */
    public static double getSDTotalSize() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return getTotalSize(Environment.getExternalStorageDirectory().getPath());
        }

        return 0;
    }

    /**
     * 获取系统可读写的总空间
     *
     * @return
     */
    public static double getSystemTotalSize() {
        return getTotalSize("/data");
    }

    /**
     * 格式化单位
     * 转换为B,GB等等
     *
     * @param size
     * @return
     */
    public static String formatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(1, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(1, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(1, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(1, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }
}
