package space.zhupeng.arch.manager;

import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author zhupeng
 * @date 2018/1/9
 */

public class HttpHelper {
    public static final String ACCEPT_LANGUAGE = "Accept-Language";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";
    public static final long CONNECT_TIME_OUT = 20 * 1000;
    public static final long READ_TIME_OUT = 30 * 1000;

    private static final String DOMAIN_NAME = "Domain-Name";

    private Retrofit mRetrofit;
    private OkHttpClient mHttpClient;
    private Context context;
    private final HashMap<Class, Object> mApiServiceHub = new HashMap<>(1);
    private final Map<String, HttpUrl> mDomainNameHub = new HashMap<>(1);

    private Interceptor mHeaderInterceptor;

    public final void baseUrl(String baseURL) {
        mRetrofit = new Retrofit.Builder().client(mHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseURL)
                .build();
        mApiServiceHub.clear();
    }

    public final <T> T createApi(Class<T> service) {
        if (!mApiServiceHub.containsKey(service)) {
            T instance = mRetrofit.create(service);
            mApiServiceHub.put(service, instance);
        }

        //noinspection unchecked
        return (T) mApiServiceHub.get(service);
    }

    /**
     * 从 {@link Request#header(String)} 中取出 Domain-Name
     *
     * @param request
     * @return
     */
    private String obtainDomainName(Request request) {
        List<String> headers = request.headers(DOMAIN_NAME);
        if (headers == null || headers.size() == 0)
            return null;
        if (headers.size() > 1)
            throw new IllegalArgumentException("Only one Domain can put in the headers");
        return request.header(DOMAIN_NAME);
    }

    /**
     * 存放 Domain 的映射关系
     *
     * @param domainName
     * @param domainUrl
     */
    public final void putDomain(String domainName, String domainUrl) {
        synchronized (mDomainNameHub) {
            mDomainNameHub.put(domainName, checkUrl(domainUrl));
        }
    }

    /**
     * 取出对应 DomainName 的 Url
     *
     * @param domainName
     * @return
     */
    public HttpUrl fetchDomain(String domainName) {
        return mDomainNameHub.get(domainName);
    }

    public void removeDomain(String domainName) {
        synchronized (mDomainNameHub) {
            mDomainNameHub.remove(domainName);
        }
    }

    public void clearAllDomain() {
        mDomainNameHub.clear();
    }

    private HttpUrl checkUrl(String url) {
        HttpUrl parseUrl = HttpUrl.parse(url);
        if (null == parseUrl) {
            throw new RuntimeException(url);
        } else {
            return parseUrl;
        }
    }

    protected HttpUrl parseUrl(HttpUrl domainUrl, HttpUrl url) {

        // 如果 HttpUrl.parse(url); 解析为 null 说明,url 格式不正确,正确的格式为 "https://github.com:443"
        // http 默认端口 80,https 默认端口 443 ,如果端口号是默认端口号就可以将 ":443" 去掉
        // 只支持 http 和 https

        if (null == domainUrl) return url;

        return url.newBuilder()
                .scheme(domainUrl.scheme())
                .host(domainUrl.host())
                .port(domainUrl.port())
                .build();
    }

    private final String generateAcceptLanguage() {
        Locale locale = Locale.getDefault();
        return String.format("%s-%s,%s;q=0.8,en-US;q=0.6,en;q=0.4",
                locale.getLanguage(), locale.getCountry(), locale.getLanguage());
    }
}
