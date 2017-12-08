package space.zhupeng.fxbase.widget.adapter.loadmore;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.View;

import space.zhupeng.fxbase.widget.adapter.BaseViewHolder;

public abstract class LoadMoreView {

    public static final int STATUS_DEFAULT = 1;
    public static final int STATUS_LOADING = 2;
    public static final int STATUS_FAIL = 3;
    public static final int STATUS_END = 4;

    private int mLoadMoreStatus = STATUS_DEFAULT;
    private boolean mLoadMoreEndGone = false;

    public void setLoadMoreStatus(int status) {
        this.mLoadMoreStatus = status;
    }

    public int getLoadMoreStatus() {
        return mLoadMoreStatus;
    }

    public void convert(BaseViewHolder holder) {
        switch (mLoadMoreStatus) {
            case STATUS_LOADING:
                showLoading(holder);
                hideLoadFail(holder);
                hideLoadEnd(holder);
                break;
            case STATUS_FAIL:
                hideLoading(holder);
                showLoadFail(holder);
                hideLoadEnd(holder);
                break;
            case STATUS_END:
                hideLoading(holder);
                hideLoadFail(holder);
                showLoadEnd(holder);
                break;
            case STATUS_DEFAULT:
                hideLoading(holder);
                hideLoadFail(holder);
                hideLoadEnd(holder);
                break;
        }
    }

    private void showLoading(BaseViewHolder holder) {
        holder.setVisibility(getLoadingViewID(), View.VISIBLE);
    }

    private void hideLoading(BaseViewHolder holder) {
        holder.setVisibility(getLoadingViewID(), View.GONE);
    }

    private void showLoadFail(BaseViewHolder holder) {
        holder.setVisibility(getLoadFailViewID(), View.VISIBLE);
    }

    private void hideLoadFail(BaseViewHolder holder) {
        holder.setVisibility(getLoadFailViewID(), View.GONE);
    }

    private void showLoadEnd(BaseViewHolder holder) {
        final int loadEndViewId = getLoadEndViewID();
        if (loadEndViewId != 0) {
            holder.setVisibility(loadEndViewId, View.VISIBLE);
        }
    }

    private void hideLoadEnd(BaseViewHolder holder) {
        final int loadEndViewId = getLoadEndViewID();
        if (loadEndViewId != 0) {
            holder.setVisibility(loadEndViewId, View.GONE);
        }
    }

    public final void setLoadMoreEndGone(boolean loadMoreEndGone) {
        this.mLoadMoreEndGone = loadMoreEndGone;
    }

    public final boolean isLoadEndMoreGone() {
        if (getLoadEndViewID() == 0) {
            return true;
        }
        return mLoadMoreEndGone;
    }

    /**
     * load more layout
     *
     * @return
     */
    @LayoutRes
    public abstract int getLayoutResID();

    /**
     * loading view
     *
     * @return
     */
    @IdRes
    protected abstract int getLoadingViewID();

    /**
     * load fail view
     *
     * @return
     */
    @IdRes
    protected abstract int getLoadFailViewID();

    /**
     * load end view, you can return 0
     *
     * @return
     */
    @IdRes
    protected abstract int getLoadEndViewID();
}
