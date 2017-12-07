package space.zhupeng.fxbase.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import java.util.List;

import butterknife.BindView;
import space.zhupeng.fxbase.R;
import space.zhupeng.fxbase.adapter.BaseAdapter;
import space.zhupeng.fxbase.task.BaseExceptionAsyncTask;
import space.zhupeng.fxbase.utils.DensityUtils;
import space.zhupeng.fxbase.widget.ptr.PtrDefaultHandler;
import space.zhupeng.fxbase.widget.ptr.PtrFrameLayout;
import space.zhupeng.fxbase.widget.ptr.header.MaterialHeader;

/**
 * 展示列表的基类
 *
 * @author zhupeng
 * @date 2017/1/14
 */
public abstract class BaseListFragment<T, VH extends BaseAdapter.BaseViewHolder> extends BaseStateFragment implements AdapterView.OnItemClickListener {

    private static final int PAGE_START = 1;
    private static final int PAGE_SIZE = 15;

    @BindView(R.id.pull_to_refresh)
    PtrFrameLayout mPtrFrameLayout;

    @BindView(R.id.rv_data_list)
    RecyclerView rvDataList;

    private List<T> mDataList;
    private List<T> mOldList;
    private List<T> mMoreDataList;

    private int mPageIndex = PAGE_START;

    private BaseAdapter mAdapter;

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_list;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        initRecyclerView();

        final MaterialHeader header = new MaterialHeader(getActivity());
        int[] colors = getResources().getIntArray(R.array.ptr_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, DensityUtils.dp2px(getActivity(), 10), 0, DensityUtils.dp2px(getActivity(), 10));

        header.setPtrFrameLayout(mPtrFrameLayout);
        mPtrFrameLayout.setHeaderView(header);
        mPtrFrameLayout.addPtrUIHandler(header);
        mPtrFrameLayout.setEnabled(true);
        mPtrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                toRefreshList();
            }
        });
    }

    /**
     * 初始化RecyclerView
     * 可重写以实现网格布局和流式布局
     */
    protected void initRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvDataList.setLayoutManager(llm);
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
    private void toLoadMoreData() {
        if (!toCheckNetwork()) {
            onLoadMoreCompleted();
            return;
        }

        runOnUiThreadSafely(new Runnable() {
            @Override
            public void run() {
                executeTask(new BaseExceptionAsyncTask(getActivity()) {
                    @Override
                    protected boolean doInBackground() throws Exception {
                        loadMoreInBackground();
                        return true;
                    }

                    @Override
                    protected void onCompleted() {
                        onLoadMoreCompleted();
                    }

                    @Override
                    protected void onSuccess() {
                        onSuccessLoadMore();
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
                loadDataInBackground();
                return true;
            }

            @Override
            protected void onCompleted() {
                onLoadFinished();
            }

            @Override
            protected void onSuccess() {
                onSuccessLoadData();
            }

            @Override
            protected void onFailure() {
                showErrorView();
            }
        });
    }

    private void loadDataInBackground() {
        this.mPageIndex = PAGE_START;
        this.mOldList = this.mDataList;
        this.mDataList = toLoadData(this.mPageIndex);
    }

    private void loadMoreInBackground() {
        this.mMoreDataList = toLoadData(mPageIndex + 1);
        this.mOldList = this.mDataList;
        if (this.mMoreDataList != null) {
            this.mDataList.addAll(this.mMoreDataList);
        }
    }

    /**
     * 加载更多数据完毕
     */
    private void onLoadMoreCompleted() {
        onLoadFinished();
        // mPtrFrameLayout.loadMoreComplete(false);
    }

    /**
     * 是否允许加载更多数据
     *
     * @param enable
     */
    public void setLoadMoreEnabled(final boolean enable) {
        // mPtrFrameLayout.setLoadMoreEnable(enable);
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

    protected void onSuccessLoadData() {
        if (null == rvDataList) return;

        if (null == mAdapter) {
            mAdapter = new ListDataAdapter();
            mAdapter.setOnItemClickListener(this);
            rvDataList.setAdapter(mAdapter);
        }

        final int count = mDataList.size();
        if (0 == count) {
            showEmptyView();
            return;
        }

        showContentView();

        final DiffUtil.Callback callback = obtainDiffCallback(mOldList, mDataList);
        if (callback != null) {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
            result.dispatchUpdatesTo(mAdapter);
        } else {
            mAdapter.setData(mDataList);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void onSuccessLoadMore() {
        if (null == mMoreDataList) return;

        final int size = mMoreDataList.size();
        if (0 == size) {
            setLoadMoreEnabled(false);
        } else if (size < getPageSize()) {
            mPageIndex++;
            setLoadMoreEnabled(false);
        } else {
            mPageIndex++;
            setLoadMoreEnabled(true);
        }

        onLoadMoreCompleted();

        onSuccessLoadData();
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    /**
     * 加载数据
     *
     * @param pageIndex 分页页码
     * @return
     */
    protected abstract List<T> toLoadData(final int pageIndex);

    /**
     * 根据位置获取视图类型
     *
     * @param position
     * @return
     */
    protected int getItemType(final int position) {
        return 0;
    }

    /**
     * 根据视图类型获取布局对应的资源ID
     *
     * @param viewType
     * @return
     */
    protected abstract int getItemLayoutId(final int viewType);

    /**
     * 创建视图容器
     *
     * @param view
     * @param viewType
     * @return
     */
    protected abstract VH onCreateItemViewHolder(final View view, final int viewType);

    /**
     * 绑定数据
     *
     * @param holder
     * @param vo
     * @param position
     */
    protected abstract void onBindItemViewHolder(VH holder, T vo, int position);

    public class ListDataAdapter extends BaseAdapter<T, VH> {

        @Override
        public int getItemViewType(int position) {
            return getItemType(position);
        }

        @Override
        public int getItemLayoutResID(int viewType) {
            return getItemLayoutId(viewType);
        }

        @Override
        public VH onCreateViewHolder(View view, int viewType) {
            return onCreateItemViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(VH holder, T vo, int position) {
            onBindItemViewHolder(holder, vo, position);
        }
    }
}
