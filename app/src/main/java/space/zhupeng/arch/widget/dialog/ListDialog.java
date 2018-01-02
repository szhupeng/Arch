package space.zhupeng.arch.widget.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;

import space.zhupeng.arch.R;
import space.zhupeng.arch.ui.List;

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
