package space.zhupeng.arch.widget.adapter.loadmore;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.View;

import space.zhupeng.arch.widget.adapter.BaseViewHolder;

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
                visibleLoading(holder, true);
                visibleLoadFail(holder, false);
                visibleLoadEnd(holder, false);
                break;
            case STATUS_FAIL:
                visibleLoading(holder, false);
                visibleLoadFail(holder, true);
                visibleLoadEnd(holder, false);
                break;
            case STATUS_END:
                visibleLoading(holder, false);
                visibleLoadFail(holder, false);
                visibleLoadEnd(holder, true);
                break;
            case STATUS_DEFAULT:
                visibleLoading(holder, false);
                visibleLoadFail(holder, false);
                visibleLoadEnd(holder, false);
                break;
        }
    }

    private void visibleLoading(BaseViewHolder holder, boolean visible) {
        holder.setVisibility(getLoadingViewResId(), visible ? View.VISIBLE : View.GONE);
    }

    private void visibleLoadFail(BaseViewHolder holder, boolean visible) {
        holder.setVisibility(getLoadFailViewResId(), visible ? View.VISIBLE : View.GONE);
    }

    private void visibleLoadEnd(BaseViewHolder holder, boolean visible) {
        final int loadEndViewId = getLoadEndViewResId();
        if (loadEndViewId != 0) {
            holder.setVisibility(loadEndViewId, visible ? View.VISIBLE : View.GONE);
        }
    }

    public final void setLoadMoreEndGone(boolean loadMoreEndGone) {
        this.mLoadMoreEndGone = loadMoreEndGone;
    }

    public final boolean isLoadEndMoreGone() {
        if (getLoadEndViewResId() == 0) {
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
    public abstract int getLayoutResId();

    /**
     * loading view
     *
     * @return
     */
    @IdRes
    protected abstract int getLoadingViewResId();

    /**
     * load fail view
     *
     * @return
     */
    @IdRes
    protected abstract int getLoadFailViewResId();

    /**
     * load end view, you can return 0
     *
     * @return
     */
    @IdRes
    protected abstract int getLoadEndViewResId();
}
