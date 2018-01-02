package space.zhupeng.arch.mvp.presenter;

import android.app.Service;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.v4.app.Fragment;
import android.support.v4.app.SupportActivity;
import android.view.View;

import space.zhupeng.arch.mvp.model.BaseModel;
import space.zhupeng.arch.mvp.view.BaseView;

/**
 * MVP模式中Presenter基类
 *
 * @author zhupeng
 * @date 2017/8/18
 */

public class BasePresenter<M extends BaseModel, V extends BaseView> implements Presenter<V>, LifecycleObserver {

    protected M mModel;
    protected V mView;

    public BasePresenter(M model) {
        this.mModel = model;
    }

    @Override
    public void attachView(V view) {
        this.mView = view;

        if (mView != null && mView instanceof LifecycleOwner) {
            ((LifecycleOwner) mView).getLifecycle().addObserver(this);
            if (mModel != null && mModel instanceof LifecycleObserver) {
                ((LifecycleOwner) mView).getLifecycle().addObserver(mModel);
            }
        }
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    /**
     * 只有当 {@code mView} 不为 null, 并且 {@code mView} 实现了 {@link LifecycleOwner} 时, 此方法才会被调用
     * 所以如果想在 {@link Service} 以及一些自定义 {@link View} 或自定义类中使用 {@code Presenter} 时
     * 将不能继续使用 {@link OnLifecycleEvent} 绑定生命周期
     *
     * @param owner link {@link SupportActivity} and {@link Fragment}
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(LifecycleOwner owner) {
        /**
         * 注意, 如果在这里调用了 {@link #onDestroy()} 方法, 会出现某些地方引用 {@code mModel} 或 {@code mRootView} 为 null 的情况
         * 比如在 {@link RxLifecycle} 终止 {@link Observable} 时, 在 {@link io.reactivex.Observable#doFinally(Action)} 中却引用了 {@code mRootView} 做一些释放资源的操作, 此时会空指针
         * 或者如果声明了多个 @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY) 时在其他 @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
         * 中引用了 {@code mModel} 或 {@code mRootView} 也可能会出现此情况
         */
        owner.getLifecycle().removeObserver(this);
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
