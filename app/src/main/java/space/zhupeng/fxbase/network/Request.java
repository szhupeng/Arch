package space.zhupeng.fxbase.network;

import space.zhupeng.fxbase.network.response.BaseResp;

/**
 * @author zhupeng
 * @date 2016/12/12
 */

public interface Request {

    <T> T get(String url) throws Exception;

    <T> void get(String url, Callback<T> callback) throws Exception;

    <T> void post(String url, String requestBody, Callback<BaseResp<T>> callback) throws Exception;

    <T> void put(String url, String requestBody, Callback<BaseResp<T>> callback) throws Exception;

    <T> void delete(String url, String requestBody, Callback<BaseResp<T>> callback) throws Exception;
}
