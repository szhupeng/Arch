package space.zhupeng.fxbase.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import space.zhupeng.fxbase.R;

/**
 * @author zhupeng
 * @date 2017/11/1
 */

public class BaseWebActivity extends BaseToolbarActivity {

    public static final String EXTRA_URL = "url";
    public static final String EXTRA_JS_FILE = "jsFile";

    @Nullable
    @Bind(R.id.rl_root)
    protected RelativeLayout rlRoot;

    @Bind(R.id.web_view)
    protected WebView mWebView;

    @Nullable
    @Bind(R.id.pb_loading)
    protected ProgressBar pbLoading;

    protected String mUrl;
    protected String mJsFile;

    /**
     * @param activity
     * @param url         链接
     * @param jsFile      放于assets目录下需要加载的js文件名
     * @param requestCode
     */
    public static void toHere(@NonNull Activity activity, @NonNull String url, String jsFile, int requestCode) {
        Intent intent = new Intent(activity, BaseWebActivity.class);
        intent.putExtra(EXTRA_URL, url);
        if (!TextUtils.isEmpty(jsFile)) {
            intent.putExtra(EXTRA_JS_FILE, jsFile);
        }

        if (requestCode > 0) {
            activity.startActivityForResult(intent, requestCode);
        } else {
            activity.startActivity(intent);
        }
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_base_web;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        final Intent intent = getIntent();
        if (null == intent) {
            return;
        }

        final String url = intent.getStringExtra(EXTRA_URL);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                setCenterTitle(title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    pbLoading.setVisibility(View.GONE);
                } else {
                    pbLoading.setVisibility(View.VISIBLE);
                    pbLoading.setProgress(newProgress);
                }
            }
        });

        loadHtml(url);
    }

    private void loadHtml(final String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        final WebViewClient wvClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!url.equals(mUrl)) {
                    view.loadUrl(url);
                } else {
                    // 页面相同，清楚此路径的历史记录
                    view.clearHistory();
                }
                return true;
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return shouldOverrideUrlLoading(view, request.getUrl().toString());
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (!TextUtils.isEmpty(mJsFile)) {
                    injectJavaScript();
                }
            }
        };

        mWebView.setWebViewClient(wvClient);
        configSettings(mWebView.getSettings());
        setCookie();
        mWebView.loadUrl(url, getHeaders());
    }

    protected void configSettings(WebSettings settings) {
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setAllowFileAccess(true); // 设置允许访问文件数据
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowUniversalAccessFromFileURLs(true);
        }
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    private void injectJavaScript() {
        InputStream input;
        try {
            input = getAssets().open(mJsFile);
            byte[] buffer = new byte[input.available()];
            input.read(buffer);
            input.close();
            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
            mWebView.loadUrl("javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var script = document.createElement('script');" +
                    "script.type = 'text/javascript';" +
                    // Tell the browser to BASE64-decode the string into your script !!!
                    "script.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(script)" +
                    "})()");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setCookie() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(this);
        }
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeSessionCookies(null);
            cookieManager.removeAllCookies(null);
        } else {
            cookieManager.removeSessionCookie();
            cookieManager.removeAllCookie();
        }
        cookieManager.setCookie(mUrl, "value");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush();
        } else {
            CookieSyncManager.getInstance().sync();
        }
    }

    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("platform", "android " + Build.VERSION.RELEASE);
        headers.put("device", Build.MODEL);
        headers.put("typeTmp", "h5");
        return headers;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 在onPause时重新加载页面，可以达到暂停音视频播放目的
    @Override
    protected void onPause() {
        mWebView.reload();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (rlRoot != null) {
            rlRoot.removeView(mWebView);
        }

        if (mWebView != null) {
            mWebView.stopLoading();
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
            rlRoot = null;
        }
        super.onDestroy();
    }
}
