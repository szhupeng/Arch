package space.zhupeng.fxbase.adapter.items;

import space.zhupeng.fxbase.adapter.ExpandableViewHolder;

/**
 * Generic implementation of {@link IExpandable} interface combined with {@link IHeader} interface
 * with most useful methods to manage expandable sections with sticky headers and sub items of
 * type {@link ISectionable}.
 * <p>This abstract class extends {@link AbstractExpandableItem}.</p>
 * Call {@code super()} in the constructor to auto-configure the section status as: shown,
 * expanded, not selectable.
 *
 * @param <VH> {@link ExpandableViewHolder}
 * @param <S>  The sub item of type {@link ISectionable}
 */
public abstract class AbstractExpandableHeaderItem<VH extends ExpandableViewHolder, S extends ISectionable>
        extends AbstractExpandableItem<VH, S>
        implements IHeader<VH> {

    /**
     * By default, expandable header is shown, expanded and not selectable.
     */
    public AbstractExpandableHeaderItem() {
        setHidden(false);
        setExpanded(true);
        setSelectable(false);
    }

}