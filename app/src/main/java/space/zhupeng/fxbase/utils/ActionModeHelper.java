package space.zhupeng.fxbase.utils;

import android.support.annotation.CallSuper;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import space.zhupeng.fxbase.widget.adapter.BaseAdapter;

public class ActionModeHelper implements ActionMode.Callback {

    @MenuRes
    private int mCabMenu;
    private ActionMode.Callback mCallback;
    protected ActionMode mActionMode;

    private BaseAdapter mAdapter;

    /**
     * Default constructor with internal callback.
     *
     * @param adapter the FlexibleAdapter instance
     * @param cabMenu the Contextual Action Bar menu resourceId
     */
    public ActionModeHelper(BaseAdapter adapter, @MenuRes int cabMenu) {
        this.mAdapter = adapter;
        this.mCabMenu = cabMenu;
    }

    /**
     * Constructor with internal callback + custom callback.
     *
     * @param adapter  the FlexibleAdapter instance
     * @param cabMenu  the Contextual Action Bar menu resourceId
     * @param callback the custom {@link ActionMode.Callback}
     */
    public ActionModeHelper(@NonNull BaseAdapter adapter, @MenuRes int cabMenu,
                            @Nullable ActionMode.Callback callback) {
        this(adapter, cabMenu);
        this.mCallback = callback;
    }

    /**
     * @return the current instance of the ActionMode, {@code null} if ActionMode is off.
     */
    public ActionMode getActionMode() {
        return mActionMode;
    }

    /**
     * Implements the basic behavior of a CAB and multi select behavior onLongClick.
     *
     * @param activity the current Activity
     * @param position the position of the clicked item
     * @return the initialized ActionMode or null if nothing was done
     */
    @NonNull
    public ActionMode onLongClick(AppCompatActivity activity, int position) {
        // Activate ActionMode
        if (mActionMode == null) {
            mActionMode = activity.startSupportActionMode(this);
        }
        // We have to select this on our own as we will consume the event
        return mActionMode;
    }

    /**
     * Updates the title of the Context Menu.
     * <p>Override to customize the title and subtitle.</p>
     *
     * @param count the current number of selected items
     */
    public void updateContextTitle(int count) {
        if (mActionMode != null) {
            mActionMode.setTitle(String.valueOf(count));
        }
    }

    @CallSuper
    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        // Inflate the Context Menu
        actionMode.getMenuInflater().inflate(mCabMenu, menu);
        // Activate the ActionMode Multi
        // Disable Swipe and Drag capabilities as per settings
        // Notify the provided callback
        return mCallback == null || mCallback.onCreateActionMode(actionMode, menu);
    }

    @CallSuper
    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return mCallback != null && mCallback.onPrepareActionMode(actionMode, menu);
    }

    @CallSuper
    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem item) {
        boolean consumed = false;
        if (mCallback != null) {
            consumed = mCallback.onActionItemClicked(actionMode, item);
        }
        if (!consumed) {
            // Finish the actionMode
            actionMode.finish();
        }
        return consumed;
    }

    @CallSuper
    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        // Change mode and deselect everything
        mActionMode = null;
        // Notify the provided callback
        if (mCallback != null) {
            mCallback.onDestroyActionMode(actionMode);
        }
    }

    /**
     * Utility method to be called from Activity in many occasions such as: <i>onBackPressed</i>,
     * <i>onRefresh</i> for SwipeRefreshLayout, after <i>deleting</i> all selected items.
     *
     * @return true if ActionMode was active (in case it is also terminated), false otherwise
     */
    public boolean destroyActionModeIfCan() {
        if (mActionMode != null) {
            mActionMode.finish();
            return true;
        }
        return false;
    }
}