package space.zhupeng.fxbase.mvp.model;

import android.app.Application;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhupeng
 * @date 2017/9/17
 */

public class RepositoryManager implements IRepositoryManager {

    private Application mAppContext;
    private final Map<String, Object> mRetrofitServiceCache = new HashMap<>();
    private final Map<String, Object> mCacheServiceCache = new HashMap<>();

    public RepositoryManager(Application application) {
        this.mAppContext = application;
    }

    @Override
    public <T> T obtainRetrofitService(Class<T> service) {
        T retrofitService;
        synchronized (mRetrofitServiceCache) {
            retrofitService = (T) mRetrofitServiceCache.get(service.getName());
            if (retrofitService == null) {
//                retrofitService = mRetrofit.get().create(service);
//                mRetrofitServiceCache.put(service.getName(), retrofitService);
            }
        }
        return retrofitService;
    }

    @Override
    public <T> T obtainCacheService(Class<T> cache) {
        T cacheService;
        synchronized (mCacheServiceCache) {
            cacheService = (T) mCacheServiceCache.get(cache.getName());
            if (cacheService == null) {
//                cacheService = mRxCache.get().using(cache);
                mCacheServiceCache.put(cache.getName(), cacheService);
            }
        }
        return cacheService;
    }

    @Override
    public void clearAllCache() {
//        mRxCache.get().evictAll();
    }

    @Override
    public Context getContext() {
        return mAppContext;
    }
}
