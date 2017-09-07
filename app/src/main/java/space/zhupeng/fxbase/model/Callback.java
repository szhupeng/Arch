package space.zhupeng.fxbase.model;

/**
 * @author zhupeng
 * @date 2017/9/7
 */

public interface Callback<T> {

    /**
     * 数据加载成功
     */
    void onSuccess(T data);

    /**
     * 数据加载失败
     *
     * @param throwable
     */
    void onFailure(Throwable throwable);
}
