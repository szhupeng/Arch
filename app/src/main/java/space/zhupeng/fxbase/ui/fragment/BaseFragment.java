package space.zhupeng.fxbase.ui.fragment;

import android.app.Activity;
import android.arch.lifecycle.LifecycleObserver;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.concurrent.Executor;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import space.zhupeng.fxbase.mvp.model.BaseModel;
import space.zhupeng.fxbase.mvp.presenter.BasePresenter;
import space.zhupeng.fxbase.mvp.presenter.PresenterFactory;
import space.zhupeng.fxbase.mvp.presenter.PresenterLoader;
import space.zhupeng.fxbase.mvp.view.BaseView;
import space.zhupeng.fxbase.task.BaseAsyncTask;
import space.zhupeng.fxbase.utils.NetworkUtils;
import space.zhupeng.fxbase.utils.ToastUtils;
import space.zhupeng.fxbase.utils.Utils;
import space.zhupeng.fxbase.widget.dialog.DialogFactory;

/**
 * @author zhupeng
 * @date 2017/1/14
 */

public abstract class BaseFragment<M extends BaseModel, V extends BaseView, P extends BasePresenter<M, V>> extends XFragment implements BaseView, LoaderManager.LoaderCallbacks<P> {

    private static final int LOADER_ID = 200;

    protected P mPresenter;
    private BaseAsyncTask mTask;
    protected Activity mParentActivity;

    private Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.mParentActivity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(getLayoutResId(), container, false);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(LOADER_ID, null, this);

        fetchData();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        }
    }

    protected void initView(View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mPresenter != null) {
            mPresenter.detachView();
        }

        if (unbinder != null && unbinder != Unbinder.EMPTY) {
            unbinder.unbind();
            unbinder = null;
        }
    }

    public void showToast(@NonNull final CharSequence text) {
        if (TextUtils.isEmpty(text)) return;

        ToastUtils.showShortSafely(getActivity().getApplicationContext(), text);
    }

    @Override
    public void showToast(@StringRes final int resId) {
        if (isAdded()) {
            showToast(getResources().getString(resId));
        }
    }

    @Override
    public void showCustomToast(@LayoutRes final int layoutId) {
        ToastUtils.showCustomShortSafely(getActivity().getApplicationContext(), layoutId);
    }

    @Override
    public void showCustomToast(@NonNull final View view) {
        ToastUtils.showCustomShortSafely(getActivity().getApplicationContext(), view);
    }

    @Override
    public void showMessageProgress(@NonNull final CharSequence message) {
        DialogFactory.showProgressDialog(getActivity(), message);
    }

    @Override
    public void showMessageProgress(@StringRes final int resId) {
        if (isAdded()) {
            DialogFactory.showProgressDialog(getActivity(), getResources().getString(resId));
        }
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
        return new PresenterLoader(getActivity(), new PresenterFactory<P>() {
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
    final void loadDataLazily() {
        loadData();
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
    protected abstract int getLayoutResId();
}
