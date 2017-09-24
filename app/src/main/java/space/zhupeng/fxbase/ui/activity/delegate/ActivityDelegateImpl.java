package space.zhupeng.fxbase.ui.activity.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * @author zhupeng
 * @date 2017/9/17
 */

public class ActivityDelegateImpl implements ActivityDelegate {

    private Activity mActivity;

    public ActivityDelegateImpl(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onDestroy() {
        this.mActivity = null;
    }
}
