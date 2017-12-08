package space.zhupeng.fxbase.adapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import space.zhupeng.fxbase.adapter.common.FlexibleLayoutManager;
import space.zhupeng.fxbase.adapter.common.IFlexibleLayoutManager;

import static space.zhupeng.fxbase.adapter.SelectableAdapter.Mode.IDLE;
import static space.zhupeng.fxbase.adapter.SelectableAdapter.Mode.MULTI;
import static space.zhupeng.fxbase.adapter.SelectableAdapter.Mode.SINGLE;

/**
 * 该类主要用于可选择的列表项
 */
@SuppressWarnings({"unused", "unchecked", "ConstantConditions", "WeakerAccess"})
public abstract class SelectableAdapter extends RecyclerView.Adapter {

    private static final String TAG = SelectableAdapter.class.getSimpleName();

    // 选择模式
    @SuppressLint("UniqueConstants")
    @IntDef({IDLE, SINGLE, MULTI})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
        /**
         * - <b>IDLE:</b> 适配器将不会跟踪选择<br>
         * - <b>SINGLE:</b> 一次只能选择一项<br>
         * - <b>MULTI:</b> 可以进行多项选择
         */
        int IDLE = 0, SINGLE = 1, MULTI = 2;
    }

    private final Set<Integer> mSelectedPositions;
    private final Set<FlexibleViewHolder> mBoundViewHolders;
    private int mMode;
    private IFlexibleLayoutManager mFlexibleLayoutManager;
    protected RecyclerView mRecyclerView;

    /**
     * 是否处于快速滚动状态
     */
    protected boolean isFastScroll = false;

    /**
     * 是否全选
     */
    protected boolean mSelectAll = false;

    /**
     * ActionMode selection flag LastItemInActionMode.
     * <p>Used when user returns to {@link Mode#IDLE} and no selection is active.</p>
     */
    protected boolean mLastItemInActionMode = false;

	/*--------------*/
    /*    构造器     */
    /*--------------*/

    public SelectableAdapter() {
        mSelectedPositions = Collections.synchronizedSet(new TreeSet<Integer>());
        mBoundViewHolders = new HashSet<>();
        mMode = IDLE;
    }

	/*--------------*/
    /*    主函数     */
    /*--------------*/

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mRecyclerView = null;
        mFlexibleLayoutManager = null;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * 适用于FlexibleAdapter的LayoutManager的包装类的实例
     * <p>
     * 返回任何非常规布局管理器的包装类，如果未初始化，则返回空
     */
    public IFlexibleLayoutManager getFlexibleLayoutManager() {
        if (mFlexibleLayoutManager == null) {
            RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
            if (layoutManager instanceof IFlexibleLayoutManager) {
                mFlexibleLayoutManager = (IFlexibleLayoutManager) layoutManager;
            } else if (layoutManager != null) {
                mFlexibleLayoutManager = new FlexibleLayoutManager(mRecyclerView);
            }
        }
        return this.mFlexibleLayoutManager;
    }

    /**
     * 允许使用自定义的LayoutManager
     *
     * @param flexibleLayoutManager 适用于FlexibleAdapter的LayoutManager
     */
    public void setFlexibleLayoutManager(IFlexibleLayoutManager flexibleLayoutManager) {
        this.mFlexibleLayoutManager = flexibleLayoutManager;
    }

    /**
     * 设置选择模式:
     * <ul>
     * <li>{@link Mode#IDLE} 默认值. 不可选;
     * <li>{@link Mode#SINGLE} 单选
     * <li>{@link Mode#MULTI} 多选
     * </ul>
     *
     * @param mode 值为{@link Mode#IDLE}, {@link Mode#SINGLE}, {@link Mode#MULTI}其中之一
     */
    public void setMode(@Mode int mode) {
        if (mMode == SINGLE && mode == IDLE)
            clearSelection();
        this.mMode = mode;
        this.mLastItemInActionMode = (mode != MULTI);
    }

    /**
     * 适配器当前选择模式
     *
     * @return 当前模式
     * @see Mode#IDLE
     * @see Mode#SINGLE
     * @see Mode#MULTI
     */
    @Mode
    public int getMode() {
        return mMode;
    }

    /**
     * @return 如果全选则返回true
     */
    public boolean isSelectAll() {
        // Reset the flags with delay
        resetActionModeFlags();
        return mSelectAll;
    }

    /**
     * @return 如果选择模式为{@link Mode#IDLE}或{@link Mode#SINGLE}并且未选中任何列表项则返回true否则返回false
     */
    public boolean isLastItemInActionMode() {
        // Reset the flags with delay
        resetActionModeFlags();
        return mLastItemInActionMode;
    }

    /**
     * 将ActionMode标志重置为false: {@code SelectAll} 和 {@code LastItemInActionMode}.
     */
    private void resetActionModeFlags() {
        if (mSelectAll || mLastItemInActionMode) {
            mRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSelectAll = false;
                    mLastItemInActionMode = false;
                }
            }, 200L);
        }
    }

    /**
     * 判断是否给定位置的列表项被选中
     *
     * @param position 列表项的位置
     * @return
     */
    public boolean isSelected(int position) {
        return mSelectedPositions.contains(position);
    }

    /**
     * 检查当前列表项是否有属性 {@code selectable = true}.
     *
     * @param position 待检查的项的位置
     * @return 如果列表项的属性</i>selectable</i>为true则返回true否则返回false
     */
    public abstract boolean isSelectable(int position);

    /**
     * 在给定位置切换列表项的选择状态
     * <p>The behaviour depends on the selection mode previously set with {@link #setMode(int)}.</p>
     * The Activated State of the ItemView is automatically set in
     * {@link FlexibleViewHolder#toggleActivation()} called in {@code onClick} event
     * <p><b>Usage:</b>
     * <ul>
     * <li>If you don't want any item to be selected/activated at all, just don't call this method.</li>
     * <li>To have actually the item visually selected you need to add a custom <i>Selector Drawable</i>
     * to the background of the View, via {@code DrawableUtils} or via layout's item:
     * <i>android:background="?attr/selectableItemBackground"</i>, pointing to a custom Drawable
     * in the style.xml (note: prefix <i>?android:attr</i> <u>doesn't</u> work).</li>
     * <li></li>
     * </ul></p>
     *
     * @param position Position of the item to toggle the selection status for.
     */
    public void toggleSelection(int position) {
        if (position < 0) return;
        if (mMode == SINGLE)
            clearSelection();

        boolean contains = mSelectedPositions.contains(position);
        if (contains) {
            removeSelection(position);
        } else {
            addSelection(position);
        }
    }

    /**
     * Adds the selection status for the given position without notifying the change.
     *
     * @param position Position of the item to add the selection status for.
     * @return true if the set is modified, false otherwise or position is not currently selectable
     * @see #isSelectable(int)
     */
    public final boolean addSelection(int position) {
        return isSelectable(position) && mSelectedPositions.add(position);
    }

    /**
     * This method is used only internally to force adjust selection.
     *
     * @param position Position of the item to add the selection status for.
     * @return true if the set is modified, false otherwise
     */
    final boolean addAdjustedSelection(int position) {
        return mSelectedPositions.add(position);
    }

    /**
     * Removes the selection status for the given position without notifying the change.
     *
     * @param position Position of the item to remove the selection status for.
     * @return true if the set is modified, false otherwise
     */
    public final boolean removeSelection(int position) {
        return mSelectedPositions.remove(position);
    }

    /**
     * Helper method to easily swap selection between 2 positions only if one of the positions
     * is <i>not</i> selected.
     *
     * @param fromPosition first position
     * @param toPosition   second position
     */
    protected void swapSelection(int fromPosition, int toPosition) {
        if (isSelected(fromPosition) && !isSelected(toPosition)) {
            removeSelection(fromPosition);
            addSelection(toPosition);
        } else if (!isSelected(fromPosition) && isSelected(toPosition)) {
            removeSelection(toPosition);
            addSelection(fromPosition);
        }
    }

    /**
     * Sets the selection status for all items which the ViewTypes are included in the specified array.
     *
     * @param viewTypes The ViewTypes for which we want the selection, pass nothing to select all
     */
    public void selectAll(Integer... viewTypes) {
        mSelectAll = true;
        List<Integer> viewTypesToSelect = Arrays.asList(viewTypes);
        int positionStart = 0, itemCount = 0;
        for (int i = 0; i < getItemCount(); i++) {
            if (isSelectable(i) &&
                    (viewTypesToSelect.isEmpty() || viewTypesToSelect.contains(getItemViewType(i)))) {
                mSelectedPositions.add(i);
                itemCount++;
            } else {
                // Optimization for ItemRangeChanged
                if (positionStart + itemCount == i) {
                    notifySelectionChanged(positionStart, itemCount);
                    itemCount = 0;
                    positionStart = i;
                }
            }
        }
        notifySelectionChanged(positionStart, getItemCount());
    }

    /**
     * Clears the selection status for all items one by one and it doesn't stop animations in the items.
     * <p>
     * <b>Note 1:</b> Items are not rebound, so an eventual animation is not stopped!<br>
     * <b>Note 2:</b> This method use {@code java.util.Iterator} on synchronized collection to
     * avoid {@code java.util.ConcurrentModificationException}.</p>
     */
    public void clearSelection() {
        // #373 - ConcurrentModificationException with Undo after multiple rapid swipe removals
        synchronized (mSelectedPositions) {
            Iterator<Integer> iterator = mSelectedPositions.iterator();
            int positionStart = 0, itemCount = 0;
            // The notification is done only on items that are currently selected.
            while (iterator.hasNext()) {
                int position = iterator.next();
                iterator.remove();
                // Optimization for ItemRangeChanged
                if (positionStart + itemCount == position) {
                    itemCount++;
                } else {
                    // Notify previous items in range
                    notifySelectionChanged(positionStart, itemCount);
                    positionStart = position;
                    itemCount = 1;
                }
            }
            // Notify remaining items in range
            notifySelectionChanged(positionStart, itemCount);
        }
    }

    private void notifySelectionChanged(int positionStart, int itemCount) {
        if (itemCount > 0) {
            // Avoid to rebind the VH, direct call to the itemView activation
            for (FlexibleViewHolder flexHolder : mBoundViewHolders) {
                flexHolder.toggleActivation();
            }
            // Use classic notification, in case FlexibleViewHolder is not implemented
            if (mBoundViewHolders.isEmpty())
                notifyItemRangeChanged(positionStart, itemCount, Payload.SELECTION);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        // Bind the correct view elevation
        if (holder instanceof FlexibleViewHolder) {
            FlexibleViewHolder flexHolder = (FlexibleViewHolder) holder;
            flexHolder.getContentView().setActivated(isSelected(position));
            if (flexHolder.getContentView().isActivated() && flexHolder.getActivationElevation() > 0)
                ViewCompat.setElevation(flexHolder.getContentView(), flexHolder.getActivationElevation());
            else if (flexHolder.getActivationElevation() > 0) //Leave unaltered the default elevation
                ViewCompat.setElevation(flexHolder.getContentView(), 0);
            mBoundViewHolders.add(flexHolder);
        } else {
            // When user scrolls, this line binds the correct selection status
            holder.itemView.setActivated(isSelected(position));
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        if (holder instanceof FlexibleViewHolder)
            mBoundViewHolders.remove(holder);
    }

    /**
     * Usually {@code RecyclerView} binds 3 items more than the visible items.
     *
     * @return a Set with all bound FlexibleViewHolders
     */
    public Set<FlexibleViewHolder> getAllBoundViewHolders() {
        return Collections.unmodifiableSet(mBoundViewHolders);
    }

    /**
     * Counts the selected items.
     *
     * @return Selected items count
     */
    public int getSelectedItemCount() {
        return mSelectedPositions.size();
    }

    /**
     * Retrieves the list of selected items.
     * <p>The list is a copy and it's sorted.</p>
     *
     * @return A copied List of selected items ids from the Set
     */
    public List<Integer> getSelectedPositions() {
        return new ArrayList<>(mSelectedPositions);
    }

    /**
     * Retrieves the set of selected items.
     * <p>The set is sorted.</p>
     *
     * @return Set of selected items ids
     */
    public Set<Integer> getSelectedPositionsAsSet() {
        return mSelectedPositions;
    }

	/*----------------*/
    /* INSTANCE STATE */
    /*----------------*/

    /**
     * Saves the state of the current selection on the items.
     *
     * @param outState Current state
     */
    public void onSaveInstanceState(Bundle outState) {
        outState.putIntegerArrayList(TAG, new ArrayList<>(mSelectedPositions));
    }

    /**
     * Restores the previous state of the selection on the items.
     *
     * @param savedInstanceState Previous state
     */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mSelectedPositions.addAll(savedInstanceState.getIntegerArrayList(TAG));
    }
}