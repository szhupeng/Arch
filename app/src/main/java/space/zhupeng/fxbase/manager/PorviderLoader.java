package space.zhupeng.fxbase.manager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

/**
 * @author zhupeng
 * @date 2017/12/29
 */

public class PorviderLoader<D> extends AsyncTaskLoader<D> {

    private Loader.ForceLoadContentObserver mObserver = new Loader.ForceLoadContentObserver();
    private SQLiteOpenHelper helper;

    public PorviderLoader(Context context, SQLiteOpenHelper helper) {
        super(context);
    }

    @Override
    public D loadInBackground() {
//        SQLiteDatabase database = helper.getReadableDatabase();
//        Cursor cursor = database.query();
//        if (cursor != null) {
//            cursor.registerContentObserver(mObserver);
//            cursor.setNotificationUri(getContext().getContentResolver(), uri);
//        }
        return null;
    }
}
