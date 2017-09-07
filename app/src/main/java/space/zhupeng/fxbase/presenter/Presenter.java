package space.zhupeng.fxbase.presenter;

import space.zhupeng.fxbase.view.BaseMvpView;

/**
 * @author zhupeng
 * @date 2017/9/7
 */

public interface Presenter<V extends BaseMvpView> {

    /**
     * Presenter与View建立连接
     *
     * @param view
     */
    void attachView(V view);

    /**
     * Presenter与View断开连接
     */
    void detachView();
}
