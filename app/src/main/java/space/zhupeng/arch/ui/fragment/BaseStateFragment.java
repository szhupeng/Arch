package space.zhupeng.arch.ui.fragment;

import android.support.annotation.Nullable;
import android.view.View;

import butterknife.BindView;
import space.zhupeng.arch.R;
import space.zhupeng.arch.mvp.model.Repository;
import space.zhupeng.arch.mvp.presenter.BasePresenter;
import space.zhupeng.arch.mvp.view.BaseView;
import space.zhupeng.arch.widget.MultiStateView;

/**
 * 根据不同状态展示不同界面的基类
 * 1.成功返回数据
 * 2.请求数据失败
 * 3.网络出现故障
 * 4.数据为空
 *
 * @author zhupeng
 * @date 2017/1/14
 */

public abstract class BaseStateFragment<M extends Repository, V extends BaseView, P extends BasePresenter<M, V>> extends BaseToolbarFragment<M, V, P> {

    @Nullable
    @BindView(R.id.multi_state_view)
    MultiStateView mMultiStateView;

    protected void showContentView() {
        if (null == mMultiStateView) return;

        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
    }

    protected void showEmptyView() {
        if (null == mMultiStateView) return;

        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
    }

    protected void showErrorView() {
        if (null == mMultiStateView) return;

        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);

        View view = mMultiStateView.getView(MultiStateView.VIEW_STATE_ERROR);
        if (view != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rerequest();
                }
            });
        }
    }

    protected void showLoadingView() {
        if (null == mMultiStateView) return;

        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
    }

    /**
     * 重新请求数据
     */
    protected void rerequest() {
    }
}
