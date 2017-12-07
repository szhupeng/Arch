package space.zhupeng.fxbase.adapter.common;

import android.support.v7.widget.OrientationHelper;

/**
 * Wrapper interface for any non-conventional LayoutManagers.
 * <p>This interface will make any third LayoutManager suitable for FlexibleAdapter.</p>
 */
public interface IFlexibleLayoutManager {

    /**
     * Finds the layout orientation of the RecyclerView, no matter which LayoutManager is in use.
     *
     * @return one of {@link OrientationHelper#HORIZONTAL}, {@link OrientationHelper#VERTICAL}
     */
    int getOrientation();

    /**
     * Helper method to retrieve the number of the columns (span count) of the given LayoutManager.
     * <p>All Layouts are supported.</p>
     *
     * @return the span count
     */
    int getSpanCount();

    /**
     * Helper method to find the adapter position of the <b>first completely</b> visible view
     * [for each span], no matter which Layout is.
     *
     * @return the adapter position of the <b>first fully</b> visible item or {@code RecyclerView.NO_POSITION}
     * if there aren't any visible items.
     * @see #findFirstVisibleItemPosition()
     */
    int findFirstCompletelyVisibleItemPosition();

    /**
     * Helper method to find the adapter position of the <b>first partially</b> visible view
     * [for each span], no matter which Layout is.
     *
     * @return the adapter position of the <b>first partially</b> visible item or {@code RecyclerView.NO_POSITION}
     * if there aren't any visible items.
     * @see #findFirstCompletelyVisibleItemPosition()
     */
    int findFirstVisibleItemPosition();

    /**
     * Helper method to find the adapter position of the <b>last completely</b> visible view
     * [for each span], no matter which Layout is.
     *
     * @return the adapter position of the <b>last fully</b> visible item or {@code RecyclerView.NO_POSITION}
     * if there aren't any visible items.
     * @see #findLastVisibleItemPosition()
     */
    int findLastCompletelyVisibleItemPosition();

    /**
     * Helper method to find the adapter position of the <b>last partially</b> visible view
     * [for each span], no matter which Layout is.
     *
     * @return the adapter position of the <b>last partially</b> visible item or {@code RecyclerView.NO_POSITION}
     * if there aren't any visible items.
     * @see #findLastCompletelyVisibleItemPosition()
     */
    int findLastVisibleItemPosition();

}