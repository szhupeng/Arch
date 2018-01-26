package space.zhupeng.arch.mvp.view;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.View;

/**
 * MVP模式中View基类
 *
 * @author zhupeng
 * @date 2017/8/18
 */

public interface BaseView {
    <C extends Context> C getGenericContext();

    void showToast(@NonNull final CharSequence text);

    void showToast(@StringRes final int resId);

    void showCustomToast(@LayoutRes final int layoutId);

    void showCustomToast(@NonNull final View view);

    void showMessageProgress(final CharSequence message);

    void showMessageProgress(@StringRes final int resId);

    void showSimpleProgress();

    void closeDialog();
}
