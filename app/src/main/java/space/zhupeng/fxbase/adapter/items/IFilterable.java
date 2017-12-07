package space.zhupeng.fxbase.adapter.items;

/**
 * When user wants to search through the list, in order to be to collected, an item must implement
 * this interface.
 */
public interface IFilterable {

    /**
     * Checks and performs the filter on this item, you can apply the logic and the filter on
     * every fields your use case foresees.
     * <p><b>Note:</b> Filter method makes use of {@code HashSet}, in case you implemented
     * {@link Object#equals(Object)} you should implement {@link Object#hashCode()} too!</p>
     *
     * @param constraint the search text typed by the user always provided in lowercase
     * @return true if this item should be collected by the Adapter for the filtered list, false otherwise
     */
    boolean filter(String constraint);

}