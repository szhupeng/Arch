package space.zhupeng.arch.analysis;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Map;

/**
 * @author zhupeng
 * @date 2017/3/27
 */

public class Analysis {

    public void onEvent(@NonNull final String key) {
        onEvent(key, null);
    }

    public void onEvent(@NonNull final String key, final Map<String, String> params) {
    }

    public static void reportError(@NonNull final Context context, final Throwable throwable) {
    }
}
