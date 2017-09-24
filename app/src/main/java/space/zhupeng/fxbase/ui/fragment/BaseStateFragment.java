package space.zhupeng.fxbase.ui.fragment;

import android.support.annotation.Nullable;
import android.view.View;

import butterknife.Bind;
import space.zhupeng.fxbase.R;
import space.zhupeng.fxbase.widget.MultiStateView;

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

public abstract class BaseStateFragment extends BaseFragment {

    @Nullable
    @Bind(R.id.multi_state_view)
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

        attachEvent(mMultiStateView.getView(MultiStateView.VIEW_STATE_ERROR));
    }

    protected void showLoadingView() {
        if (null == mMultiStateView) return;

        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
    }

    /**
     * 对错误状态下的界面视图添加事件监听
     *
     * @param view
     */
    protected void attachEvent(final View view) {
    }
}
