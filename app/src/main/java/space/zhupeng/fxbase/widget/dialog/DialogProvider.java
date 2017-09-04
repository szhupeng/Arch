package space.zhupeng.fxbase.widget.dialog;

import android.app.Dialog;
import android.support.annotation.LayoutRes;

/**
 * @author zhupeng
 * @date 2017/9/4
 */

public class DialogProvider {

    private static Dialog sDialog;

    private DialogProvider() {
    }

    public static void showMessageProgress(CharSequence message) {

    }

    public static void showSimpleProgress() {

    }

    public static void showCustomDialog(@LayoutRes int viewId) {

    }

    /**
     * 关闭对话框
     */
    public static void dismissDialog() {
        if (null == sDialog || !sDialog.isShowing()) return;

        sDialog.cancel();
    }
}
