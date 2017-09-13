package space.zhupeng.fxbase.permission;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;

/**
 * @author zhupeng
 * @date 2017/9/12
 */

class PermissionsDispatcher {

    public static void dispatch(@NonNull Object host, @NonNull String rationale,
                                @StringRes int positive, @StringRes int negative,
                                int requestCode, @NonNull String[] permissions) {
        if (host instanceof AppCompatActivity) {

        } else if (host instanceof Activity) {

        } else {

        }
    }
}
