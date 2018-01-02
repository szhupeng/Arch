package space.zhupeng.arch.widget.dialog;

import android.widget.TextView;

import space.zhupeng.arch.R;

/**
 * @author zhupeng
 * @date 2016/12/4
 */

public class SimpleDialog extends BaseDialogFragment {

    private TextView tvTitle;
    private TextView tvMessage;

    @Override
    protected int getLayoutResId() {
        return R.layout.dialog_simple;
    }
}
