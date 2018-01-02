package space.zhupeng.arch.executor;

import android.os.Handler;
import android.os.Looper;

/**
 * @author zhupeng
 * @date 2017/9/24
 */

class MainThreadImpl implements MainThread {

    private Handler handler;

    MainThreadImpl() {
        this.handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void post(Runnable runnable) {
        handler.post(runnable);
    }
}
