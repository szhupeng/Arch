package space.zhupeng.fxbase.widget.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import space.zhupeng.fxbase.R;

/**
 * @author zhupeng
 * @date 2016/12/4
 */

public class SimpleDialog extends BaseDialogFragment {

    private TextView tvTitle;
    private TextView tvMessage;


    @Override
    protected int getLayoutResID() {
        return R.layout.dialog_simple;
    }
}
