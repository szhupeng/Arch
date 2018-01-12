package space.zhupeng.arch.manager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import space.zhupeng.arch.Provider;

/**
 * 子类实现为单例模式
 *
 * @author zhupeng
 * @date 2018/1/9
 */

public class DataManager {

    protected Context context;
    protected Provider<HttpHelper> mHttpHelperProvider;
    protected Provider<PreferenceHelper> mPreferenceHelperProvider;
    protected Provider<DBHelper> mDBHelperProvider;

    protected DataManager(Context context, HttpHelper.HeadersProvider provider, @NonNull final String prefsName) {
        this.context = context.getApplicationContext();

        initialize(provider, prefsName);
    }

    public void setDebuggable(boolean debuggable) {
        getHttpHelper().setDebuggable(debuggable);
    }

    protected void initialize(final HttpHelper.HeadersProvider provider, final String prefsName) {
        initialize(provider, prefsName, null);
    }

    protected void initialize(final HttpHelper.HeadersProvider provider, final String prefsName, final String dbName) {
        initialize(provider, prefsName, dbName, 1);
    }

    protected void initialize(final HttpHelper.HeadersProvider provider, final String prefsName, final String dbName, final int dbVersion) {
        this.mHttpHelperProvider = new Provider<HttpHelper>() {
            @Override
            public HttpHelper get() {
                return new HttpHelper(provider);
            }
        };

        this.mPreferenceHelperProvider = new Provider<PreferenceHelper>() {
            @Override
            public PreferenceHelper get() {
                if (TextUtils.isEmpty(prefsName)) {
                    throw new RuntimeException("if you want to operate preference, you must pass prefsName when call initialize function");
                }
                return new PreferenceHelper(context, prefsName);
            }
        };

        this.mDBHelperProvider = new Provider<DBHelper>() {
            @Override
            public DBHelper get() {
                if (TextUtils.isEmpty(dbName)) {
                    throw new RuntimeException("if you want to operate database, you must pass dbName when call initialize function");
                }

                return new DBHelper(context, dbName, dbVersion);
            }
        };
    }

    public final HttpHelper getHttpHelper() {
        return this.mHttpHelperProvider.get();
    }

    public final PreferenceHelper getPreferenceHelper() {
        return this.mPreferenceHelperProvider.get();
    }

    public final DBHelper getDBHelper() {
        return this.mDBHelperProvider.get();
    }
}
