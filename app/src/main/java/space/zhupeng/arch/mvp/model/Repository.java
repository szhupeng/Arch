package space.zhupeng.arch.mvp.model;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhupeng
 * @date 2017/9/9
 */

public abstract class Repository<T> implements LifecycleObserver {

    public interface RepositoryObserver {
        void onRepositoryChanged();
    }

    private final List<RepositoryObserver> mObservers = new ArrayList<>(1);

    private T mCachedData;
    protected boolean mCacheIsDirty = false;

    public Repository() {
    }

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

    /**
     * 刷新数据
     *
     * @param callback
     */
    public void toRefreshData(final Callback callback) {
        if (null == callback) return;

        this.mCacheIsDirty = true;

        loadFromRemote(new Callback<T>() {
            @Override
            public void onSuccess(T data) {
                mCachedData = data;
                mCacheIsDirty = false;
                saveToLocal(data);
                callback.onSuccess(data);
            }

            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    public final T toLoadData() {
        T loadedData = null;
        if (!mCacheIsDirty) {
            if (mCachedData != null) {
                return mCachedData;
            } else {
                loadedData = loadFromLocal();
            }
        }
        if (loadedData == null) {
            loadedData = loadFromRemote();
            saveToLocal(loadedData);
        }

        if (loadedData != null) {
            mCacheIsDirty = false;
            mCachedData = loadedData;
        }

        return mCachedData;
    }

    /**
     * 加载数据
     *
     * @param callback
     */
    public final void toLoadData(final Callback callback) {
        if (null == callback) return;

        if (isCachedDataAvailable()) {
            callback.onSuccess(mCachedData);
            return;
        }

        if (mCacheIsDirty) {
            loadFromRemote(new Callback<T>() {
                @Override
                public void onSuccess(T data) {
                    mCachedData = data;
                    mCacheIsDirty = false;
                    saveToLocal(data);
                    callback.onSuccess(data);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }
            });
        } else {
            loadFromLocal(new Callback<T>() {
                @Override
                public void onSuccess(T data) {
                    mCachedData = data;
                    mCacheIsDirty = false;
                    callback.onSuccess(data);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }
            });
        }
    }

    /**
     * 数据存储到本地
     *
     * @param cache
     */
    protected void saveToLocal(final T cache) {
    }

    /**
     * 加载本地数据（数据库，文件等数据）
     *
     * @param callback
     * @return false 无本地数据，true 有本地数据
     */
    protected void loadFromLocal(final Callback callback) {
    }

    protected T loadFromLocal() {
        return null;
    }

    protected abstract T loadFromRemote();

    protected abstract void loadFromRemote(final Callback callback);

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(LifecycleOwner owner) {
        owner.getLifecycle().removeObserver(this);
    }
}
