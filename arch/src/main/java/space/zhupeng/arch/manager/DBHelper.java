package space.zhupeng.arch.manager;

import android.content.Context;

/**
 * @author zhupeng
 * @date 2018/1/9
 */

public class DBHelper {

    protected final Context context;
    protected final String mDbName;
    protected final int mDbVersion;

    public DBHelper(Context context, String dbName, int dbVersion) {
        this.context = context.getApplicationContext();
        this.mDbName = dbName;
        this.mDbVersion = dbVersion;
    }
}
