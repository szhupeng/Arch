package space.zhupeng.fxbase.executor;

/**
 * @author zhupeng
 * @date 2017/9/24
 */

public class ExecutorModule {

    Executor provideExecutor(ThreadExecutor threadExecutor) {
        return threadExecutor;
    }

    MainThread provideMainThread(MainThreadImpl mainThread) {
        return mainThread;
    }
}
