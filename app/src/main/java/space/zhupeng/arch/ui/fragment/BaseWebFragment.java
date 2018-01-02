package space.zhupeng.arch.ui.fragment;

import android.support.v4.content.Loader;

/**
 * @author zhupeng
 * @date 2017/12/26
 */

public class BaseWebFragment extends BaseToolbarFragment {

    @Override
    protected int getLayoutResId() {
        return 0;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
    }
}
