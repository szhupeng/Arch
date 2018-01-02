package space.zhupeng.arch.task;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;

import space.zhupeng.arch.utils.ToastUtils;

/**
 * 显示加载框的异步任务
 *
 * @author zhupeng
 * @date 2017/8/17
 */

public abstract class BaseProgressAsyncTask extends BaseExceptionAsyncTask {

    private Dialog mProgressDialog;
    private CharSequence mMessage;

    public BaseProgressAsyncTask(Context context) {
        super(context);
    }

    public BaseProgressAsyncTask(Context context, final CharSequence message) {
        super(context);
        this.mMessage = message;
    }

    @Override
    protected void onPreExecute() {
        toShowDialog();
    }

    @Override
    protected void onSuccess() {
        final String msg = getSuccessMessage();
        if (!TextUtils.isEmpty(msg)) {
            ToastUtils.showShort(this.context, msg);
        }
    }

    protected void toShowDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            if (mProgressDialog instanceof ProgressDialog) {
                ((ProgressDialog) mProgressDialog).setMessage(mMessage);
            } else {
                try {
                    mProgressDialog.getClass()
                            .getMethod("setMessage", String.class)
                            .invoke(mProgressDialog, mMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            ProgressDialog dialog = new ProgressDialog(context);
            dialog.setIndeterminate(true);
            dialog.setMessage(mMessage);
            this.mProgressDialog = dialog;
            this.mProgressDialog.show();
        }
    }

    protected void toCloseDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    protected String getSuccessMessage() {
        return null;
    }
}
