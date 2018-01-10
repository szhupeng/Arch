package space.zhupeng.arch.manager;

import android.content.Context;

/**
 * @author zhupeng
 * @date 2018/1/9
 */

public class DataManager {

    private static DataManager sInstance;

    protected Context context;
    protected final HttpHelper mHttpHelper;
    protected final PreferenceHelper mPreferenceHelper;
    protected final DBHelper mDBHelper;

    private DataManager(Context context) {
        this.context = context;
        this.mHttpHelper = new HttpHelper();
        this.mPreferenceHelper = new PreferenceHelper(context, null);
        this.mDBHelper = new DBHelper();
    }

    public static DataManager obtain(Context context) {
        if (null == sInstance) {
            synchronized (DataManager.class) {
                if (null == sInstance) {
                    sInstance = new DataManager(context.getApplicationContext());
                }
            }
        }

        return sInstance;
    }
}
