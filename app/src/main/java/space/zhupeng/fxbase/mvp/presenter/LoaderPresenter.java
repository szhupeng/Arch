package space.zhupeng.fxbase.mvp.presenter;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import space.zhupeng.fxbase.mvp.model.MvpLoader;
import space.zhupeng.fxbase.mvp.view.BaseView;

/**
 * @author zhupeng
 * @date 2017/9/10
 */

public class LoaderPresenter<T, M, V extends BaseView> extends BasePresenter<M, V> implements LoaderManager.LoaderCallbacks<T> {

    private MvpLoader<T> mLoader;
    private final LoaderManager mLoaderManager;

    public LoaderPresenter(M model, MvpLoader<T> loader, LoaderManager manager) {
        super(model);
        if (null == loader) {
            throw new NullPointerException("The loader passed to LoaderPresenter cannot be null");
        }
        if (null == manager) {
            throw new NullPointerException("The loader LoaderManager to LoaderPresenter cannot be null");
        }
        this.mLoader = loader;
        this.mLoaderManager = manager;
    }

    @Override
    public Loader<T> onCreateLoader(int id, Bundle args) {
        return mLoader;
    }

    @Override
    public void onLoadFinished(Loader<T> loader, T data) {
    }

    @Override
    public void onLoaderReset(Loader<T> loader) {
    }
}
