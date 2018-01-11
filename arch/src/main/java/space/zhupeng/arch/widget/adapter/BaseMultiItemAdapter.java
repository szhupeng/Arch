package space.zhupeng.arch.widget.adapter;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;

import java.util.List;

import space.zhupeng.arch.widget.adapter.entity.IExpandable;
import space.zhupeng.arch.widget.adapter.entity.MultiItemEntity;

public abstract class BaseMultiItemAdapter<T extends MultiItemEntity, VH extends BaseViewHolder> extends BaseAdapter<T, VH> {

    public BaseMultiItemAdapter(Context context) {
        super(context);
    }

    public BaseMultiItemAdapter(Context context, @Nullable List<T> data) {
        super(context, data);
    }

    @Override
    protected final int getContentItemType(int position) {
        T item = mDataSet.get(position);
        return item.getItemType();
    }

    @Override
    public void remove(@IntRange(from = 0L) int position) {
        if (mDataSet == null
                || position < 0
                || position >= mDataSet.size()) return;

        T entity = mDataSet.get(position);
        if (entity instanceof IExpandable) {
            removeAllChild((IExpandable) entity, position);
        }
        removeDataFromParent(entity);
        super.remove(position);
    }

    /**
     * 移除父控件时，若父控件处于展开状态，则先移除其所有的子控件
     *
     * @param parent
     * @param parentPosition
     */
    protected void removeAllChild(IExpandable parent, int parentPosition) {
        if (parent.isExpanded()) {
            List<MultiItemEntity> children = parent.getSubItems();
            if (children == null || children.size() == 0) return;

            int childSize = children.size();
            for (int i = 0; i < childSize; i++) {
                remove(parentPosition + 1);
            }
        }
    }

    /**
     * 移除子控件时，移除父控件实体类中相关子控件数据，避免关闭后再次展开数据重现
     *
     * @param child
     */
    protected void removeDataFromParent(T child) {
        int position = getParentPosition(child);
        if (position >= 0) {
            IExpandable parent = (IExpandable) mDataSet.get(position);
            parent.getSubItems().remove(child);
        }
    }
}
