package space.zhupeng.fxbase.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import butterknife.Bind;
import space.zhupeng.fxbase.R;

/**
 * 包含Toolbar的Activity基类
 *
 * @author zhupeng
 * @date 2017/1/14
 */

public abstract class BaseToolbarActivity extends BaseActivity {

    @Nullable
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }
}
