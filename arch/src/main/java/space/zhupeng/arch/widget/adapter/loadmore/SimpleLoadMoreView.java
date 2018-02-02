package space.zhupeng.arch.widget.adapter.loadmore;

import space.zhupeng.arch.R;

public final class SimpleLoadMoreView extends LoadMoreView {

    @Override
    public int getLayoutResId() {
        return R.layout.layout_load_more;
    }

    @Override
    protected int getLoadingViewResId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewResId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewResId() {
        return R.id.load_more_load_end_view;
    }
}
