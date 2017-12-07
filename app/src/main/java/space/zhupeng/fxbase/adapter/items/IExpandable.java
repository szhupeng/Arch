package space.zhupeng.fxbase.adapter.items;

import java.util.List;

import space.zhupeng.fxbase.adapter.ExpandableViewHolder;

/**
 * Interface to manage expanding operations on items with {@link FlexibleAdapter}
 * <p>Implements this interface or use {@link AbstractExpandableItem}.</p>
 */
public interface IExpandable<VH extends ExpandableViewHolder, S extends IFlexible>
        extends IFlexible<VH> {

	/*--------------------*/
    /* EXPANDABLE METHODS */
    /*--------------------*/

    boolean isExpanded();

    void setExpanded(boolean expanded);

    /**
     * Establish the level of the expansion of this type of item in case of multi level expansion.
     * <p>Default value of first level should return 0.</p>
     * Sub expandable items should return a level +1 for each sub level.
     *
     * @return the level of the expansion of this type of item
     */
    int getExpansionLevel();

	/*-------------------*/
    /* SUB ITEMS METHODS */
    /*-------------------*/

    List<S> getSubItems();
}