package space.zhupeng.fxbase.widget.adapter;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import space.zhupeng.fxbase.widget.adapter.animation.AlphaInAnimation;
import space.zhupeng.fxbase.widget.adapter.animation.BaseAnimation;
import space.zhupeng.fxbase.widget.adapter.animation.ScaleInAnimation;
import space.zhupeng.fxbase.widget.adapter.animation.SlideInBottomAnimation;
import space.zhupeng.fxbase.widget.adapter.animation.SlideInLeftAnimation;
import space.zhupeng.fxbase.widget.adapter.animation.SlideInRightAnimation;
import space.zhupeng.fxbase.widget.adapter.entity.IExpandable;
import space.zhupeng.fxbase.widget.adapter.loadmore.LoadMoreView;
import space.zhupeng.fxbase.widget.adapter.loadmore.SimpleLoadMoreView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public abstract class BaseAdapter<T, VH extends BaseViewHolder> extends RecyclerView.Adapter<VH> {

    public static final int TYPE_HEADER_VIEW = 0x00000111;
    public static final int TYPE_FOOTER_VIEW = 0x00000222;
    public static final int TYPE_LOADMORE_VIEW = 0x00000333;

    private boolean mNextLoadEnable = false;
    private boolean mLoadMoreEnable = false;
    private boolean isLoadingMore = false;
    private LoadMoreView mLoadMoreView = new SimpleLoadMoreView();
    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean mEnableLoadMoreEndClick = false;

    private boolean isHeaderViewAsFlow, isFooterViewAsFlow;

    //Animation
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int ALPHAIN = 0x00000001;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SCALEIN = 0x00000002;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SLIDEIN_BOTTOM = 0x00000003;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SLIDEIN_LEFT = 0x00000004;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SLIDEIN_RIGHT = 0x00000005;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private OnChildClickListener mOnChildClickListener;
    private OnChildLongClickListener mOnChildLongClickListener;

    @IntDef({ALPHAIN, SCALEIN, SLIDEIN_BOTTOM, SLIDEIN_LEFT, SLIDEIN_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AnimationType {
    }

    private boolean mFirstOnlyEnable = true;
    private boolean mOpenAnimationEnable = false;
    private Interpolator mInterpolator = new LinearInterpolator();
    private int mDuration = 300;
    private int mLastPosition = -1;

    private int mPreLoadNumber = 1;

    private BaseAnimation mCustomAnimation;
    private BaseAnimation mSelectAnimation = new AlphaInAnimation();

    protected Context context;
    protected List<T> mDataSet;

    private LinearLayout mHeaderLayout;
    private LinearLayout mFooterLayout;

    private SpanSizeLookup mSpanSizeLookup;

    private RecyclerView mRecyclerView;

    protected RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public BaseAdapter(Context context) {
        this(context, null);
    }

    public BaseAdapter(Context context, @Nullable List<T> data) {
        this.context = context;
        this.mDataSet = data == null ? new ArrayList<T>() : data;
    }

    public final void setData(@Nullable List<T> data) {
        this.mDataSet = data == null ? new ArrayList<T>() : data;
        if (mOnLoadMoreListener != null) {
            mNextLoadEnable = true;
            mLoadMoreEnable = true;
            isLoadingMore = false;
            mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
        }
        mLastPosition = -1;
        notifyDataSetChanged();
    }

    public final void addData(@IntRange(from = 0) int position, @NonNull T data) {
        if (null == data) return;

        mDataSet.add(position, data);
        notifyItemInserted(position + getHeaderLayoutCount());
        compatibilityDataSizeChanged(1);
    }

    public final void addData(@IntRange(from = 0) int position, @NonNull Collection<? extends T> data) {
        if (null == data) return;

        mDataSet.addAll(position, data);
        notifyItemRangeInserted(position + getHeaderLayoutCount(), data.size());
        compatibilityDataSizeChanged(data.size());
    }

    public final void addData(@NonNull Collection<? extends T> data) {
        if (null == data) return;

        mDataSet.addAll(data);
        notifyItemRangeInserted(mDataSet.size() - data.size() + getHeaderLayoutCount(), data.size());
        compatibilityDataSizeChanged(data.size());
    }

    public final void addData(@NonNull T data) {
        if (null == data) return;

        mDataSet.add(data);
        notifyItemInserted(mDataSet.size() + getHeaderLayoutCount());
        compatibilityDataSizeChanged(1);
    }

    public void remove(@IntRange(from = 0) int position) {
        mDataSet.remove(position);
        int internalPosition = position + getHeaderLayoutCount();
        notifyItemRemoved(internalPosition);
        compatibilityDataSizeChanged(0);
        notifyItemRangeChanged(internalPosition, mDataSet.size() - internalPosition);
    }

    public final void setData(@IntRange(from = 0) int index, @NonNull T data) {
        mDataSet.set(index, data);
        notifyItemChanged(index + getHeaderLayoutCount());
    }

    private void compatibilityDataSizeChanged(int size) {
        final int dataSize = mDataSet == null ? 0 : mDataSet.size();
        if (dataSize == size) {
            notifyDataSetChanged();
        }
    }

    private void openLoadMore(OnLoadMoreListener listener) {
        this.mOnLoadMoreListener = listener;
        mNextLoadEnable = true;
        mLoadMoreEnable = true;
        isLoadingMore = false;
    }

    public final void setOnLoadMoreListener(OnLoadMoreListener listener, RecyclerView recyclerView) {
        openLoadMore(listener);
        if (getRecyclerView() == null) {
            mRecyclerView = recyclerView;
        }
    }

    public final void disableLoadMoreIfNotFullPage(RecyclerView recyclerView) {
        setEnableLoadMore(false);
        if (recyclerView == null) return;
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager == null) return;
        if (manager instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) manager;
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if ((linearLayoutManager.findLastCompletelyVisibleItemPosition() + 1) != getItemCount()) {
                        setEnableLoadMore(true);
                    }
                }
            }, 50);
        } else if (manager instanceof StaggeredGridLayoutManager) {
            final StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) manager;
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    final int[] positions = new int[staggeredGridLayoutManager.getSpanCount()];
                    staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(positions);
                    int pos = getTheBiggestNumber(positions) + 1;
                    if (pos != getItemCount()) {
                        setEnableLoadMore(true);
                    }
                }
            }, 50);
        }
    }

    private int getTheBiggestNumber(int[] numbers) {
        int tmp = -1;
        if (numbers == null || numbers.length == 0) {
            return tmp;
        }
        for (int num : numbers) {
            if (num > tmp) {
                tmp = num;
            }
        }
        return tmp;
    }

    public final int getLoadMoreViewCount() {
        if (mOnLoadMoreListener == null || !mLoadMoreEnable) {
            return 0;
        }
        if (!mNextLoadEnable && mLoadMoreView.isLoadEndMoreGone()) {
            return 0;
        }
        if (mDataSet.size() == 0) {
            return 0;
        }
        return 1;
    }

    /**
     * 设置自定义加载更多视图
     *
     * @param view
     */
    public final void setLoadMoreView(LoadMoreView view) {
        this.mLoadMoreView = view;
    }

    public final int getLoadMoreViewPosition() {
        return getHeaderLayoutCount() + mDataSet.size() + getFooterLayoutCount();
    }

    public final void loadMoreEnd() {
        loadMoreEnd(false);
    }

    /**
     * 没有更多数据加载了
     *
     * @param gone
     */
    public final void loadMoreEnd(boolean gone) {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        isLoadingMore = false;
        mNextLoadEnable = false;
        mLoadMoreView.setLoadMoreEndGone(gone);
        if (gone) {
            notifyItemRemoved(getLoadMoreViewPosition());
        } else {
            mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_END);
            notifyItemChanged(getLoadMoreViewPosition());
        }
    }

    public final void loadMoreComplete() {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        isLoadingMore = false;
        mNextLoadEnable = true;
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
        notifyItemChanged(getLoadMoreViewPosition());
    }

    public final void loadMoreFailed() {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        isLoadingMore = false;
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_FAIL);
        notifyItemChanged(getLoadMoreViewPosition());
    }

    public final void setEnableLoadMore(boolean enable) {
        int oldLoadMoreCount = getLoadMoreViewCount();
        mLoadMoreEnable = enable;
        int newLoadMoreCount = getLoadMoreViewCount();

        if (oldLoadMoreCount == 1) {
            if (newLoadMoreCount == 0) {
                notifyItemRemoved(getLoadMoreViewPosition());
            }
        } else {
            if (newLoadMoreCount == 1) {
                mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
                notifyItemInserted(getLoadMoreViewPosition());
            }
        }
    }

    public final boolean isLoadMoreEnable() {
        return mLoadMoreEnable;
    }

    public final int getHeaderLayoutCount() {
        if (mHeaderLayout == null || mHeaderLayout.getChildCount() == 0) {
            return 0;
        }
        return 1;
    }

    public final int getFooterLayoutCount() {
        if (mFooterLayout == null || mFooterLayout.getChildCount() == 0) {
            return 0;
        }
        return 1;
    }

    @Nullable
    public final T getItem(@IntRange(from = 0) int position) {
        if (position < mDataSet.size()) {
            return mDataSet.get(position);
        } else {
            return null;
        }
    }

    @Override
    public final VH onCreateViewHolder(ViewGroup parent, int viewType) {
        VH holder = null;
        switch (viewType) {
            case TYPE_LOADMORE_VIEW:
                holder = getLoadMoreView(parent);
                break;
            case TYPE_HEADER_VIEW:
                holder = onCreateBaseViewHolder(mHeaderLayout);
                break;
            case TYPE_FOOTER_VIEW:
                holder = onCreateBaseViewHolder(mFooterLayout);
                break;
            default:
                View convertView = LayoutInflater.from(context).inflate(getItemLayoutResID(viewType), parent, false);
                holder = create(viewType, convertView, parent);
                bindClickListener(holder);
        }
        holder.setAdapter(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        //Do not move position, need to change before LoadMoreView binding
        autoLoadMore(position);
        int viewType = holder.getItemViewType();

        switch (viewType) {
            case 0:
                convert(holder, getItem(position - getHeaderLayoutCount()));
                break;
            case TYPE_LOADMORE_VIEW:
                mLoadMoreView.convert(holder);
                break;
            case TYPE_HEADER_VIEW:
                break;
            case TYPE_FOOTER_VIEW:
                break;
            default:
                convert(holder, getItem(position - getHeaderLayoutCount()));
                break;
        }
    }

    public final int getDataItemCount() {
        return mDataSet.size();
    }

    @Override
    public final int getItemCount() {
        return getHeaderLayoutCount() + mDataSet.size() + getFooterLayoutCount() + getLoadMoreViewCount();
    }

    @Override
    public final int getItemViewType(int position) {
        int numberOfHeaders = getHeaderLayoutCount();
        if (position < numberOfHeaders) {
            return TYPE_HEADER_VIEW;
        } else {
            int realPosition = position - numberOfHeaders;
            int adapterCount = mDataSet.size();
            if (realPosition < adapterCount) {
                return getContentItemType(realPosition);
            } else {
                realPosition = realPosition - adapterCount;
                int numberOfFooters = getFooterLayoutCount();
                if (realPosition < numberOfFooters) {
                    return TYPE_FOOTER_VIEW;
                } else {
                    return TYPE_LOADMORE_VIEW;
                }
            }
        }
    }

    @Override
    public final long getItemId(int position) {
        return position;
    }

    protected int getContentItemType(int position) {
        return super.getItemViewType(position);
    }

    private VH getLoadMoreView(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(mLoadMoreView.getLayoutResID(), parent, false);
        VH holder = onCreateBaseViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoadMoreView.getLoadMoreStatus() == LoadMoreView.STATUS_FAIL) {
                    notifyToLoadMore();
                }
                if (mEnableLoadMoreEndClick && mLoadMoreView.getLoadMoreStatus() == LoadMoreView.STATUS_END) {
                    notifyToLoadMore();
                }
            }
        });
        return holder;
    }

    public final void setPreLoadNumber(int preLoadNumber) {
        if (preLoadNumber > 1) {
            mPreLoadNumber = preLoadNumber;
        }
    }

    private void autoLoadMore(int position) {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        if (position < getItemCount() - mPreLoadNumber) {
            return;
        }
        if (mLoadMoreView.getLoadMoreStatus() != LoadMoreView.STATUS_DEFAULT) {
            return;
        }
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_LOADING);
        if (!isLoadingMore) {
            isLoadingMore = true;
            if (getRecyclerView() != null) {
                getRecyclerView().post(new Runnable() {
                    @Override
                    public void run() {
                        mOnLoadMoreListener.loadMore();
                    }
                });
            } else {
                mOnLoadMoreListener.loadMore();
            }
        }
    }

    public final void notifyToLoadMore() {
        if (mLoadMoreView.getLoadMoreStatus() == LoadMoreView.STATUS_LOADING) {
            return;
        }
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
        notifyItemChanged(getLoadMoreViewPosition());
    }

    public final void enableLoadMoreEndClick(boolean enable) {
        mEnableLoadMoreEndClick = enable;
    }

    @Override
    public void onViewAttachedToWindow(VH holder) {
        super.onViewAttachedToWindow(holder);
        int viewType = holder.getItemViewType();
        if (TYPE_HEADER_VIEW == viewType || TYPE_LOADMORE_VIEW == viewType || TYPE_FOOTER_VIEW == viewType) {
            setFullSpan(holder);
        } else {
            addAnimation(holder);
        }
    }

    protected final void setFullSpan(RecyclerView.ViewHolder holder) {
        if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder
                    .itemView.getLayoutParams();
            params.setFullSpan(true);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mRecyclerView = recyclerView;
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = getItemViewType(position);
                    if (TYPE_HEADER_VIEW == viewType && isHeaderViewAsFlow()) {
                        return 1;
                    }
                    if (TYPE_FOOTER_VIEW == viewType && isFooterViewAsFlow()) {
                        return 1;
                    }
                    if (mSpanSizeLookup == null) {
                        return isFixedViewType(viewType) ? gridManager.getSpanCount() : 1;
                    } else {
                        return (isFixedViewType(viewType)) ? gridManager.getSpanCount() : mSpanSizeLookup.getSpanSize(gridManager,
                                position - getHeaderLayoutCount());
                    }
                }
            });
        }
    }

    protected boolean isFixedViewType(int viewType) {
        return TYPE_HEADER_VIEW == viewType || TYPE_LOADMORE_VIEW == viewType || TYPE_FOOTER_VIEW == viewType;
    }

    public final void setHeaderViewAsFlow(boolean asFlow) {
        this.isHeaderViewAsFlow = asFlow;
    }

    public final boolean isHeaderViewAsFlow() {
        return isHeaderViewAsFlow;
    }

    public final void setFooterViewAsFlow(boolean asFlow) {
        this.isFooterViewAsFlow = asFlow;
    }

    public final boolean isFooterViewAsFlow() {
        return isFooterViewAsFlow;
    }

    public final void setSpanSizeLookup(SpanSizeLookup spanSizeLookup) {
        this.mSpanSizeLookup = spanSizeLookup;
    }

    private void bindClickListener(final BaseViewHolder baseViewHolder) {
        if (baseViewHolder == null) {
            return;
        }
        final View view = baseViewHolder.itemView;
        if (view == null) {
            return;
        }
        if (mOnItemClickListener != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(BaseAdapter.this, v, baseViewHolder.getLayoutPosition() - getHeaderLayoutCount());
                }
            });
        }
        if (mOnItemLongClickListener != null) {
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mOnItemLongClickListener.onItemLongClick(BaseAdapter.this, v, baseViewHolder.getLayoutPosition() - getHeaderLayoutCount());
                }
            });
        }
    }

    private void addAnimation(RecyclerView.ViewHolder holder) {
        if (mOpenAnimationEnable) {
            if (!mFirstOnlyEnable || holder.getLayoutPosition() > mLastPosition) {
                BaseAnimation animation = null;
                if (mCustomAnimation != null) {
                    animation = mCustomAnimation;
                } else {
                    animation = mSelectAnimation;
                }
                for (Animator anim : animation.getAnimators(holder.itemView)) {
                    startAnim(anim, holder.getLayoutPosition());
                }
                mLastPosition = holder.getLayoutPosition();
            }
        }
    }

    protected final void startAnim(Animator anim, int index) {
        anim.setDuration(mDuration).start();
        anim.setInterpolator(mInterpolator);
    }

    public void openLoadAnimation(@AnimationType int animationType) {
        this.mOpenAnimationEnable = true;
        mCustomAnimation = null;
        switch (animationType) {
            case ALPHAIN:
                mSelectAnimation = new AlphaInAnimation();
                break;
            case SCALEIN:
                mSelectAnimation = new ScaleInAnimation();
                break;
            case SLIDEIN_BOTTOM:
                mSelectAnimation = new SlideInBottomAnimation();
                break;
            case SLIDEIN_LEFT:
                mSelectAnimation = new SlideInLeftAnimation();
                break;
            case SLIDEIN_RIGHT:
                mSelectAnimation = new SlideInRightAnimation();
                break;
            default:
                break;
        }
    }

    public final void openLoadAnimation(BaseAnimation animation) {
        this.mOpenAnimationEnable = true;
        this.mCustomAnimation = animation;
    }

    public final void openLoadAnimation() {
        this.mOpenAnimationEnable = true;
    }

    public final void isFirstOnly(boolean firstOnly) {
        this.mFirstOnlyEnable = firstOnly;
    }

    protected VH create(int viewType, View convertView, ViewGroup parent) {
        return onCreateBaseViewHolder(convertView);
    }

    protected final VH onCreateBaseViewHolder(View parent) {
        Class temp = getClass();
        Class clazz = null;
        while (clazz == null && null != temp) {
            clazz = getInstancedGenericKClass(temp);
            temp = temp.getSuperclass();
        }
        VH holder;
        // 泛型擦除会导致clazz为null
        if (clazz == null) {
            holder = (VH) new BaseViewHolder(parent);
        } else {
            holder = createGenericKInstance(clazz, parent);
        }
        return holder != null ? holder : (VH) new BaseViewHolder(parent);
    }

    @SuppressWarnings("unchecked")
    private VH createGenericKInstance(Class clazz, View view) {
        try {
            Constructor constructor;
            // inner and unstatic class
            if (clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers())) {
                constructor = clazz.getDeclaredConstructor(getClass(), View.class);
                constructor.setAccessible(true);
                return (VH) constructor.newInstance(this, view);
            } else {
                constructor = clazz.getDeclaredConstructor(View.class);
                constructor.setAccessible(true);
                return (VH) constructor.newInstance(view);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Class getInstancedGenericKClass(Class clazz) {
        Type type = clazz.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            for (Type temp : types) {
                if (temp instanceof Class) {
                    Class tempClass = (Class) temp;
                    if (BaseViewHolder.class.isAssignableFrom(tempClass)) {
                        return tempClass;
                    }
                } else if (temp instanceof ParameterizedType) {
                    Type rawType = ((ParameterizedType) temp).getRawType();
                    if (rawType instanceof Class && BaseViewHolder.class.isAssignableFrom((Class<?>) rawType)) {
                        return (Class<?>) rawType;
                    }
                }
            }
        }
        return null;
    }

    @Nullable
    public final View getViewByPosition(RecyclerView recyclerView, int position, @IdRes int id) {
        if (null == recyclerView) return null;

        BaseViewHolder holder = (BaseViewHolder) recyclerView.findViewHolderForLayoutPosition(position);
        if (null == holder) return null;

        return holder.findViewById(id);
    }

    public final LinearLayout getHeaderLayout() {
        return mHeaderLayout;
    }

    public final LinearLayout getFooterLayout() {
        return mFooterLayout;
    }

    public final int addHeaderView(View header) {
        return addHeaderView(header, -1);
    }

    public final int addHeaderView(View header, int index) {
        return addHeaderView(header, index, LinearLayout.VERTICAL);
    }

    public final int addHeaderView(View header, int index, int orientation) {
        if (mHeaderLayout == null) {
            mHeaderLayout = new LinearLayout(header.getContext());
            if (orientation == LinearLayout.VERTICAL) {
                mHeaderLayout.setOrientation(LinearLayout.VERTICAL);
                mHeaderLayout.setLayoutParams(new RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            } else {
                mHeaderLayout.setOrientation(LinearLayout.HORIZONTAL);
                mHeaderLayout.setLayoutParams(new RecyclerView.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
            }
        }
        final int childCount = mHeaderLayout.getChildCount();
        if (index < 0 || index > childCount) {
            index = childCount;
        }
        mHeaderLayout.addView(header, index);
        if (mHeaderLayout.getChildCount() == 1) {
            notifyItemInserted(0);
        }
        return index;
    }

    public final int setHeaderView(View header) {
        return setHeaderView(header, 0, LinearLayout.VERTICAL);
    }

    public final int setHeaderView(View header, int index) {
        return setHeaderView(header, index, LinearLayout.VERTICAL);
    }

    public final int setHeaderView(View header, int index, int orientation) {
        if (mHeaderLayout == null || mHeaderLayout.getChildCount() <= index) {
            return addHeaderView(header, index, orientation);
        } else {
            mHeaderLayout.removeViewAt(index);
            mHeaderLayout.addView(header, index);
            return index;
        }
    }

    public final int addFooterView(View footer) {
        return addFooterView(footer, -1, LinearLayout.VERTICAL);
    }

    public final int addFooterView(View footer, int index) {
        return addFooterView(footer, index, LinearLayout.VERTICAL);
    }

    public final int addFooterView(View footer, int index, int orientation) {
        if (mFooterLayout == null) {
            mFooterLayout = new LinearLayout(footer.getContext());
            if (orientation == LinearLayout.VERTICAL) {
                mFooterLayout.setOrientation(LinearLayout.VERTICAL);
                mFooterLayout.setLayoutParams(new RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            } else {
                mFooterLayout.setOrientation(LinearLayout.HORIZONTAL);
                mFooterLayout.setLayoutParams(new RecyclerView.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
            }
        }
        final int childCount = mFooterLayout.getChildCount();
        if (index < 0 || index > childCount) {
            index = childCount;
        }
        mFooterLayout.addView(footer, index);
        if (mFooterLayout.getChildCount() == 1) {
            int position = getFooterViewPosition();
            if (position != -1) {
                notifyItemInserted(position);
            }
        }
        return index;
    }

    public final int setFooterView(View header) {
        return setFooterView(header, 0, LinearLayout.VERTICAL);
    }

    public final int setFooterView(View header, int index) {
        return setFooterView(header, index, LinearLayout.VERTICAL);
    }

    public final int setFooterView(View header, int index, int orientation) {
        if (mFooterLayout == null || mFooterLayout.getChildCount() <= index) {
            return addFooterView(header, index, orientation);
        } else {
            mFooterLayout.removeViewAt(index);
            mFooterLayout.addView(header, index);
            return index;
        }
    }

    public final void removeHeaderView(View header) {
        if (getHeaderLayoutCount() == 0) return;

        mHeaderLayout.removeView(header);
        if (mHeaderLayout.getChildCount() == 0) {
            notifyItemRemoved(0);
        }
    }

    public final void removeFooterView(View footer) {
        if (getFooterLayoutCount() == 0) return;

        mFooterLayout.removeView(footer);
        if (mFooterLayout.getChildCount() == 0) {
            int position = getFooterViewPosition();
            if (position != -1) {
                notifyItemRemoved(position);
            }
        }
    }

    public final void removeAllHeaderView() {
        if (getHeaderLayoutCount() == 0) return;

        mHeaderLayout.removeAllViews();
        notifyItemRemoved(0);
    }

    public final void removeAllFooterView() {
        if (getFooterLayoutCount() == 0) return;

        mFooterLayout.removeAllViews();
        int position = getFooterViewPosition();
        if (position != -1) {
            notifyItemRemoved(position);
        }
    }

    private final int getFooterViewPosition() {
        //Return to footer view notify position
        return getHeaderLayoutCount() + mDataSet.size();
    }

    @SuppressWarnings("unchecked")
    private int recursiveExpand(int position, @NonNull List list) {
        int count = list.size();
        int pos = position + list.size() - 1;
        for (int i = list.size() - 1; i >= 0; i--, pos--) {
            if (list.get(i) instanceof IExpandable) {
                IExpandable item = (IExpandable) list.get(i);
                if (item.isExpanded() && hasSubItems(item)) {
                    List subList = item.getSubItems();
                    mDataSet.addAll(pos + 1, subList);
                    int subItemCount = recursiveExpand(pos + 1, subList);
                    count += subItemCount;
                }
            }
        }
        return count;
    }

    @SuppressWarnings("unchecked")
    public final int expand(@IntRange(from = 0) int position, boolean animate, boolean shouldNotify) {
        position -= getHeaderLayoutCount();

        IExpandable expandable = getExpandableItem(position);
        if (expandable == null) {
            return 0;
        }
        if (!hasSubItems(expandable)) {
            expandable.setExpanded(true);
            notifyItemChanged(position);
            return 0;
        }
        int subItemCount = 0;
        if (!expandable.isExpanded()) {
            List list = expandable.getSubItems();
            mDataSet.addAll(position + 1, list);
            subItemCount += recursiveExpand(position + 1, list);

            expandable.setExpanded(true);
        }
        int parentPos = position + getHeaderLayoutCount();
        if (shouldNotify) {
            if (animate) {
                notifyItemChanged(parentPos);
                notifyItemRangeInserted(parentPos + 1, subItemCount);
            } else {
                notifyDataSetChanged();
            }
        }
        return subItemCount;
    }

    public final int expand(@IntRange(from = 0) int position, boolean animate) {
        return expand(position, animate, true);
    }

    public final int expand(@IntRange(from = 0) int position) {
        return expand(position, true, true);
    }

    public final int expandAll(int position, boolean animate, boolean notify) {
        position -= getHeaderLayoutCount();

        T endItem = null;
        if (position + 1 < this.mDataSet.size()) {
            endItem = getItem(position + 1);
        }

        IExpandable expandable = getExpandableItem(position);
        if (expandable == null) {
            return 0;
        }

        if (!hasSubItems(expandable)) {
            expandable.setExpanded(true);
            notifyItemChanged(position);
            return 0;
        }

        int count = expand(position + getHeaderLayoutCount(), false, false);
        for (int i = position + 1; i < this.mDataSet.size(); i++) {
            T item = getItem(i);

            if (item == endItem) {
                break;
            }
            if (isExpandable(item)) {
                count += expand(i + getHeaderLayoutCount(), false, false);
            }
        }

        if (notify) {
            if (animate) {
                notifyItemRangeInserted(position + getHeaderLayoutCount() + 1, count);
            } else {
                notifyDataSetChanged();
            }
        }
        return count;
    }

    public final int expandAll(int position, boolean init) {
        return expandAll(position, true, !init);
    }

    public final void expandAll() {
        for (int i = mDataSet.size() - 1 + getHeaderLayoutCount(); i >= getHeaderLayoutCount(); i--) {
            expandAll(i, false, false);
        }
    }

    @SuppressWarnings("unchecked")
    private int recursiveCollapse(@IntRange(from = 0) int position) {
        T item = getItem(position);
        if (!isExpandable(item)) {
            return 0;
        }
        IExpandable expandable = (IExpandable) item;
        int subItemCount = 0;
        if (expandable.isExpanded()) {
            List<T> subItems = expandable.getSubItems();
            if (null == subItems) return 0;

            for (int i = subItems.size() - 1; i >= 0; i--) {
                T subItem = subItems.get(i);
                int pos = getItemPosition(subItem);
                if (pos < 0) {
                    continue;
                }
                if (subItem instanceof IExpandable) {
                    subItemCount += recursiveCollapse(pos);
                }
                mDataSet.remove(pos);
                subItemCount++;
            }
        }
        return subItemCount;
    }

    public final int collapse(@IntRange(from = 0) int position, boolean animate, boolean notify) {
        position -= getHeaderLayoutCount();

        IExpandable expandable = getExpandableItem(position);
        if (expandable == null) {
            return 0;
        }
        int subItemCount = recursiveCollapse(position);
        expandable.setExpanded(false);
        int parentPos = position + getHeaderLayoutCount();
        if (notify) {
            if (animate) {
                notifyItemChanged(parentPos);
                notifyItemRangeRemoved(parentPos + 1, subItemCount);
            } else {
                notifyDataSetChanged();
            }
        }
        return subItemCount;
    }

    public final int collapse(@IntRange(from = 0) int position) {
        return collapse(position, true, true);
    }

    public final int collapse(@IntRange(from = 0) int position, boolean animate) {
        return collapse(position, animate, true);
    }

    private int getItemPosition(T item) {
        return item != null && mDataSet != null && !mDataSet.isEmpty() ? mDataSet.indexOf(item) : -1;
    }

    private boolean hasSubItems(IExpandable item) {
        if (item == null) {
            return false;
        }
        List list = item.getSubItems();
        return list != null && list.size() > 0;
    }

    public final boolean isExpandable(T item) {
        return item != null && item instanceof IExpandable;
    }

    private IExpandable getExpandableItem(int position) {
        T item = getItem(position);
        if (isExpandable(item)) {
            return (IExpandable) item;
        } else {
            return null;
        }
    }

    public final int getParentPosition(@NonNull T item) {
        int position = getItemPosition(item);
        if (position == -1) {
            return -1;
        }

        // if the item is IExpandable, return a closest IExpandable item position whose level smaller than this.
        // if it is not, return the closest IExpandable item position whose level is not negative
        int level;
        if (item instanceof IExpandable) {
            level = ((IExpandable) item).getLevel();
        } else {
            level = Integer.MAX_VALUE;
        }
        if (level == 0) {
            return position;
        } else if (level == -1) {
            return -1;
        }

        for (int i = position; i >= 0; i--) {
            T temp = mDataSet.get(i);
            if (temp instanceof IExpandable) {
                IExpandable expandable = (IExpandable) temp;
                if (expandable.getLevel() >= 0 && expandable.getLevel() < level) {
                    return i;
                }
            }
        }
        return -1;
    }

    public final void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public final void setOnChildClickListener(OnChildClickListener listener) {
        mOnChildClickListener = listener;
    }

    public final void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }

    public final void setOnChildLongClickListener(OnChildLongClickListener listener) {
        mOnChildLongClickListener = listener;
    }

    public final OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public final OnItemLongClickListener getOnItemLongClickListener() {
        return mOnItemLongClickListener;
    }

    public final OnChildClickListener getOnChildClickListener() {
        return mOnChildClickListener;
    }

    public final OnChildLongClickListener getOnChildLongClickListener() {
        return mOnChildLongClickListener;
    }

    protected abstract int getItemLayoutResID(final int viewType);

    protected abstract void convert(final VH holder, final T item);

    public interface SpanSizeLookup {
        int getSpanSize(GridLayoutManager gridLayoutManager, int position);
    }

    public interface OnLoadMoreListener {
        void loadMore();
    }

    public interface OnChildClickListener {
        void onChildClick(BaseAdapter adapter, View view, int position);
    }

    public interface OnChildLongClickListener {
        boolean onChildLongClick(BaseAdapter adapter, View view, int position);
    }

    public interface OnItemClickListener {
        void onItemClick(BaseAdapter adapter, View view, int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(BaseAdapter adapter, View view, int position);
    }
}
