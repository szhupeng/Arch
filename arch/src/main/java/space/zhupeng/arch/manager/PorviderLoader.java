package space.zhupeng.arch.manager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

/**
 * @author zhupeng
 * @date 2017/12/29
 */

public class PorviderLoader extends AsyncTaskLoader<Cursor> {

    private Loader.ForceLoadContentObserver mObserver = new Loader.ForceLoadContentObserver();
    private SQLiteOpenHelper helper;

    public PorviderLoader(Context context, SQLiteOpenHelper helper) {
        super(context);
    }

    @Override
    public final Cursor loadInBackground() {
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = query(database);
        if (cursor != null) {
            cursor.registerContentObserver(mObserver);
            cursor.setNotificationUri(getContext().getContentResolver(), getNotificationUri());
        }
        return cursor;
    }

    protected Cursor query(SQLiteDatabase database) {
        return null;
    }

    protected Uri getNotificationUri() {
        return null;
    }
}
