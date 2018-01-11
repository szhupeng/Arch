package space.zhupeng.arch.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;

import java.util.List;

import space.zhupeng.arch.R;
import space.zhupeng.arch.task.BaseExceptionAsyncTask;
import space.zhupeng.arch.utils.DataUtils;
import space.zhupeng.arch.utils.DensityUtils;
import space.zhupeng.arch.widget.adapter.BaseAdapter;
import space.zhupeng.arch.widget.ptr.PtrDefaultHandler;
import space.zhupeng.arch.widget.ptr.PtrFrameLayout;
import space.zhupeng.arch.widget.ptr.header.MaterialHeader;
import space.zhupeng.arch.widget.ptr.header.PtrHeader;

/**
 * 展示列表的基类
 *
 * @author zhupeng
 * @date 2017/1/14
 */
public abstract class BaseListFragment<T> extends BaseStateFragment implements BaseAdapter.OnItemClickListener {

    private static final int PAGE_START = 1;
    private static final int PAGE_SIZE = 15;

    protected PtrFrameLayout mPtrFrameLayout;
    protected RecyclerView rvDataList;

    private int mPageIndex = PAGE_START;

    private BaseAdapter mAdapter;

    private List<T> mDataSource;
    private boolean isRefresh = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_list;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);

        mPtrFrameLayout = (PtrFrameLayout) view.findViewById(R.id.pull_to_refresh);
        rvDataList = (RecyclerView) view.findViewById(R.id.rv_data_list);

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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mDataSource != null && !mDataSource.isEmpty()) {
            outState.putString("json-data", new Gson().toJson(mDataSource));
        }
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

    /**
     * 请求列表数据
     */
    @Override
    public void loadData() {
        if (!isNetworkAvailable()) {
            if (0 == getItemCount()) showErrorView();
            return;
        }

        toRefreshListData(true);
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
     * 刷新列表数据
     */
    public void toRefreshList() {
        if (!isNetworkAvailable()) {
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
        if (!isNetworkAvailable()) {
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
                        mMoreData = toLoadData(mPageIndex);
                        return true;
                    }

                    @Override
                    protected void onCompleted() {
                        onLoadFinished();
                    }

                    @Override
                    protected void onSuccess() {
                        isRefresh = false;
                        bindData(mMoreData);
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

            @Override
            protected void onPreExecute() {
                if (showLoading) showLoadingView();
            }

            @Override
            protected boolean doInBackground() throws Exception {
                mPageIndex = PAGE_START;
                mDataSource = toLoadData(mPageIndex);
                return true;
            }

            @Override
            protected void onCompleted() {
                onLoadFinished();
                mAdapter.setEnableLoadMore(true);
            }

            @Override
            protected void onSuccess() {
                isRefresh = true;
                bindData(mDataSource);
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

    /**
     * 子类中的onActivityCreated中调用
     *
     * @param savedInstanceState
     * @param cls
     */
    protected void bindSavedData(@Nullable Bundle savedInstanceState, Class<? extends T> cls) {
        if (savedInstanceState != null && savedInstanceState.containsKey("json-data")) {
            String jsonData = savedInstanceState.getString("json-data");
            isRefresh = true;
            bindData(DataUtils.getObjectList(jsonData, cls));
        } else {
            fetchData(true);
        }
    }

    @Override
    public void bindData(Object data) {
        if (null == rvDataList || null == mAdapter) return;

        List<T> ds = (List<T>) data;

        final int size = ds == null ? 0 : ds.size();
        if (isRefresh && 0 == size) {
            showEmptyView();
        } else {
            showContentView();
        }

        if (isRefresh) {
            mAdapter.setData(ds);
        } else if (size > 0) {
            mAdapter.addData(ds);
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

    @Override
    protected void rerequest() {
        loadData();
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
