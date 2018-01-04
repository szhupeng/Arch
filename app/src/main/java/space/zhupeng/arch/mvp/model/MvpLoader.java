package space.zhupeng.arch.mvp.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

/**
 * @author zhupeng
 * @date 2017/9/10
 */

public abstract class MvpLoader<T> extends AsyncTaskLoader<T> implements Repository.RepositoryObserver {

    private Repository<T> mRepository;

    public MvpLoader(Context context, @NonNull Repository<T> repository) {
        super(context);
        if (null == repository)
            throw new NullPointerException("The repository passed to MvpLoader cannot be null");
        this.mRepository = repository;
    }

    @Override
    public T loadInBackground() {
        return mRepository.toLoadData();
    }

    @Override
    public void deliverResult(T data) {
        if (isReset()) {
            return;
        }

        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        // Deliver any previously loaded data immediately if available.
        if (mRepository.isCachedDataAvailable()) {
            deliverResult(mRepository.getCachedData());
        }

        // Begin monitoring the underlying data source.
        mRepository.addContentObserver(this);

        if (takeContentChanged() || !mRepository.isCachedDataAvailable()) {
            // When a change has been delivered, we force a load.
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        mRepository.removeContentObserver(this);
    }

    @Override
    public void onRepositoryChanged() {
        if (isStarted()) {
            forceLoad();
        }
    }
}
