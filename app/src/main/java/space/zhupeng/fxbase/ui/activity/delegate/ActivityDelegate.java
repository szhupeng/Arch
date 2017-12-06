package space.zhupeng.fxbase.ui.activity.delegate;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * @author zhupeng
 * @date 2017/9/13
 */

public interface ActivityDelegate {

    void onCreate(@Nullable Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onSaveInstanceState(Bundle outState);

    void onDestroy();
}
