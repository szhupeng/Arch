package space.zhupeng.fxbase.ui.activity;

import space.zhupeng.fxbase.mvp.presenter.BasePresenter;
import space.zhupeng.fxbase.mvp.view.BaseView;

/**
 * tab切换的基类
 *
 * @author zhupeng
 * @date 2017/1/14
 */

public abstract class BaseTabActivity<M, V extends BaseView, P extends BasePresenter<M, V>> extends BaseToolbarActivity<M, V, P> {

}
