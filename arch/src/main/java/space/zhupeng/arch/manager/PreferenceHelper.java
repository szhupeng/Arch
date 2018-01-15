package space.zhupeng.arch.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.util.Map;

/**
 * @author zhupeng
 * @date 2018/1/9
 */

public class PreferenceHelper {

    protected final Context context;
    private final SharedPreferences mPrefs;

    public PreferenceHelper(Context context, String prefsName, int mode) {
        if (mode != Context.MODE_PRIVATE
                && mode != Context.MODE_WORLD_READABLE
                && mode != Context.MODE_WORLD_WRITEABLE
                && mode != Context.MODE_MULTI_PROCESS) {
            throw new IllegalArgumentException("The mode is invalid");
        }

        this.context = context.getApplicationContext();
        if (!TextUtils.isEmpty(prefsName)) {
            this.mPrefs = context.getSharedPreferences(prefsName, mode);
        } else {
            this.mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        }
    }

    @SuppressWarnings("WeakerAccess")
    public final SharedPreferences getPreferences() {
        if (mPrefs != null) {
            return mPrefs;
        }
        throw new RuntimeException(
                "Prefs class not correctly instantiated. Please call Builder.setContext().build() in the Application class onCreate.");
    }

    public final Map<String, ?> getAll() {
        return getPreferences().getAll();
    }

    protected final int getInt(final String key, final int defValue) {
        return getPreferences().getInt(key, defValue);
    }

    protected final boolean getBoolean(final String key, final boolean defValue) {
        return getPreferences().getBoolean(key, defValue);
    }

    protected final long getLong(final String key, final long defValue) {
        return getPreferences().getLong(key, defValue);
    }

    protected final double getDouble(final String key, final double defValue) {
        return Double.longBitsToDouble(getPreferences().getLong(key, Double.doubleToLongBits(defValue)));
    }

    protected final float getFloat(final String key, final float defValue) {
        return getPreferences().getFloat(key, defValue);
    }

    protected final String getString(final String key, final String defValue) {
        return getPreferences().getString(key, defValue);
    }

    protected final void putLong(final String key, final long value) {
        final SharedPreferences.Editor editor = getPreferences().edit();
        editor.putLong(key, value);
        editor.apply();
    }

    protected final void putInt(final String key, final int value) {
        final SharedPreferences.Editor editor = getPreferences().edit();
        editor.putInt(key, value);
        editor.apply();
    }

    protected final void putDouble(final String key, final double value) {
        final SharedPreferences.Editor editor = getPreferences().edit();
        editor.putLong(key, Double.doubleToRawLongBits(value));
        editor.apply();
    }

    protected final void putFloat(final String key, final float value) {
        final SharedPreferences.Editor editor = getPreferences().edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    protected final void putBoolean(final String key, final boolean value) {
        final SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    protected final void putString(final String key, final String value) {
        final SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(key, value);
        editor.apply();
    }

    public final void remove(final String key) {
        SharedPreferences prefs = getPreferences();
        final SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.apply();
    }

    public final boolean contains(final String key) {
        return getPreferences().contains(key);
    }

    public final SharedPreferences.Editor clear() {
        final SharedPreferences.Editor editor = getPreferences().edit().clear();
        editor.apply();
        return editor;
    }

    public final SharedPreferences.Editor edit() {
        return getPreferences().edit();
    }
}
