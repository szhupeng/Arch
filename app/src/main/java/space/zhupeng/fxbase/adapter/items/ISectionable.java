package space.zhupeng.fxbase.adapter.items;

import android.support.v7.widget.RecyclerView;

/**
 * This interface represents an item in the section.
 */
public interface ISectionable<VH extends RecyclerView.ViewHolder, T extends IHeader>
        extends IFlexible<VH> {

    T getHeader();

    void setHeader(T header);
}