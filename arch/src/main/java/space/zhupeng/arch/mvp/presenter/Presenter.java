package space.zhupeng.arch.mvp.presenter;

import space.zhupeng.arch.mvp.view.BaseView;

/**
 * @author zhupeng
 * @date 2017/9/7
 */

public interface Presenter<V extends BaseView> {

    /**
     * Presenter与View建立连接
     *
     * @param mvpView
     */
    void attachView(V mvpView);

    /**
     * Presenter与View断开连接
     */
    void detachView();
}
