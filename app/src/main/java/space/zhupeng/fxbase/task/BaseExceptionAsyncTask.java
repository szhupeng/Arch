package space.zhupeng.fxbase.task;

import android.content.Context;
import android.text.TextUtils;

import space.zhupeng.fxbase.utils.ToastUtils;

/**
 * 提示异常的异步任务
 *
 * @author zhupeng
 * @date 2017/8/17
 */

public abstract class BaseExceptionAsyncTask extends BaseAsyncTask {

    protected Context context;

    public BaseExceptionAsyncTask(final Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    protected void onError(final Exception e) {
        if (null == e || TextUtils.isEmpty(e.getMessage())) return;

        postSafely(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showShort(context, e.getMessage());
            }
        });
    }

    @Override
    protected void onFailure() {
    }
}
