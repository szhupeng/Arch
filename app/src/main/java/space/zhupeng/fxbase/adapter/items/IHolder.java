package space.zhupeng.fxbase.adapter.items;

/**
 * Simple interface to configure an item that holds the model object.
 */
public interface IHolder<Model> {

    /**
     * @return the model object
     */
    Model getModel();
}