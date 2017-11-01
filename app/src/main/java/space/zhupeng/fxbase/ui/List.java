package space.zhupeng.fxbase.ui;

import android.widget.AdapterView;

/**
 * @author zhupeng
 * @date 2017/9/24
 */

public interface List<T> extends AdapterView.OnItemClickListener {

    /**
     * 初始化列表视图
     */
    void initListView();

    /**
     * 获取列表项数目
     *
     * @return
     */
    int getItemCount();

    /**
     * @param position
     * @return
     */
    T getItem(int position);

    /**
     * 刷新列表数据
     */
    void toRefreshList();

    /**
     * 加载更多数据
     */
    void toLoadMoreData();

    /**
     * 设置数据
     */
    void setDataToAdapterView();
}
