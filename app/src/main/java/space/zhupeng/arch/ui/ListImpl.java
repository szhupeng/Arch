package space.zhupeng.arch.ui;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import butterknife.ButterKnife;

/**
 * @author zhupeng
 * @date 2017/9/24
 */

public class ListImpl<T> implements List<T> {

    private Context context;

    public ListImpl(Context context, View view) {
        this.context = context;
        ButterKnife.bind(this, view);
    }

    @Override
    public void initListView() {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public T getItem(int position) {
        return null;
    }

    @Override
    public void toRefreshList() {

    }

    @Override
    public void toLoadMoreData() {
    }

    @Override
    public void setDataToAdapterView() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
