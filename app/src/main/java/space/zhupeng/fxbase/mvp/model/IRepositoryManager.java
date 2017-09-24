package space.zhupeng.fxbase.mvp.model;

import android.content.Context;

/**
 * @author zhupeng
 * @date 2017/9/17
 */

public interface IRepositoryManager {
    /**
     * 根据传入的Class获取对应的Retrofit Service
     *
     * @param service
     * @param <T>
     * @return
     */
    <T> T obtainRetrofitService(Class<T> service);

    /**
     * 根据传入的Class获取对应的 RxCache service
     *
     * @param cache
     * @param <T>
     * @return
     */
    <T> T obtainCacheService(Class<T> cache);

    /**
     * 清理所有缓存
     */
    void clearAllCache();

    Context getContext();
}
