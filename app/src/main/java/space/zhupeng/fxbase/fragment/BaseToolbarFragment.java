package space.zhupeng.fxbase.fragment;

import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import butterknife.Bind;
import space.zhupeng.base.R;

/**
 * 如果项目使用的是单Activity多Fragment，建议继承此类
 *
 * @author zhupeng
 * @date 2017/9/4
 */

public abstract class BaseToolbarFragment extends BaseFragment {

    @Nullable
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
}
