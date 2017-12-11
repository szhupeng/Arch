package space.zhupeng.fxbase.widget.dialog;

import android.view.View;

import space.zhupeng.fxbase.R;
import space.zhupeng.fxbase.ui.List;

/**
 * @author zhupeng
 * @date 2016/12/4
 */

public class ListDialog<T> extends BaseDialogFragment {

    private List<T> mDataList;

    @Override
    protected void initView(View view) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_list;
    }
}
