package space.zhupeng.arch.manager;

import android.content.Context;
import android.support.annotation.NonNull;

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

    public DataManager(Context context, HttpHelper.HeadersProvider provider, @NonNull final String prefsName) {
        this.context = context.getApplicationContext();

        initialize(provider, prefsName);
    }

    public void setDebuggable(boolean debuggable) {
        getHttpHelper().setDebuggable(debuggable);
    }

    protected void initialize(final HttpHelper.HeadersProvider provider, final String prefsName) {
        this.mHttpHelperProvider = new Provider<HttpHelper>() {
            @Override
            public HttpHelper get() {
                return new HttpHelper(provider);
            }
        };

        this.mPreferenceHelperProvider = new Provider<PreferenceHelper>() {
            @Override
            public PreferenceHelper get() {
                return new PreferenceHelper(context, prefsName);
            }
        };

        this.mDBHelperProvider = new Provider<DBHelper>() {
            @Override
            public DBHelper get() {
                return new DBHelper();
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
