package space.zhupeng.fxbase.mvp.presenter;

/**
 * @author zhupeng
 * @date 2017/9/7
 */

public interface PresenterFactory<P extends Presenter> {
    P create();
}
