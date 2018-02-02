package space.zhupeng.arch.widget.dialog;

import android.content.Context;
import android.support.annotation.StringRes;

/**
 * @author zhupeng
 * @date 2018/2/2
 */

public interface DialogService {
    void showProgressDialog(Context context, CharSequence message);

    void showProgressDialog(Context context, @StringRes int resId);

    void showProgressDialog(Context context);

    void closeDialog();
}
