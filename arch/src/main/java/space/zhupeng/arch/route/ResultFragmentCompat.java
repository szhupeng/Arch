package space.zhupeng.arch.route;

import android.content.Intent;

/**
 * 屏蔽Fragment版本差异
 *
 * @author zhupeng
 * @date 2016/12/11
 */
public interface ResultFragmentCompat {
    void startActivityForResult(Intent intent, Router.Callback callback);
}
