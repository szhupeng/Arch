package space.zhupeng.fxbase.widget.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import java.util.List;

import space.zhupeng.fxbase.widget.adapter.entity.SectionEntity;

public abstract class BaseSectionAdapter<T extends SectionEntity, VH extends BaseViewHolder> extends BaseAdapter<T, VH> {

    protected static final int TYPE_SECTION_HEADER = 0x00000444;

    public BaseSectionAdapter(Context context) {
        super(context);
    }

    public BaseSectionAdapter(Context context, @Nullable List<T> data) {
        super(context, data);
    }

    @Override
    protected final int getContentItemType(int position) {
        return mDataSet.get(position).isHeader ? TYPE_SECTION_HEADER : 0;
    }

    @Override
    protected boolean isFixedViewType(int type) {
        return super.isFixedViewType(type) || type == TYPE_SECTION_HEADER;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_SECTION_HEADER:
                setFullSpan(holder);
                convertHeader(holder, getItem(position - getHeaderLayoutCount()));
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    protected abstract void convertHeader(VH holder, T item);
}
