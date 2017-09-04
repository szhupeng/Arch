package space.zhupeng.fxbase.task;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import space.zhupeng.fxbase.utils.Utils;

/**
 * 异步任务基类
 *
 * @author zhupeng
 * @date 2017/8/17
 */

public abstract class BaseAsyncTask extends AsyncTask<Void, Void, Boolean> {

    private final Handler mHandler;

    public BaseAsyncTask() {
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            return doInBackground();
        } catch (Exception e) {
            onError(e);
            return false;
        }
    }

    @Override
    protected void onPostExecute(final Boolean result) {
        postSafely(new Runnable() {
            @Override
            public void run() {
                onCompleted();
                if (result) {
                    onSuccess();
                } else {
                    onFailure();
                }
            }
        });
    }

    protected void postSafely(final Runnable runnable) {
        Utils.postSafely(mHandler, runnable);
    }

    protected void onCompleted() {
    }

    protected void onError(final Exception e) {
    }

    protected abstract boolean doInBackground() throws Exception;

    protected abstract void onSuccess();

    protected abstract void onFailure();
}
