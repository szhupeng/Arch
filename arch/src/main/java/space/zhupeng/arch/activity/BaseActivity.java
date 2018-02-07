package space.zhupeng.arch.activity;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;

import space.zhupeng.arch.R;
import space.zhupeng.arch.manager.StatusBarTinter;
import space.zhupeng.arch.mvp.model.Repository;
import space.zhupeng.arch.mvp.presenter.BasePresenter;
import space.zhupeng.arch.mvp.presenter.PresenterFactory;
import space.zhupeng.arch.mvp.presenter.PresenterLoader;
import space.zhupeng.arch.mvp.view.BaseView;
import space.zhupeng.arch.utils.NetworkUtils;
import space.zhupeng.arch.utils.ToastUtils;
import space.zhupeng.arch.utils.Utils;
import space.zhupeng.arch.widget.dialog.DialogFactory;

/**
 * 业务无关的Activity基类，包括Toast，加载进度框等
 *
 * @author zhupeng
 * @date 2017/1/14
 */
@SuppressWarnings("all")
public abstract class BaseActivity<M extends Repository, V extends BaseView, P extends BasePresenter<M, V>> extends XActivity implements BaseView, LoaderManager.LoaderCallbacks<P> {

    private static final int ID_PRESENTER_LOADER = 100;

    protected final Handler mHandler = new Handler(Looper.getMainLooper());

    protected P mPresenter;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        LayoutInflaterFactory factory = getInflaterFactory();
        if (factory != null) {
            LayoutInflaterCompat.setFactory(getLayoutInflater(), factory);
        }

        super.onCreate(savedInstanceState);

        requestFeature();

        if (getLayoutResId() > 0) {
            setContentView(getLayoutResId());
        }

        if (isStatusBarTintEnabled()) {
            tintStatusBar();
        }

        getSupportLoaderManager().initLoader(ID_PRESENTER_LOADER, null, this);

        initView(savedInstanceState);
        bindEvent();
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroy();
    }

    @Override
    protected int getContainerId() {
        return 0;
    }

    /**
     * 对字体图标进行渲染
     *
     * @return
     */
    protected LayoutInflaterFactory getInflaterFactory() {
        return null;
    }

    /**
     * 执行setContentView之前的操作，比如全屏
     */
    protected void requestFeature() {
    }

    /**
     * 初始化视图
     *
     * @param savedInstanceState
     */
    protected void initView(@Nullable Bundle savedInstanceState) {
    }

    /**
     * 给视图绑定事件
     */
    protected void bindEvent() {
    }

    protected void onPresenterReady() {
    }

    protected boolean isStatusBarTintEnabled() {
        return true;
    }

    /**
     * 状态栏着色
     */
    private void tintStatusBar() {
        int colorResId;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            colorResId = android.R.attr.colorPrimaryDark;
        } else {
            colorResId = R.attr.colorPrimaryDark;
        }
        TypedValue ta = new TypedValue();
        int tintColor = ContextCompat.getColor(this, R.color.colorPrimaryDark);
        try {
            Resources.Theme theme = getTheme();
            if (theme != null && theme.resolveAttribute(colorResId, ta, true)) {
                if (ta.type >= TypedValue.TYPE_FIRST_INT && ta.type <= TypedValue.TYPE_LAST_INT)
                    tintColor = ta.data;
                else if (ta.type == TypedValue.TYPE_STRING)
                    tintColor = ContextCompat.getColor(this, ta.resourceId);
            }
        } catch (Exception ex) {
        }
        StatusBarTinter.setStatusBarColor(this, tintColor);
    }

    /**
     * 返回上一层Activity
     */
    protected void navigateUp() {
        if (NavUtils.getParentActivityName(getContext()) != null) {
            NavUtils.navigateUpFromSameTask(getContext());
        }
    }

    public void showSnackbar(final String message) {
        final View view = getWindow().getDecorView().findViewById(android.R.id.content);
        runOnUiThreadSafely(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showToast(@NonNull final CharSequence text) {
        if (TextUtils.isEmpty(text)) return;

        ToastUtils.showShortSafely(getApplicationContext(), text);
    }

    @Override
    public void showToast(@StringRes final int resId) {
        showToast(getResources().getString(resId));
    }

    @Override
    public void showCustomToast(@LayoutRes final int layoutId) {
        ToastUtils.showCustomShortSafely(getApplicationContext(), layoutId);
    }

    @Override
    public void showCustomToast(@NonNull final View view) {
        ToastUtils.showCustomShortSafely(getApplicationContext(), view);
    }

    @Override
    public void showMessageProgress(@NonNull final CharSequence message) {
        DialogFactory.create().showProgressDialog(getContext(), message);
    }

    @Override
    public void showMessageProgress(@StringRes int resId) {
        DialogFactory.create().showProgressDialog(getContext(), getResources().getString(resId));
    }

    @Override
    public void showSimpleProgress() {
        DialogFactory.create().showProgressDialog(this.getContext());
    }

    @Override
    public void closeDialog() {
        DialogFactory.create().closeDialog();
    }

    @Override
    public Activity getContext() {
        return this;
    }

    @Override
    public final Loader<P> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader(this, new PresenterFactory<P>() {
            @Override
            public P create() {
                return createPresenter();
            }
        });
    }

    protected P createPresenter() {
        return null;
    }

    @Override
    public final void onLoadFinished(Loader<P> loader, P data) {
        if (ID_PRESENTER_LOADER == loader.getId()) {
            mPresenter = data;
            if (mPresenter != null) {
                mPresenter.attachView((V) this);
                onPresenterReady();
            }
        }
    }

    @Override
    public final void onLoaderReset(Loader<P> loader) {
        if (ID_PRESENTER_LOADER == loader.getId()) {
            mPresenter = null;
        }
    }

    /**
     * 在主线程安全执行
     *
     * @param runnable
     */
    public final void runOnUiThreadSafely(final Runnable runnable) {
        Utils.runOnUiThreadSafely(getContext(), runnable);
    }

    /**
     * 在主线程安全执行
     *
     * @param runnable
     */
    public final void postSafely(final Runnable runnable) {
        Utils.postSafely(mHandler, runnable);
    }

    /**
     * 检查网络是否可用
     *
     * @return
     */
    protected final boolean toCheckNetwork() {
        if (!NetworkUtils.isNetworkValid(getContext())) {
            showToast("网络连接不可用");
            return false;
        }
        return true;
    }

    protected final <T extends View> T findById(@IdRes int id) {
        return findViewById(id);
    }

    @LayoutRes
    protected abstract int getLayoutResId();
}
