package space.zhupeng.fxbase.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import space.zhupeng.fxbase.R;
import space.zhupeng.fxbase.task.BaseExceptionAsyncTask;
import space.zhupeng.fxbase.utils.DensityUtils;
import space.zhupeng.fxbase.widget.adapter.BaseAdapter;
import space.zhupeng.fxbase.widget.ptr.PtrDefaultHandler;
import space.zhupeng.fxbase.widget.ptr.PtrFrameLayout;
import space.zhupeng.fxbase.widget.ptr.header.MaterialHeader;
import space.zhupeng.fxbase.widget.ptr.header.PtrHeader;

/**
 * 展示列表的基类
 *
 * @author zhupeng
 * @date 2017/1/14
 */
public abstract class BaseListFragment<T> extends BaseStateFragment implements BaseAdapter.OnItemClickListener {

    private static final int PAGE_START = 1;
    private static final int PAGE_SIZE = 15;

    @BindView(R.id.pull_to_refresh)
    PtrFrameLayout mPtrFrameLayout;

    @BindView(R.id.rv_data_list)
    RecyclerView rvDataList;

    private int mPageIndex = PAGE_START;

    private BaseAdapter mAdapter;

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_list;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);

        initRecyclerView();

        PtrHeader header = getPtrHeaderView();
        header.setPtrFrameLayout(mPtrFrameLayout);
        mPtrFrameLayout.setHeaderView((View) header);
        mPtrFrameLayout.addPtrUIHandler(header);
        mPtrFrameLayout.setEnabled(true);
        mPtrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                toRefreshList();
            }
        });
    }

    protected PtrHeader getPtrHeaderView() {
        final MaterialHeader header = new MaterialHeader(getActivity());
        int[] colors = getResources().getIntArray(R.array.ptr_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, DensityUtils.dp2px(getActivity(), 10), 0, DensityUtils.dp2px(getActivity(), 10));
        return header;
    }

    /**
     * 初始化RecyclerView
     * 可重写以实现网格布局和流式布局
     */
    protected void initRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvDataList.setLayoutManager(llm);
        rvDataList.setItemViewCacheSize(0);
        rvDataList.setHasFixedSize(true);
        rvDataList.setItemAnimator(new DefaultItemAnimator());
        final RecyclerView.ItemDecoration decoration = onCreateDecoration();
        if (decoration != null) rvDataList.addItemDecoration(decoration);
    }

    protected int getItemCount() {
        if (null == mAdapter) return 0;

        return mAdapter.getItemCount();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getUserVisibleHint()) {
            requestData();
        }
    }

    public void setAdapter(@NonNull final BaseAdapter adapter) {
        this.mAdapter = adapter;
        this.mAdapter.setOnLoadMoreListener(new BaseAdapter.OnLoadMoreListener() {
            @Override
            public void loadMore() {
                toLoadMoreData();
            }
        }, rvDataList);
        this.mAdapter.setOnItemClickListener(this);
        this.rvDataList.setAdapter(adapter);
    }

    /**
     * 请求列表数据
     */
    public void requestData() {
        if (!toCheckNetwork()) {
            if (0 == getItemCount()) showErrorView();
            return;
        }

        toRefreshListData(true);
    }

    /**
     * 刷新列表数据
     */
    public void toRefreshList() {
        if (!toCheckNetwork()) {
            onLoadFinished();
            showErrorView();
            return;
        }

        toRefreshListData(false);
    }

    /**
     * 分页加载更多列表数据
     */
    public void toLoadMoreData() {
        if (!toCheckNetwork()) {
            mAdapter.loadMoreFailed();
            return;
        }

        runOnUiThreadSafely(new Runnable() {

            private List<T> mMoreData;

            @Override
            public void run() {
                executeTask(new BaseExceptionAsyncTask(getActivity()) {
                    @Override
                    protected boolean doInBackground() throws Exception {
                        mMoreData = toLoadData(mPageIndex + 1);
                        return true;
                    }

                    @Override
                    protected void onCompleted() {
                        onLoadFinished();
                    }

                    @Override
                    protected void onSuccess() {
                        onSuccessLoadData(false, mMoreData);
                    }

                    @Override
                    protected void onFailure() {
                        onLoadMoreFailed();
                    }
                });
            }
        });
    }

    /**
     * 加载刷新列表数据
     *
     * @param showLoading 是否显示加载界面
     */
    protected void toRefreshListData(final boolean showLoading) {
        executeTask(new BaseExceptionAsyncTask(getActivity()) {

            private List<T> mNewData;

            @Override
            protected void onPreExecute() {
                if (showLoading) showLoadingView();
            }

            @Override
            protected boolean doInBackground() throws Exception {
                mPageIndex = PAGE_START;
                mNewData = toLoadData(mPageIndex);
                return true;
            }

            @Override
            protected void onCompleted() {
                onLoadFinished();
                mAdapter.setEnableLoadMore(true);
            }

            @Override
            protected void onSuccess() {
                onSuccessLoadData(true, mNewData);
            }

            @Override
            protected void onFailure() {
                showErrorView();
            }
        });
    }

    /**
     * 加载更多数据完毕
     */
    private void onLoadMoreFailed() {
        if (null == mAdapter) return;

        runOnUiThreadSafely(new Runnable() {
            @Override
            public void run() {
                mAdapter.loadMoreFailed();
            }
        });
    }

    /**
     * 是否允许加载更多数据
     *
     * @param enable
     */
    public void setLoadMoreEnabled(final boolean enable) {
        if (null == mAdapter) return;

        mAdapter.setEnableLoadMore(enable);
    }

    protected RecyclerView.ItemDecoration onCreateDecoration() {
        return null;
    }

    /**
     * 每一页请求的数据条数，默认为{@link #PAGE_SIZE}条
     * 如果一次性请求所有数据，不需要分页，可以返回{@link java.lang.Integer#MAX_VALUE}
     *
     * @return
     */
    protected int getPageSize() {
        return PAGE_SIZE;
    }

    /**
     * 数据请求结束
     */
    protected void onLoadFinished() {
        if (mPtrFrameLayout != null) {
            mPtrFrameLayout.post(new Runnable() {
                @Override
                public void run() {
                    if (mPtrFrameLayout != null) {
                        mPtrFrameLayout.refreshComplete();
                    }
                }
            });
        }
    }

    protected void onSuccessLoadData(boolean isRefresh, final List<T> data) {
        if (null == rvDataList || null == mAdapter) return;

        final int size = data == null ? 0 : data.size();
        if (isRefresh && 0 == size) {
            showEmptyView();
        } else {
            showContentView();
        }

        if (isRefresh) {
            mAdapter.setData(data);
        } else if (size > 0) {
            mAdapter.addData(data);
        }

        if (size < PAGE_SIZE) {
            mAdapter.loadMoreEnd(isRefresh);
        } else {
            mAdapter.loadMoreComplete();
            mPageIndex++;
        }
    }

    @Override
    public void onItemClick(BaseAdapter adapter, View view, int position) {
    }

    /**
     * 获取DiffUtil.Callback
     *
     * @param oldData 旧数据集
     * @param newData 新数据集
     * @return
     */
    protected DiffUtil.Callback obtainDiffCallback(final List<T> oldData, final List<T> newData) {
        return null;
    }

    /**
     * 加载数据
     *
     * @param pageIndex 分页页码
     * @return
     */
    protected abstract List<T> toLoadData(final int pageIndex);
}
