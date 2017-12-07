package space.zhupeng.fxbase.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import space.zhupeng.fxbase.R;
import space.zhupeng.fxbase.utils.ActionModeHelper;

/**
 * 包含Toolbar的Activity基类
 *
 * @author zhupeng
 * @date 2017/1/14
 */

public abstract class BaseToolbarActivity extends BaseActivity {

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    protected ActionModeHelper mActionModeHelper;

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    protected void setCenterTitle(CharSequence title) {

    }

    protected int getContextMenuResID() {
        return 0;
    }
}
