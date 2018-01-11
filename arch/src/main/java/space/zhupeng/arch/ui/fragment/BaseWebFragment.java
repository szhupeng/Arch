package space.zhupeng.arch.ui.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import space.zhupeng.arch.R;
import space.zhupeng.arch.utils.Utils;

/**
 * @author zhupeng
 * @date 2017/12/26
 */

public class BaseWebFragment extends BaseToolbarFragment {

    public static BaseWebFragment newInstance(@NonNull String url, String jsFile) {
        Bundle args = new Bundle();
        args.putString(EXTRA_URL, url);
        if (!TextUtils.isEmpty(jsFile)) {
            args.putString(EXTRA_JS_FILE, jsFile);
        }
        BaseWebFragment fragment = new BaseWebFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private final static int RC_TAKE_PHOTO = 100;

    public static final String EXTRA_URL = "url";
    public static final String EXTRA_JS_FILE = "jsFile";

    @Nullable
    protected ViewGroup mWebRoot;
    protected WebView mWebView;
    @Nullable
    protected ProgressBar pbLoading;

    protected String mUrl;
    protected String mJsFile;

    protected ValueCallback<Uri> mUploadMessage;
    protected ValueCallback<Uri[]> mLollipopUploadCallback;
    protected File mPhotoFile = new File(Environment.getExternalStorageDirectory().getPath() + "/" + SystemClock.currentThreadTimeMillis() + ".jpg");
    protected Uri mImageUri;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_base_web;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);

        mWebRoot = view.findViewById(R.id.web_root);
        mWebView = view.findViewById(R.id.wv_html);
        pbLoading = view.findViewById(R.id.pb_loading);

        Bundle args = getArguments();
        if (null == args) {
            if (mPassedData != null) {
                args = (Bundle) mPassedData;
            } else {
                return;
            }
        }

        mUrl = args.getString(EXTRA_URL);
        mWebView.setWebChromeClient(getWebChromeClient());

        mWebView.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadHtml(mUrl);
            }
        }, 200);
    }

    protected WebChromeClient getWebChromeClient() {
        return new BaseWebChromeClient();
    }

    protected WebViewClient getWebViewClient() {
        return new BaseWebViewClient();
    }

    private void loadHtml(final String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        mWebView.setWebViewClient(getWebViewClient());
        configSettings(mWebView.getSettings());
        setCookie();
        mWebView.loadUrl(url, getHeaders());
    }

    protected void configSettings(WebSettings settings) {
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDomStorageEnabled(true);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setAllowContentAccess(true); // 是否可访问ContentProvider的资源，默认值true
        settings.setAllowFileAccess(true);    // 是否可访问本地文件，默认值true
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // 是否允许通过file url加载的Javascript读取本地文件，默认值false
            settings.setAllowFileAccessFromFileURLs(false);
            // 是否允许通过file url加载的Javascript读取全部资源(包括文件,http,https)，默认值 false
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
            input = getResources().getAssets().open(mJsFile);
            byte[] buffer = new byte[input.available()];
            input.read(buffer);
            input.close();
            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
            final String trigger = new StringBuilder("javascript:(function() {")
                    .append("var parent = document.getElementsByTagName('head').item(0);")
                    .append("var script = document.createElement('script');")
                    .append("script.type = 'text/javascript';")
                    .append("script.innerHTML = window.atob('")
                    .append(encoded)
                    .append("');")
                    .append("parent.appendChild(script)")
                    .append("})()").toString();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mWebView.evaluateJavascript(trigger, null);
            } else {
                mWebView.loadUrl(trigger);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setCookie() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(getActivity());
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode && mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 在onPause时重新加载页面，可以达到暂停音视频播放目的
    @Override
    public void onPause() {
        if (mWebView != null) {
            mWebView.reload();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (mWebRoot != null) {
            mWebRoot.removeView(mWebView);
        }

        if (mWebView != null) {
            mWebView.stopLoading();
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
            mWebRoot = null;
        }
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (RC_TAKE_PHOTO == requestCode) {
            if (null == mUploadMessage && null == mLollipopUploadCallback) return;
            Uri result = data == null || resultCode != Activity.RESULT_OK ? null : data.getData();
            if (mLollipopUploadCallback != null) {
                onLollipopActivityResult(requestCode, resultCode, data);
            } else if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onLollipopActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != RC_TAKE_PHOTO || mLollipopUploadCallback == null) {
            return;
        }
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                results = new Uri[]{mImageUri};
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }

                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        mLollipopUploadCallback.onReceiveValue(results);
        mLollipopUploadCallback = null;
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        mImageUri = Uri.fromFile(mPhotoFile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mImageUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".fileprovider", mPhotoFile);//通过FileProvider创建一个content类型的Uri

        }
        Utils.takePicture(getActivity(), mImageUri, RC_TAKE_PHOTO);
    }

    protected void callSystemResources() {
        takePhoto();
    }

    public class BaseWebChromeClient extends WebChromeClient {

        // For Android 3.0-
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            mUploadMessage = uploadMsg;
            callSystemResources();
        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            mUploadMessage = uploadMsg;
            callSystemResources();
        }

        //For Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUploadMessage = uploadMsg;
            callSystemResources();
        }

        // For Android 5.0+
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            mLollipopUploadCallback = filePathCallback;
            callSystemResources();
            return true;
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            setCenterTitle(title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (pbLoading != null) {
                if (newProgress == 100) {
                    pbLoading.setVisibility(View.GONE);
                } else {
                    pbLoading.setVisibility(View.VISIBLE);
                    pbLoading.setProgress(newProgress);
                }
            }
        }
    }

    public class BaseWebViewClient extends WebViewClient {
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
    }
}
