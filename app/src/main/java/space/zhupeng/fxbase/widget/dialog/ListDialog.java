package space.zhupeng.fxbase.widget.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;

import space.zhupeng.fxbase.R;
import space.zhupeng.fxbase.ui.List;

/**
 * @author zhupeng
 * @date 2016/12/4
 */

public class ListDialog<T> extends BaseDialogFragment {

    private List<T> mDataList;

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.dialog_list;
    }
}
