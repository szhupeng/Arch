package space.zhupeng.arch.utils;

import android.text.TextUtils;
import android.util.Log;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class FormatUtils {

    public static final String FORMAT_SLASH_YMD_HMS = "yyyy/MM/dd HH:mm:ss";
    public static final String FORMAT_SLASH_YMD_HM = "yyyy/MM/dd HH:mm";
    public static final String FORMAT_SlASH_YMD = "yyyy/MM/dd";
    public static final String FORMAT_SLASH_YM = "yyyy/MM";
    public static final String FORMAT_SLASH_MD_HMS = "MM/dd HH:mm:ss";
    public static final String FORMAT_SLASH_MD_HM = "MM/dd HH:mm";
    public static final String FORMAT_SLASH_MD = "MM/dd";
    public static final String FORMAT_BARS_YMD_HMS = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_BARS_YMD_HM = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_BARS_YMD = "yyyy-MM-dd";
    public static final String FORMAT_BARS_YM = "yyyy-MM";
    public static final String FORMAT_BARS_MD_HMS = "MM-dd HH:mm:ss";
    public static final String FORMAT_BARS_MD_HM = "MM-dd HH:mm";
    public static final String FORMAT_BARS_MD = "MM-dd";
    public static final String FORMAT_CN_YMD_HMS = "yyyy年MM月dd日 HH:mm:ss";
    public static final String FORMAT_CN_YMD_HM = "yyyy年MM月dd日 HH:mm";
    public static final String FORMAT_CN_YMD = "yyyy年MM月dd日";
    public static final String FORMAT_CN_YM = "yyyy年MM月";
    public static final String FORMAT_CN_MD_HMS = "MM月dd日 HH:mm:ss";
    public static final String FORMAT_CN_MD_HM = "MM月dd日 HH:mm";
    public static final String FORMAT_CN_MD = "MM月dd日";

    private FormatUtils() {
        throw new UnsupportedOperationException("this method can't be called");
    }

    public static String formatDate(long timeMillis, String format) {
        if (TextUtils.isEmpty(format)) {
            throw new IllegalArgumentException("Please specify the conversion format");
        } else {
            return (new SimpleDateFormat(format)).format(new Date(timeMillis));
        }
    }

    public static String formatDate(String dateStr, String srcFormat, String destFormat) {
        try {
            Date date = (new SimpleDateFormat(srcFormat)).parse(dateStr);
            return (new SimpleDateFormat(destFormat)).format(date);
        } catch (ParseException e) {
            Log.e("FormatUtils", e.getMessage());
            return null;
        }
    }

    public static Calendar formatDate(String dateStr, String srcFormat) {
        try {
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.setTime((new SimpleDateFormat(srcFormat)).parse(dateStr));
            return calendar;
        } catch (ParseException e) {
            Log.e("FormatUtils", e.getMessage());
            return null;
        }
    }

    public static String formatDate(int value, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, value);
        return (new SimpleDateFormat(format)).format(calendar.getTime());
    }

    public static String formatNumber(double value, int scale) {
        BigDecimal bd = new BigDecimal(value);
        return Double.toString(bd.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue());
    }

    public static String formatNumber(double value) {
        return formatNumber(value, false);
    }

    public static String formatNumber(double value, boolean comma) {
        return formatNumber(value, comma ? "###,##0.###" : "0.###");
    }

    public static String formatBigNumber(double value) {
        return formatNumber(value, "###,###");
    }

    public static String formatNumber(double value, String pattern) {
        try {
            DecimalFormat nf = new DecimalFormat(pattern);
            return nf.format(value);
        } catch (Exception e) {
            Log.e("FormatUtils", e.getMessage());
            return null;
        }
    }
}
