package space.zhupeng.fxbase.widget.adapter;

import android.support.v7.util.DiffUtil;

import java.util.List;

/**
 * DiffUtil.Callback基类
 *
 * @author zhupeng
 * @date 2017/8/20
 */

public abstract class DiffCallback<T> extends DiffUtil.Callback {

    private final List<T> mOldData;
    private final List<T> mNewData;

    public DiffCallback(final List<T> oldData, final List<T> newData) {
        this.mOldData = oldData;
        this.mNewData = newData;
    }

    @Override
    public int getOldListSize() {
        if (mOldData != null) {
            return mOldData.size();
        }
        return 0;
    }

    @Override
    public int getNewListSize() {
        if (mNewData != null) {
            return mNewData.size();
        }
        return 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        if (mOldData != null && mNewData != null) {
            return mOldData.get(oldItemPosition).getClass().equals(mNewData.get(newItemPosition).getClass());
        }
        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final T oldItem = mOldData.get(oldItemPosition);
        final T newItem = mNewData.get(newItemPosition);
        return areContentsTheSame(oldItem, newItem);
    }

    public abstract boolean areContentsTheSame(final T oldItem, final T newItem);
}
