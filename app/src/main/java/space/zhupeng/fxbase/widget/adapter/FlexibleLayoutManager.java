package space.zhupeng.fxbase.widget.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Internal wrapper class for all conventional LayoutManagers.
 */
public class FlexibleLayoutManager {

    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;

    /**
     * Providing RecyclerView is preferable for when the LayoutManager swaps.
     *
     * @param recyclerView the RecyclerView with LayoutManager in use
     * @see #FlexibleLayoutManager(RecyclerView.LayoutManager)
     */
    public FlexibleLayoutManager(RecyclerView recyclerView) {
        this(recyclerView.getLayoutManager());
        this.mRecyclerView = recyclerView;
    }

    /**
     * Directly rely on the current instance of LayoutManager.
     * Using this constructor it won't update the internal instance on LayoutManager swapping.
     *
     * @param layoutManager the current instance of LayoutManager
     * @see #FlexibleLayoutManager(RecyclerView)
     */
    public FlexibleLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

    /**
     * Finds the layout orientation of the RecyclerView, no matter which LayoutManager is in use.
     *
     * @return one of {@link OrientationHelper#HORIZONTAL}, {@link OrientationHelper#VERTICAL}
     */
    public int getOrientation() {
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).getOrientation();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).getOrientation();
        }
        return OrientationHelper.VERTICAL;
    }

    /**
     * Helper method to retrieve the number of the columns (span count) of the given LayoutManager.
     * <p>All Layouts are supported.</p>
     *
     * @return the span count
     */
    public int getSpanCount() {
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return 1;
    }

    /**
     * Helper method to find the adapter position of the <b>first completely</b> visible view
     * [for each span], no matter which Layout is.
     *
     * @return the adapter position of the <b>first fully</b> visible item or {@code RecyclerView.NO_POSITION}
     * if there aren't any visible items.
     * @see #findFirstVisibleItemPosition()
     */
    public int findFirstCompletelyVisibleItemPosition() {
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPositions(null)[0];
        } else {
            return ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
        }
    }

    /**
     * Helper method to find the adapter position of the <b>first partially</b> visible view
     * [for each span], no matter which Layout is.
     *
     * @return the adapter position of the <b>first partially</b> visible item or {@code RecyclerView.NO_POSITION}
     * if there aren't any visible items.
     * @see #findFirstCompletelyVisibleItemPosition()
     */
    public int findFirstVisibleItemPosition() {
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(null)[0];
        } else {
            return ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        }
    }

    /**
     * Helper method to find the adapter position of the <b>last completely</b> visible view
     * [for each span], no matter which Layout is.
     *
     * @return the adapter position of the <b>last fully</b> visible item or {@code RecyclerView.NO_POSITION}
     * if there aren't any visible items.
     * @see #findLastVisibleItemPosition()
     */
    public int findLastCompletelyVisibleItemPosition() {
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).findLastCompletelyVisibleItemPositions(null)[0];
        } else {
            return ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
        }
    }

    /**
     * Helper method to find the adapter position of the <b>last partially</b> visible view
     * [for each span], no matter which Layout is.
     *
     * @return the adapter position of the <b>last partially</b> visible item or {@code RecyclerView.NO_POSITION}
     * if there aren't any visible items.
     * @see #findLastCompletelyVisibleItemPosition()
     */
    public int findLastVisibleItemPosition() {
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null)[0];
        } else {
            return ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        }
    }

    /**
     * @return the current LayoutManager in use if RecyclerView was set
     */
    private RecyclerView.LayoutManager getLayoutManager() {
        return mRecyclerView != null ? mRecyclerView.getLayoutManager() : mLayoutManager;
    }

}