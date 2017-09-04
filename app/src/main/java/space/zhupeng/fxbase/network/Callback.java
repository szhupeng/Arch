package space.zhupeng.fxbase.network;

/**
 * @author zhupeng
 * @date 2016/12/21
 */

public interface Callback<T> {
    void onSuccess(T data);

    void onFailure(Throwable throwable);
}
