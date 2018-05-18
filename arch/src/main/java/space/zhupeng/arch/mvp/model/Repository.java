package space.zhupeng.arch.mvp.model;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import space.zhupeng.arch.net.response.BaseResp;

/**
 * @author zhupeng
 * @date 2017/9/9
 */

public abstract class Repository<T> implements DataSource<T>, LifecycleObserver {

    public interface CallFactory<D> {
        Call<D> create();
    }

    public interface RepositoryObserver {
        void onRepositoryChanged();
    }

    private final List<RepositoryObserver> mObservers = new ArrayList<>(1);

    protected T mCachedData;
    //将缓存标记为无效（设为true），以便在下次请求数据时强制更新
    protected boolean mCacheIsDirty = true;

    private LocalDataSource<T> mLocal;
    private RemoteDataSource<T> mRemote;

    public Repository(RemoteDataSource remote, LocalDataSource local) {
        this.mRemote = remote;
        this.mLocal = local;
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

    public final T toLoadDataSync(final CallFactory<BaseResp<T>> factory, final boolean forceToFetch) throws Exception {
        return toLoadDataSync(factory, forceToFetch, 0);
    }

    public final T toLoadDataSync(final CallFactory<BaseResp<T>> factory) throws Exception {
        return toLoadDataSync(factory, false, 0);
    }

    public final T toLoadDataSync(final CallFactory<BaseResp<T>> factory, final int what) throws Exception {
        return toLoadDataSync(factory, false, what);
    }

    /**
     * 同步加载数据
     *
     * @param factory
     * @param forceToFetch 是否强制从网络拉取数据
     * @return
     * @throws Exception
     */
    public final T toLoadDataSync(final CallFactory<BaseResp<T>> factory, final boolean forceToFetch, final int what) throws Exception {
        if (forceToFetch) {
            this.mCachedData = null;
            this.mCacheIsDirty = true;
        }

        T loadedData = null;
        if (!this.mCacheIsDirty) {
            if (this.mCachedData != null) {
                return this.mCachedData;
            } else {
                loadedData = loadFromLocal(what);
            }
        }

        if (loadedData == null) {
            Call<BaseResp<T>> call = factory.create();
            Response<BaseResp<T>> response = call.execute();
            if (!checkResp(response)) {
                throw new RuntimeException("请求失败,请重试!");
            }

            final BaseResp<T> resp = response.body();
            loadedData = resp.result;
            saveToLocal(loadedData, what);
        }

        if (loadedData != null) {
            this.mCacheIsDirty = false;
            this.mCachedData = loadedData;
        }

        return this.mCachedData;
    }

    public final void toLoadDataAsync(final CallFactory<BaseResp<T>> factory, final RepoCallback callback) throws Exception {
        toLoadDataAsync(factory, callback, false, 0);
    }

    public final void toLoadDataAsync(final CallFactory<BaseResp<T>> factory, final RepoCallback callback, final boolean forceToFetch) throws Exception {
        toLoadDataAsync(factory, callback, forceToFetch, 0);
    }

    public final void toLoadDataAsync(final CallFactory<BaseResp<T>> factory, final RepoCallback callback, final int what) throws Exception {
        toLoadDataAsync(factory, callback, false, what);
    }

    /**
     * 异步加载数据
     *
     * @param factory
     * @param callback
     * @param forceToFetch 是否强制从网络拉取数据
     * @param what
     */
    public final void toLoadDataAsync(final CallFactory<BaseResp<T>> factory, final RepoCallback callback, final boolean forceToFetch, final int what) throws Exception {
        if (null == factory || null == callback) return;

        if (forceToFetch) {
            this.mCachedData = null;
            this.mCacheIsDirty = true;
        }

        if (isCachedDataAvailable()) {
            callback.onSuccess(this.mCachedData);
            return;
        }

        if (this.mCacheIsDirty) {
            Call<BaseResp<T>> call = factory.create();
            call.enqueue(new Callback<BaseResp<T>>() {
                @Override
                public void onResponse(Call<BaseResp<T>> call, Response<BaseResp<T>> response) {
                    if (!checkResp(response)) {
                        throw new RuntimeException("请求失败，请重试!");
                    }

                    try {
                        mCachedData = response.body().result;
                        mCacheIsDirty = false;
                        saveToLocal(mCachedData, what);
                        callback.onSuccess(mCachedData);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onFailure(Call<BaseResp<T>> call, Throwable t) {
                    callback.onFailure(t);
                }
            });
        } else {
            loadFromLocal(new RepoCallback<T>() {
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
            }, what);
        }
    }

    /**
     * 检查请求响应
     *
     * @param response
     * @return
     */
    protected boolean checkResp(Response<BaseResp<T>> response) {
        if (null == response || !response.isSuccessful()) {
            return false;
        }

        BaseResp<T> resp = response.body();
        if (null == resp) {
            return false;
        }

        return true;
    }

    /**
     * 数据存储到本地
     *
     * @param cache
     * @param what  用户定义的代码，便于不同处理
     */
    protected void saveToLocal(final T cache, final int what) throws Exception {
    }

    /**
     * 异步加载本地数据（数据库，文件等数据）
     *
     * @param callback
     * @param what     用户定义的代码，便于不同处理
     * @return false 无本地数据，true 有本地数据
     */
    protected void loadFromLocal(final RepoCallback callback, final int what) throws Exception {
    }

    /**
     * 同步加载本地数据（数据库，文件等数据）
     *
     * @param what 用户定义的代码，便于不同处理
     * @return 返回数据
     */
    protected T loadFromLocal(final int what) throws Exception {
        return null;
    }

    @Override
    public T getData() {
        if (!mCacheIsDirty) {
            return mLocal.getData();
        } else {
            return mRemote.getData();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(LifecycleOwner owner) {
        owner.getLifecycle().removeObserver(this);
    }
}
