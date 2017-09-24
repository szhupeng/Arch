package space.zhupeng.fxbase.executor;

/**
 * 将执行上下文从任何线程更改为UI线程
 *
 * @author zhupeng
 * @date 2017/9/24
 */

public interface MainThread {
    void post(final Runnable runnable);
}
