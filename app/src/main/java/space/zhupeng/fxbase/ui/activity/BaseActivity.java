package space.zhupeng.fxbase.ui.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
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
import space.zhupeng.fxbase.R;
import space.zhupeng.fxbase.manager.StatusBarTintManager;
import space.zhupeng.fxbase.mvp.presenter.BasePresenter;
import space.zhupeng.fxbase.mvp.presenter.PresenterFactory;
import space.zhupeng.fxbase.mvp.presenter.PresenterLoader;
import space.zhupeng.fxbase.mvp.view.BaseView;
import space.zhupeng.fxbase.utils.NetworkUtils;
import space.zhupeng.fxbase.utils.ToastUtils;
import space.zhupeng.fxbase.utils.Utils;
import space.zhupeng.fxbase.widget.dialog.DialogFactory;

/**
 * 业务无关的Activity基类，包括Toast，加载进度框等
 *
 * @author zhupeng
 * @date 2017/1/14
 */
@SuppressWarnings("deprecation")
public abstract class BaseActivity<M, V extends BaseView, P extends BasePresenter<M, V>> extends XActivity implements BaseView, LoaderManager.LoaderCallbacks<P> {

    private static final int LOADER_ID = 100;

    protected final Handler mHandler = new Handler(Looper.getMainLooper());

    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LayoutInflaterFactory factory = getInflaterFactory();
        if (factory != null) {
            LayoutInflaterCompat.setFactory(getLayoutInflater(), factory);
        }
        super.onCreate(savedInstanceState);

        doBeforeSetView();

        tintStatusBar();

        if (getLayoutResID() > 0) {
            setContentView(getLayoutResID());
        }

        ButterKnife.bind(this);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

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
        tintManager.setStatusBarTintResource(R.color.colorPrimary);
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
        if (NavUtils.getParentActivityName(getActivity()) != null) {
            NavUtils.navigateUpFromSameTask(getActivity());
        }
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
        DialogFactory.showProgressDialog(getActivity(), message);
    }

    @Override
    public void showMessageProgress(@StringRes int resId) {
        DialogFactory.showProgressDialog(getActivity(), getResources().getString(resId));
    }

    @Override
    public void showSimpleProgress() {
        DialogFactory.showProgressDialog(getActivity());
    }

    @Override
    public void closeDialog() {
        DialogFactory.dismissDialog();
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
        mPresenter = data;
    }

    @Override
    public final void onLoaderReset(Loader<P> loader) {
        mPresenter = null;
    }

    @Override
    public void loadData() {

    }

    @Override
    public void bindData() {
    }

    /**
     * 在主线程安全执行
     *
     * @param runnable
     */
    public final void runOnUiThreadSafely(final Runnable runnable) {
        Utils.runOnUiThreadSafely(getActivity(), runnable);
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
        if (!NetworkUtils.isNetworkValid(getActivity())) {
            showToast("网络连接不可用");
            return false;
        }
        return true;
    }

    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    protected BaseActivity getActivity() {
        return this;
    }

    @LayoutRes
    protected abstract int getLayoutResID();
}
