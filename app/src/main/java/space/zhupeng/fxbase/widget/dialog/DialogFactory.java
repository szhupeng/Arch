package space.zhupeng.fxbase.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentManager;

/**
 * @author zhupeng
 * @date 2017/9/4
 */

public class DialogFactory {

    private static Dialog sDialog;

    private DialogFactory() {
    }

    public static void showProgressDialog(Context context, CharSequence message) {
        MaterialProgressDialog dialog = new MaterialProgressDialog(context);
        dialog.setMessage(message);
        dialog.show();
    }

    public static void showProgressDialog(Context context, @StringRes int resId) {
        MaterialProgressDialog dialog = new MaterialProgressDialog(context);
        dialog.setMessage(resId);
        dialog.show();
    }

    public static void showProgressDialog(Context context) {
        MaterialProgressDialog dialog = new MaterialProgressDialog(context);
        dialog.setMessage(null);
        dialog.show();
    }

    public static void showSimpleDialog(FragmentManager fm, @LayoutRes int viewId) {
        SimpleDialog dialog = new SimpleDialog();
        dialog.show(fm);
    }

    public static void showListDialog(FragmentManager fm, @LayoutRes int viewId) {
        ListDialog dialog = new ListDialog();
        dialog.show(fm);
    }

    public static void showCustomDialog(FragmentManager fm, @LayoutRes int viewId) {
    }

    /**
     * 关闭对话框
     */
    public static void dismissDialog() {
        if (null == sDialog || !sDialog.isShowing()) return;

        sDialog.cancel();
    }
}
