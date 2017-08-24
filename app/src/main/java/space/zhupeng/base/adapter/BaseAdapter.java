package space.zhupeng.base.adapter;

import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Checkable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * RecyclerView适配器基类
 *
 * @author zhupeng
 * @date 2017/8/18
 */

public abstract class BaseAdapter<T, VH extends BaseAdapter.BaseViewHolder> extends RecyclerView.Adapter<VH> {

    public static final int NONE = 0;
    public static final int SINGLE = 1;
    public static final int MULTIPLE = 2;

    @IntDef({NONE, SINGLE, MULTIPLE})
    @Target(ElementType.PARAMETER)
    private @interface ChoiceMode {
    }

    private int mChoiceMode = NONE;
    private SparseBooleanArray mCheckStates;
    private int mCheckedItemCount;

    protected List<T> mDataList;

    private AdapterView.OnItemClickListener mItemClickListener;

    public BaseAdapter() {
    }

    public void setData(final List<T> list) {
        if (null == list) return;

        if (null == mDataList) {
            mDataList = new ArrayList<>();
        }

        mDataList.clear();
        mDataList.addAll(list);
    }

    public void addData(final T data) {
        if (null == data) return;

        if (null == mDataList) {
            mDataList = new ArrayList<>();
        }

        if (!mDataList.contains(data)) {
            mDataList.add(data);
        }
    }

    public void clear() {
        if (mDataList != null) {
            mDataList.clear();
        }
    }

    /**
     * 设置选择模式，不可选，单选，多选
     *
     * @param mode 选择模式
     */
    public void setChoiceMode(@ChoiceMode int mode) {
        mChoiceMode = mode;
        if (mChoiceMode != NONE) {
            if (mCheckStates == null) {
                mCheckStates = new SparseBooleanArray(0);
            }
        }
    }

    @Override
    public final VH onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutResID(viewType), parent, false);
        final VH holder = onCreateViewHolder(view, viewType);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = holder.getAdapterPosition();

                if (mChoiceMode != NONE && mDataList.get(position) instanceof Checkable) {
                    if (mChoiceMode == SINGLE) {
                        boolean checked = !mCheckStates.get(position, false);
                        if (mCheckedItemCount == 1 && mCheckStates.valueAt(0)) {
                            int lastSelectedPosition = mCheckStates.keyAt(0);
                            ((Checkable) mDataList.get(lastSelectedPosition)).setChecked(false);
                            mCheckedItemCount = 0;
                            mCheckStates.clear();
                            notifyItemChanged(lastSelectedPosition);
                        }
                        if (checked) {
                            mCheckStates.clear();
                            mCheckStates.put(position, true);
                            mCheckedItemCount = 1;
                            ((Checkable) mDataList.get(position)).setChecked(true);
                        }
                    } else if (mChoiceMode == MULTIPLE) {
                        boolean checked = !mCheckStates.get(position, false);
                        mCheckStates.put(position, checked);
                        ((Checkable) mDataList.get(position)).toggle();
                        if (checked) {
                            mCheckedItemCount++;
                        } else {
                            mCheckedItemCount--;
                        }
                    }
                    notifyItemChanged(position);
                }

                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(null, view, position, getItemId(position));
                }
            }
        });
        return holder;
    }

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        onBindViewHolder(holder, getItem(position), position);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return null == mDataList ? 0 : mDataList.size();
    }

    public T getItem(final int position) {
        if (null == mDataList) return null;

        return mDataList.get(position);
    }

    @LayoutRes
    public abstract int getLayoutResID(final int viewType);

    public abstract VH onCreateViewHolder(final View view, final int viewType);

    public abstract void onBindViewHolder(final VH holder, final T vo, final int position);

    public static class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
