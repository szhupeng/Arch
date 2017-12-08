package space.zhupeng.fxbase.widget.adapter.listener;

import android.view.View;

import space.zhupeng.fxbase.widget.adapter.BaseAdapter;

/**
 * A convenience class to extend when you only want to OnItemChildLongClickListener for a subset
 * of all the SimpleClickListener. This implements all methods in the
 * {@link SimpleClickListener}
 **/
public abstract class OnItemChildLongClickListener extends SimpleClickListener {
    @Override
    public void onItemClick(BaseAdapter adapter, View view, int position) {
    }

    @Override
    public void onItemLongClick(BaseAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(BaseAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(BaseAdapter adapter, View view, int position) {
        onSimpleItemChildLongClick(adapter, view, position);
    }

    public abstract void onSimpleItemChildLongClick(BaseAdapter adapter, View view, int position);
}
