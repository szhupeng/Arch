package space.zhupeng.base.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

/**
 * Toast工具类
 *
 * @author zhupeng
 * @date 2017/8/16
 */

public final class ToastUtils {

    private static final int DEFAULT_COLOR = 0x12000000;
    private static final Handler sHandler = new Handler(Looper.getMainLooper());
    private static Toast sToast;

    private ToastUtils() {
        throw new UnsupportedOperationException("this method can't be called");
    }

    /**
     * 安全地显示短时吐司
     *
     * @param context
     * @param text    文本
     */
    public static void showShortSafely(@NonNull final Context context, @NonNull final CharSequence text) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                show(context, text, Toast.LENGTH_SHORT);
            }
        });
    }

    /**
     * 安全地显示短时吐司
     *
     * @param context
     * @param resId   资源Id
     */
    public static void showShortSafely(@NonNull final Context context, @StringRes final int resId) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                show(context, resId, Toast.LENGTH_SHORT);
            }
        });
    }

    /**
     * 安全地显示长时吐司
     *
     * @param context
     * @param text    文本
     */
    public static void showLongSafely(@NonNull final Context context, @NonNull final CharSequence text) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                show(context, text, Toast.LENGTH_LONG);
            }
        });
    }

    /**
     * 安全地显示长时吐司
     *
     * @param context
     * @param resId   资源Id
     */
    public static void showLongSafely(@NonNull final Context context, @StringRes final int resId) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                show(context, resId, Toast.LENGTH_LONG);
            }
        });
    }

    /**
     * 显示短时吐司
     *
     * @param context
     * @param text    文本
     */
    public static void showShort(@NonNull final Context context, @NonNull final CharSequence text) {
        show(context, text, Toast.LENGTH_SHORT);
    }

    /**
     * 显示短时吐司
     *
     * @param context
     * @param resId   资源Id
     */
    public static void showShort(@NonNull final Context context, @StringRes final int resId) {
        show(context, resId, Toast.LENGTH_SHORT);
    }

    /**
     * 显示长时吐司
     *
     * @param context
     * @param text    文本
     */
    public static void showLong(@NonNull final Context context, @NonNull final CharSequence text) {
        show(context, text, Toast.LENGTH_LONG);
    }

    /**
     * 显示长时吐司
     *
     * @param context
     * @param resId   资源Id
     */
    public static void showLong(@NonNull final Context context, @StringRes final int resId) {
        show(context, resId, Toast.LENGTH_LONG);
    }

    /**
     * 安全地显示短时自定义吐司
     *
     * @param context
     */
    public static void showCustomShortSafely(@NonNull final Context context, @LayoutRes final int layoutId) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                showCustom(context, layoutId, Toast.LENGTH_SHORT);
            }
        });
    }

    /**
     * 安全地显示长时自定义吐司
     *
     * @param context
     */
    public static void showCustomLongSafely(@NonNull final Context context, @LayoutRes final int layoutId) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                showCustom(context, layoutId, Toast.LENGTH_LONG);
            }
        });
    }

    /**
     * 显示短时自定义吐司
     *
     * @param context
     */
    public static void showCustomShort(@NonNull final Context context, @LayoutRes final int layoutId) {
        showCustom(context, layoutId, Toast.LENGTH_SHORT);
    }

    /**
     * 显示长时自定义吐司
     *
     * @param context
     */
    public static void showCustomLong(@NonNull final Context context, @LayoutRes final int layoutId) {
        showCustom(context, layoutId, Toast.LENGTH_LONG);
    }

    /**
     * 安全地显示短时自定义吐司
     *
     * @param context
     */
    public static void showCustomShortSafely(@NonNull final Context context, @NonNull final View view) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                showCustom(context, view, Toast.LENGTH_SHORT);
            }
        });
    }

    /**
     * 安全地显示长时自定义吐司
     *
     * @param context
     */
    public static void showCustomLongSafely(@NonNull final Context context, @NonNull final View view) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                showCustom(context, view, Toast.LENGTH_LONG);
            }
        });
    }

    /**
     * 显示短时自定义吐司
     *
     * @param context
     */
    public static void showCustomShort(@NonNull final Context context, @NonNull final View view) {
        showCustom(context, view, Toast.LENGTH_SHORT);
    }

    /**
     * 显示长时自定义吐司
     */
    public static void showCustomLong(@NonNull final Context context, @NonNull final View view) {
        showCustom(context, view, Toast.LENGTH_LONG);
    }

    /**
     * 显示吐司
     *
     * @param resId    资源Id
     * @param duration 显示时长
     */
    private static void show(@NonNull final Context context, @StringRes final int resId, final int duration) {
        show(context, context.getResources().getText(resId).toString(), duration);
    }

    /**
     * 显示吐司
     *
     * @param context
     * @param text     文本
     * @param duration 显示时长
     */
    private static void show(@NonNull final Context context, final CharSequence text, final int duration) {
        if (TextUtils.isEmpty(text)) return;

        cancel();

        sToast = Toast.makeText(context, text, duration);
        sToast.setGravity(Gravity.CENTER, 0, 0);
        sToast.show();
    }

    /**
     * 显示自定义吐司
     *
     * @param context
     * @param layoutId
     * @param duration
     */
    private static void showCustom(@NonNull final Context context, @LayoutRes final int layoutId, final int duration) {
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        showCustom(context, inflate.inflate(layoutId, null), duration);
    }

    /**
     * 显示自定义吐司
     *
     * @param context
     * @param view
     * @param duration
     */
    private static void showCustom(@NonNull final Context context, @NonNull final View view, final int duration) {
        if (null == view) return;
        
        cancel();

        sToast = new Toast(context);
        sToast.setView(view);
        sToast.setDuration(duration);
        sToast.setGravity(Gravity.CENTER, 0, 0);
        sToast.show();
    }

    /**
     * 取消吐司显示
     */
    public static void cancel() {
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }
    }
}
