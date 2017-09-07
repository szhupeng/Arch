package space.zhupeng.fxbase.presenter;

import space.zhupeng.fxbase.view.BaseMvpView;

/**
 * MVP模式中Presenter基类
 *
 * @author zhupeng
 * @date 2017/8/18
 */

public class BasePresenter<M, V extends BaseMvpView> implements Presenter<V> {

    protected M mModel;
    protected V mView;

    public BasePresenter(M model) {
        this.mModel = model;
    }

    @Override
    public void attachView(V view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    /**
     * 检查是否与View建立连接
     */
    public void checkViewAttached() {
        if (null == this.mView) {
            throw new RuntimeException("The view not attached, please call attachView(view) firstly");
        }
    }
}
