package space.zhupeng.arch.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author zhupeng
 * @date 2018/1/9
 */

public class PreferenceHelper {

    private final Context context;
    private SharedPreferences mSharedPreferences;

    public PreferenceHelper(Context context, String prefsName) {
        this.context = context.getApplicationContext();
//        this.mSharedPreferences = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
    }

    protected void put(String key, Object value) {

    }
}
