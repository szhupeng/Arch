package space.zhupeng.arch.mvp.presenter;

import android.content.Context;
import android.support.v4.content.Loader;

/**
 * 将同步的Loader作为Presenter的缓存，延长Presenter的生命周期，
 * 使得Presenter能在View消亡后还持续存在
 *
 * @author zhupeng
 * @date 2017/9/7
 */

public final class PresenterLoader<P extends Presenter> extends Loader<P> {

    private final PresenterFactory<P> mFactory;
    private P mPresenter;

    public PresenterLoader(Context context, PresenterFactory factory) {
        super(context);
        this.mFactory = factory;
    }

    @Override
    protected void onStartLoading() {
        if (mPresenter != null) {
            deliverResult(mPresenter);
            return;
        }
        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        mPresenter = mFactory.create();
        if (mPresenter != null) {
            deliverResult(mPresenter);
        }
    }

    @Override
    protected void onReset() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        mPresenter = null;
    }
}
