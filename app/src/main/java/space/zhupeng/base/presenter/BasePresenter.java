package space.zhupeng.base.presenter;

import space.zhupeng.base.view.BaseView;

/**
 * MVP模式中Presenter基类
 *
 * @author zhupeng
 * @date 2017/8/18
 */

public class BasePresenter<M, V extends BaseView> {

    protected M mModel;

    protected V mView;

    public BasePresenter(M model, V view) {
        this.mModel = model;
        this.mView = view;
    }
}
