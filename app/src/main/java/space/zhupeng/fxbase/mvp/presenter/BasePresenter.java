package space.zhupeng.fxbase.mvp.presenter;

import space.zhupeng.fxbase.mvp.view.BaseView;

/**
 * MVP模式中Presenter基类
 *
 * @author zhupeng
 * @date 2017/8/18
 */

public class BasePresenter<M, V extends BaseView> implements Presenter<V> {

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
            throw new MvpViewNotAttachedException();
        }
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("The view not attached, please call attachView(view) firstly");
        }
    }
}
