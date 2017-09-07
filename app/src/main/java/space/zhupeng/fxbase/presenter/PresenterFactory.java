package space.zhupeng.fxbase.presenter;

/**
 * @author zhupeng
 * @date 2017/9/7
 */

public interface PresenterFactory<P extends Presenter> {
    P create();
}
