package space.zhupeng.base.network;

/**
 * @author zhupeng
 * @date 2016/12/12
 */

public interface Request {
    void get(String url, Callback callback);

    void post(String url, String requestBody, Callback callback);

    void put(String url, String requestBody, Callback callback);

    void delete(String url, String requestBody, Callback callback);
}
