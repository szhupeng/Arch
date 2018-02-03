package space.zhupeng.arch.manager;

import android.content.Context;

/**
 * @author zhupeng
 * @date 2018/1/9
 */

public class DatabaseHelper {

    protected final Context context;
    protected final String mDatabaseName;
    protected final int mDatabaseVersion;

    public DatabaseHelper(Context context, String dbName, int dbVersion) {
        this.context = context.getApplicationContext();
        this.mDatabaseName = dbName;
        this.mDatabaseVersion = dbVersion;
    }
}
