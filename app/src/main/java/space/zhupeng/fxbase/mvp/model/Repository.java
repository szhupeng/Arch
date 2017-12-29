package space.zhupeng.fxbase.mvp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhupeng
 * @date 2017/9/9
 */

public class Repository<T> {

    public interface RepositoryObserver {
        void onRepositoryChanged();
    }

    private final List<RepositoryObserver> mObservers = new ArrayList<>(1);

    protected T mCachedData;

    protected boolean mCacheIsDirty;

    public boolean isCachedDataAvailable() {
        return mCachedData != null && !mCacheIsDirty;
    }

    public T getCachedData() {
        return mCachedData;
    }

    public void addContentObserver(RepositoryObserver observer) {
        if (!mObservers.contains(observer)) {
            mObservers.add(observer);
        }
    }

    public void removeContentObserver(RepositoryObserver observer) {
        if (mObservers.contains(observer)) {
            mObservers.remove(observer);
        }
    }

    private void notifyContentObserver() {
        for (RepositoryObserver observer : mObservers) {
            observer.onRepositoryChanged();
        }
    }
}
