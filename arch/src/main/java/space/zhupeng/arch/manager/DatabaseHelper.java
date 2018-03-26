package space.zhupeng.arch.manager;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

/**
 * @author zhupeng
 * @date 2018/1/9
 */

public class DatabaseHelper implements Handler.Callback {

    protected final Context context;
    protected final String mDatabaseName;
    protected final int mDatabaseVersion;

    private final HandlerThread mReadWriteThread;
    private final Handler mReadWriteHandler;

    public DatabaseHelper(Context context, String dbName, int dbVersion) {
        this.context = context.getApplicationContext();
        this.mDatabaseName = dbName;
        this.mDatabaseVersion = dbVersion;

        this.mReadWriteThread = new HandlerThread("ReadWriteDbThread");
        this.mReadWriteThread.start();
        this.mReadWriteHandler = new Handler(mReadWriteThread.getLooper(), this);
    }

    public void postSafely(final Runnable runnable) {
        if (runnable != null) {
            this.mReadWriteHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        runnable.run();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
