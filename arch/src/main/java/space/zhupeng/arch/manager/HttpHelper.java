package space.zhupeng.arch.manager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import space.zhupeng.arch.Provider;

/**
 * @author zhupeng
 * @date 2018/1/9
 */

public class HttpHelper {

    public interface HeadersProvider extends Provider<ArrayMap<String, String>> {
    }

    public static final String ACCEPT_LANGUAGE = "Accept-Language";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";
    public static final long CONNECT_TIME_OUT = 20 * 1000;
    public static final long READ_TIME_OUT = 30 * 1000;

    private static final String DOMAIN_NAME = "Domain-Name";

    private static HttpHelper sInstance;

    private Retrofit mRetrofit;
    private final ArrayMap<String, Object> mApiServiceHub = new ArrayMap<>(1);
    private final ArrayMap<String, HttpUrl> mDomainNameHub = new ArrayMap<>(1);

    private HeadersProvider mHeadersProvider;
    private Interceptor mHeadersInterceptor;
    private HttpLoggingInterceptor mLoggingInterceptor;

    public static HttpHelper getInstance(@Nullable HeadersProvider provider, @NonNull String baseURL) {
        if (null == sInstance) {
            synchronized (HttpHelper.class) {
                if (null == sInstance) {
                    sInstance = new HttpHelper(provider, baseURL);
                }
            }
        }
        return sInstance;
    }

    private HttpHelper(@Nullable HeadersProvider provider, @NonNull String baseURL) {
        this.mHeadersProvider = provider;

        this.mHeadersInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                return chain.proceed(processRequest(chain.request()));
            }
        };

        this.mLoggingInterceptor = new HttpLoggingInterceptor();

        baseUrl(baseURL);
    }

    public void setDebuggable(boolean debuggable) {
        if (debuggable) {
            this.mLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            this.mLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
    }

    public final void setHeadersProvider(final HeadersProvider provider) {
        this.mHeadersProvider = provider;
    }

    public final void baseUrl(@NonNull String baseURL, Converter.Factory converter, CallAdapter.Factory adapter) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient client = builder.connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .addInterceptor(mHeadersInterceptor)
                .addInterceptor(mLoggingInterceptor)
                .build();
        mRetrofit = new Retrofit.Builder().client(client)
                .addConverterFactory(converter)
                .addCallAdapterFactory(adapter)
                .baseUrl(baseURL)
                .build();
        mApiServiceHub.clear();
    }

    public final void baseUrl(@NonNull String baseURL, Converter.Factory factory) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient client = builder.connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .addInterceptor(mHeadersInterceptor)
                .addInterceptor(mLoggingInterceptor)
                .build();
        mRetrofit = new Retrofit.Builder().client(client)
                .addConverterFactory(factory)
                .baseUrl(baseURL)
                .build();
        mApiServiceHub.clear();
    }

    public final void baseUrl(@NonNull String baseURL) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient client = builder.connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .addInterceptor(mHeadersInterceptor)
                .addInterceptor(mLoggingInterceptor)
                .build();
        mRetrofit = new Retrofit.Builder().client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseURL)
                .build();
        mApiServiceHub.clear();
    }

    /**
     * 对 {@link Request} 进行处理
     *
     * @param request
     * @return
     */
    private Request processRequest(final Request request) {
        Request.Builder builder = request.newBuilder();

        if (mHeadersProvider != null) {
            ArrayMap<String, String> headers = mHeadersProvider.get();
            if (headers != null && !headers.isEmpty()) {
                for (int i = 0, size = headers.size(); i < size; i++) {
                    builder.header(headers.keyAt(i), headers.valueAt(i));
                }
            }
        }
        builder.header(ACCEPT_LANGUAGE, generateAcceptLanguage())
                .header(CONTENT_TYPE, CONTENT_TYPE_JSON);

        final String domainName = obtainDomainName(request);

        HttpUrl httpUrl = null;

        // 如果有 header，获取 header 中配置的url
        if (!TextUtils.isEmpty(domainName)) {
            httpUrl = fetchDomain(domainName);
            builder.removeHeader(DOMAIN_NAME);
        }

        if (null != httpUrl) {
            return builder.url(parseUrl(httpUrl, request.url())).build();
        }

        return builder.build();
    }

    public final <T> T createApi(Class<T> service) {
        if (!mApiServiceHub.containsKey(service.getCanonicalName())) {
            T instance = mRetrofit.create(service);
            mApiServiceHub.put(service.getCanonicalName(), instance);
        }

        //noinspection unchecked
        return (T) mApiServiceHub.get(service.getCanonicalName());
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

    public final void removeDomain(String domainName) {
        synchronized (mDomainNameHub) {
            mDomainNameHub.remove(domainName);
        }
    }

    public final void clearAllDomain() {
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

    protected final HttpUrl parseUrl(HttpUrl domainUrl, HttpUrl url) {
        if (null == domainUrl) return url;

        return url.newBuilder()
                .scheme(domainUrl.scheme())
                .host(domainUrl.host())
                .port(domainUrl.port())
                .build();
    }

    private String generateAcceptLanguage() {
        Locale locale = Locale.getDefault();
        return String.format("%s-%s,%s;q=0.8,en-US;q=0.6,en;q=0.4",
                locale.getLanguage(), locale.getCountry(), locale.getLanguage());
    }

    public static <T> retrofit2.Response<T> syncCall(Call<T> call) {
        try {
            return call.execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
