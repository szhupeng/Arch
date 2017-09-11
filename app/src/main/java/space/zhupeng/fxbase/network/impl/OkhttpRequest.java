package space.zhupeng.fxbase.network.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import space.zhupeng.fxbase.network.Callback;
import space.zhupeng.fxbase.network.Request;

/**
 * @author zhupeng
 * @date 2016/12/21
 */

public class OkhttpRequest implements Request {

    private OkHttpClient mOkHttpClient;

    public OkhttpRequest() {
        mOkHttpClient = new OkHttpClient();
    }

    @Override
    public <T> T get(String url) throws Exception {
        okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();
        Response response = mOkHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            String json = response.body().string();
            Gson gson = new Gson();
            T data = gson.fromJson(json, new TypeToken<T>() {
            }.getType());
            return data;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    @Override
    public <T> void get(String url, final Callback<T> callback) throws Exception {
        okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Gson gson = new Gson();
                T data = gson.fromJson(json, new TypeToken<T>() {
                }.getType());

                callback.onSuccess(data);
            }
        });
    }

    @Override
    public void post(String url, String requestBody, Callback callback) {
    }

    @Override
    public void put(String url, String requestBody, Callback callback) {
    }

    @Override
    public void delete(String url, String requestBody, Callback callback) {
    }
}
