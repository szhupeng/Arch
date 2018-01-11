package space.zhupeng.arch.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import space.zhupeng.arch.R;
import space.zhupeng.arch.manager.StatusBarTintManager;
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

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LayoutInflaterFactory factory = getInflaterFactory();
        if (factory != null) {
            LayoutInflaterCompat.setFactory(getLayoutInflater(), factory);
        }

        super.onCreate(savedInstanceState);

        doBeforeSetView();

        tintStatusBar();

        if (getLayoutResId() > 0) {
            setContentView(getLayoutResId());
        }

        mUnbinder = ButterKnife.bind(this);

        getSupportLoaderManager().initLoader(ID_PRESENTER_LOADER, null, this);

        initView(savedInstanceState);

        bindEvent();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        }
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }

        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY) {
            mUnbinder.unbind();
            mUnbinder = null;
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
    protected void doBeforeSetView() {
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

    /**
     * 状态栏着色
     */
    protected void tintStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        StatusBarTintManager tintManager = new StatusBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        int[] attrsArray = {android.R.attr.colorPrimaryDark};
        TypedArray ta = getTheme().obtainStyledAttributes(attrsArray);
        int primaryDarkColor = ta.getColor(0, R.color.colorPrimaryDark);
        ta.recycle();
        tintManager.setStatusBarTintColor(primaryDarkColor);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window window = getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        window.setAttributes(winParams);
    }

    /**
     * 返回上一层Activity
     */
    protected void navigateUp() {
        if (NavUtils.getParentActivityName(getGenericContext()) != null) {
            NavUtils.navigateUpFromSameTask(getGenericContext());
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
        DialogFactory.showProgressDialog(getGenericContext(), message);
    }

    @Override
    public void showMessageProgress(@StringRes int resId) {
        DialogFactory.showProgressDialog(getGenericContext(), getResources().getString(resId));
    }

    @Override
    public void showSimpleProgress() {
        DialogFactory.showProgressDialog(this.getGenericContext());
    }

    @Override
    public void closeDialog() {
        DialogFactory.dismissDialog();
    }

    @Override
    public Activity getGenericContext() {
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
        }
    }

    @Override
    public final void onLoaderReset(Loader<P> loader) {
        if (ID_PRESENTER_LOADER == loader.getId()) {
            mPresenter = null;
        }
    }

    @Override
    public void loadData() {

    }

    @Override
    public void bindData(Object data) {
    }

    /**
     * 在主线程安全执行
     *
     * @param runnable
     */
    public final void runOnUiThreadSafely(final Runnable runnable) {
        Utils.runOnUiThreadSafely(getGenericContext(), runnable);
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
        if (!NetworkUtils.isNetworkValid(getGenericContext())) {
            showToast("网络连接不可用");
            return false;
        }
        return true;
    }

    @LayoutRes
    protected abstract int getLayoutResId();
}
