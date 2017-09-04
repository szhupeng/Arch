package space.zhupeng.fxbase.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.NavUtils;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.text.TextUtils;
import android.view.View;

import butterknife.ButterKnife;
import space.zhupeng.fxbase.presenter.BasePresenter;
import space.zhupeng.fxbase.utils.NetworkUtils;
import space.zhupeng.fxbase.utils.ToastUtils;
import space.zhupeng.fxbase.utils.Utils;
import space.zhupeng.fxbase.widget.dialog.DialogProvider;

/**
 * 业务无关的Activity基类，包括Toast，加载进度框等
 *
 * @author zhupeng
 * @date 2017/1/14
 */

public abstract class BaseActivity<P extends BasePresenter> extends XActivity {

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

        mPresenter = getPresenter();

        initView(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @Override
    protected int getContainerId() {
        return 0;
    }

    protected P getPresenter() {
        return null;
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
     * 状态栏着色
     */
    protected void tintStatusBar() {

    }

    /**
     * 返回上一层Activity
     */
    protected void navigateUp() {
        if (NavUtils.getParentActivityName(getActivity()) != null) {
            NavUtils.navigateUpFromSameTask(getActivity());
        }
    }

    protected void showToast(@NonNull final CharSequence text) {
        if (TextUtils.isEmpty(text)) return;

        ToastUtils.showShortSafely(getApplicationContext(), text);
    }

    protected void showToast(@StringRes final int resId) {
        showToast(getResources().getString(resId));
    }

    protected void showCustomToast(@LayoutRes final int layoutId) {
        ToastUtils.showCustomShortSafely(getApplicationContext(), layoutId);
    }

    protected void showCustomToast(@NonNull final View view) {
        ToastUtils.showCustomShortSafely(getApplicationContext(), view);
    }

    protected void showMessageProgress(final CharSequence message) {
        DialogProvider.showMessageProgress(message);
    }

    protected void showSimpleProgress() {
        DialogProvider.showSimpleProgress();
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
