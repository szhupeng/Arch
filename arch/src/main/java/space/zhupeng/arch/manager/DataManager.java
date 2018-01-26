package space.zhupeng.arch.manager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import space.zhupeng.arch.Provider;

/**
 * @author zhupeng
 * @date 2018/1/9
 */

public final class DataManager {

    private static DataManager sInstance;
    protected Context context;
    protected Provider<HttpHelper> mHttpHelperProvider;
    protected Provider<PreferenceHelper> mPreferenceHelperProvider;
    protected Provider<DBHelper> mDBHelperProvider;

    public static DataManager getInstance() {
        if (null == sInstance) {
            synchronized (DataManager.class) {
                if (null == sInstance) {
                    sInstance = new DataManager();
                }
            }
        }
        return sInstance;
    }

    private DataManager() {
    }

    public void initialize(final Context context,
                           final String baseUrl) {
        initialize(context, null, baseUrl, null, Context.MODE_PRIVATE, null, 1);
    }

    public void initialize(final Context context,
                           final String baseUrl,
                           final String prefsName) {
        initialize(context, null, baseUrl, prefsName, Context.MODE_PRIVATE, null, 1);
    }

    public void initialize(final Context context,
                           final HttpHelper.HeadersProvider provider,
                           final String baseUrl) {
        initialize(context, provider, baseUrl, null, Context.MODE_PRIVATE, null, 1);
    }

    public void initialize(final Context context,
                           final HttpHelper.HeadersProvider provider,
                           final String baseUrl,
                           final String prefsName) {
        initialize(context, provider, baseUrl, prefsName, Context.MODE_PRIVATE, null, 1);
    }

    public void initialize(final Context context,
                           final HttpHelper.HeadersProvider provider,
                           final String baseUrl,
                           final String prefsName,
                           final int mode) {
        initialize(context, provider, baseUrl, prefsName, mode, null, 1);
    }

    public void initialize(final Context context,
                           final HttpHelper.HeadersProvider provider,
                           final String baseUrl,
                           final String prefsName,
                           final int mode,
                           final String dbName) {
        initialize(context, provider, baseUrl, prefsName, mode, dbName, 1);
    }

    public void initialize(final Context context,
                           final HttpHelper.HeadersProvider provider,
                           final String baseUrl,
                           final String prefsName,
                           final int mode,
                           final String dbName,
                           final int dbVersion) {
        this.context = context.getApplicationContext();

        this.mHttpHelperProvider = new Provider<HttpHelper>() {
            @Override
            public HttpHelper get() {
                return HttpHelper.getInstance(provider, baseUrl);
            }
        };

        this.mPreferenceHelperProvider = new Provider<PreferenceHelper>() {
            @Override
            public PreferenceHelper get() {
                if (TextUtils.isEmpty(prefsName)) {
                    throw new RuntimeException("if you want to operate preference, you must pass prefsName when call initialize function");
                }
                return new PreferenceHelper(context, prefsName, mode);
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

    public final void baseUrl(@NonNull String baseUrl) {
        getHttpHelper().baseUrl(baseUrl);
    }

    public final HttpHelper getHttpHelper() {
        if (null == this.mHttpHelperProvider) {
            throw new RuntimeException("You must call initialize function firstly");
        }
        return this.mHttpHelperProvider.get();
    }

    public final PreferenceHelper getPreferenceHelper() {
        if (null == this.mHttpHelperProvider) {
            throw new RuntimeException("You must call initialize function firstly");
        }
        return this.mPreferenceHelperProvider.get();
    }

    public final DBHelper getDBHelper() {
        if (null == this.mHttpHelperProvider) {
            throw new RuntimeException("You must call initialize function firstly");
        }
        return this.mDBHelperProvider.get();
    }

    public final void setDebuggable(boolean debuggable) {
        getHttpHelper().setDebuggable(debuggable);
    }
}
