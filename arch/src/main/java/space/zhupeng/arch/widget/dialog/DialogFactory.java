package space.zhupeng.arch.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.StringRes;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author zhupeng
 * @date 2017/9/4
 */

public class DialogFactory {
    private static DialogFactory sInstance;
    private DialogService mDialogService;
    private WeakReference<Dialog> mDialogRef;

    private DialogFactory() {
        ServiceLoader<DialogService> loader = ServiceLoader.load(DialogService.class);
        Iterator<DialogService> iterator = loader.iterator();
        while (iterator.hasNext()) {
            mDialogService = iterator.next();
        }
    }

    public static DialogFactory create() {
        if (null == sInstance) {
            sInstance = new DialogFactory();
        }
        return sInstance;
    }

    public void showProgressDialog(Context context, CharSequence message) {
        if (null == this.mDialogService) {
            MaterialProgressDialog dialog = new MaterialProgressDialog(context);
            dialog.setMessage(message);
            dialog.show();
            mDialogRef = new WeakReference<Dialog>(dialog);
        } else {
            this.mDialogService.showProgressDialog(context, message);
        }
    }

    public void showProgressDialog(Context context, @StringRes int resId) {
        if (null == this.mDialogService) {
            MaterialProgressDialog dialog = new MaterialProgressDialog(context);
            dialog.setMessage(resId);
            dialog.show();
            mDialogRef = new WeakReference<Dialog>(dialog);
        } else {
            this.mDialogService.showProgressDialog(context, resId);
        }
    }

    public void showProgressDialog(Context context) {
        if (null == this.mDialogService) {
            MaterialProgressDialog dialog = new MaterialProgressDialog(context);
            dialog.setMessage(null);
            dialog.show();
            mDialogRef = new WeakReference<Dialog>(dialog);
        } else {
            this.mDialogService.showProgressDialog(context);
        }
    }

    /**
     * 关闭对话框
     */
    public void closeDialog() {
        if (null == this.mDialogService) {
            if (null == mDialogRef || null == mDialogRef.get()) return;

            mDialogRef.get().dismiss();
            mDialogRef.clear();
        } else {
            this.mDialogService.closeDialog();
        }
    }
}
