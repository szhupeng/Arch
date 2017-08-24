package space.zhupeng.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.concurrent.Executor;

import butterknife.ButterKnife;
import space.zhupeng.base.presenter.BasePresenter;
import space.zhupeng.base.task.BaseAsyncTask;
import space.zhupeng.base.utils.NetworkUtils;
import space.zhupeng.base.utils.ToastUtils;
import space.zhupeng.base.utils.Utils;
import space.zhupeng.base.widget.dialog.DialogFactory;

/**
 * @author zhupeng
 * @date 2017/1/14
 */

public abstract class BaseFragment<P extends BasePresenter> extends XFragment {

    protected P mPresenter;

    private BaseAsyncTask mTask;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(getLayoutResID(), container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter = getPresenter();

        initView(savedInstanceState);
    }

    protected void initView(@Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            onVisible();
        } else {
            onInvisible();
        }
    }

    /**
     * fragment可见
     */
    protected void onVisible() {
    }

    /**
     * fragment不可见
     */
    protected void onInvisible() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ButterKnife.unbind(this);
    }

    protected P getPresenter() {
        return null;
    }

    protected void showToast(@NonNull final CharSequence text) {
        if (TextUtils.isEmpty(text)) return;

        ToastUtils.showShortSafely(getActivity().getApplicationContext(), text);
    }

    protected void showToast(@StringRes final int resId) {
        showToast(getResources().getString(resId));
    }

    protected void showCustomToast(@LayoutRes final int layoutId) {
        ToastUtils.showCustomShortSafely(getActivity().getApplicationContext(), layoutId);
    }

    protected void showCustomToast(@NonNull final View view) {
        ToastUtils.showCustomShortSafely(getActivity().getApplicationContext(), view);
    }

    protected void showMessageProgress() {
        DialogFactory.showMessageProgress();
    }

    protected void showSimpleProgress() {
        DialogFactory.showSimpleProgress();
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
     * 执行单个异步任务
     *
     * @param task
     */
    protected void executeTask(final BaseAsyncTask task) {
        Utils.cancelTask(mTask);
        task.execute();
        this.mTask = task;
    }

    /**
     * 在执行器上执行单个异步任务
     *
     * @param task
     * @param exec
     */
    protected void executeTaskOnExecutor(final BaseAsyncTask task, final Executor exec) {
        Utils.cancelTask(mTask);
        task.executeOnExecutor(exec);
        this.mTask = task;
    }

    /**
     * 检查网络是否可用
     *
     * @return
     */
    protected final boolean toCheckNetwork() {
        if (getActivity() == null) {
            return false;
        }

        if (!NetworkUtils.isNetworkValid(getActivity())) {
            showToast("网络连接不可用");
            return false;
        }
        return true;
    }

    protected <T extends View> T findView(int id) {
        return (T) getView().findViewById(id);
    }

    @LayoutRes
    protected abstract int getLayoutResID();
}
