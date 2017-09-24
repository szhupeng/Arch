package space.zhupeng.fxbase.executor;

/**
 * 使用这个类来执行一个Interactor
 * 交互者不应该知道Executor或MainThread依赖关系
 * Interactors客户端代码应该得到一个Executor实例来执行交互器
 *
 * @author zhupeng
 * @date 2017/9/24
 */

public interface Executor {
    void run(final Interactor interactor);
}
