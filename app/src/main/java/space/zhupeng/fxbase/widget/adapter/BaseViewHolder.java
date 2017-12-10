package space.zhupeng.fxbase.widget.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class BaseViewHolder extends RecyclerView.ViewHolder {

    private final SparseArray<View> mItemViews;

    public Set<Integer> getNestViews() {
        return mNestViews;
    }

    private final HashSet<Integer> mNestViews;
    private final LinkedHashSet<Integer> mChildClickViewIds;
    private final LinkedHashSet<Integer> mChildLongClickViewIds;

    private BaseAdapter mAdapter;

    public BaseViewHolder(final View itemView) {
        super(itemView);
        this.mItemViews = new SparseArray<>();
        this.mChildClickViewIds = new LinkedHashSet<>();
        this.mChildLongClickViewIds = new LinkedHashSet<>();
        this.mNestViews = new HashSet<>();
    }

    private int getClickPosition() {
        if (getLayoutPosition() >= mAdapter.getHeaderLayoutCount()) {
            return getLayoutPosition() - mAdapter.getHeaderLayoutCount();
        }
        return 0;
    }

    public HashSet<Integer> getChildLongClickViewIds() {
        return mChildLongClickViewIds;
    }

    public HashSet<Integer> getChildClickViewIds() {
        return mChildClickViewIds;
    }

    public BaseViewHolder setText(@IdRes int id, CharSequence text) {
        TextView view = findViewById(id);
        view.setText(text);
        return this;
    }

    public BaseViewHolder setText(@IdRes int id, @StringRes int resId) {
        TextView view = findViewById(id);
        view.setText(resId);
        return this;
    }

    public BaseViewHolder setImageResource(@IdRes int id, @DrawableRes int resId) {
        ImageView view = findViewById(id);
        view.setImageResource(resId);
        return this;
    }

    public BaseViewHolder setImageDrawable(@IdRes int id, Drawable drawable) {
        ImageView view = findViewById(id);
        view.setImageDrawable(drawable);
        return this;
    }

    public BaseViewHolder setBackgroundRes(@IdRes int id, @DrawableRes int resId) {
        View view = findViewById(id);
        view.setBackgroundResource(resId);
        return this;
    }

    public BaseViewHolder setTextColor(@IdRes int id, @ColorInt int color) {
        TextView view = findViewById(id);
        view.setTextColor(color);
        return this;
    }

    public BaseViewHolder setImageBitmap(@IdRes int id, Bitmap bitmap) {
        ImageView view = findViewById(id);
        view.setImageBitmap(bitmap);
        return this;
    }

    public BaseViewHolder setVisibility(@IdRes int id, int visibility) {
        View view = findViewById(id);
        view.setVisibility(visibility);
        return this;
    }

    public BaseViewHolder setTag(@IdRes int id, Object tag) {
        View view = findViewById(id);
        view.setTag(tag);
        return this;
    }

    public BaseViewHolder setTag(@IdRes int id, int key, Object tag) {
        View view = findViewById(id);
        view.setTag(key, tag);
        return this;
    }

    protected final BaseViewHolder setAdapter(BaseAdapter adapter) {
        this.mAdapter = adapter;
        return this;
    }

    @SuppressWarnings("unchecked")
    public BaseViewHolder addOnClickListener(@IdRes final int id) {
        mChildClickViewIds.add(id);
        final View view = findViewById(id);
        if (view != null) {
            if (!view.isClickable()) {
                view.setClickable(true);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAdapter.getOnChildClickListener() != null) {
                        mAdapter.getOnChildClickListener().onChildClick(mAdapter, v, getClickPosition());
                    }
                }
            });
        }

        return this;
    }

    public BaseViewHolder setNestView(@IdRes int id) {
        addOnClickListener(id);
        addOnLongClickListener(id);
        mNestViews.add(id);
        return this;
    }

    @SuppressWarnings("unchecked")
    public BaseViewHolder addOnLongClickListener(@IdRes final int id) {
        mChildLongClickViewIds.add(id);
        final View view = findViewById(id);
        if (view != null) {
            if (!view.isLongClickable()) {
                view.setLongClickable(true);
            }
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mAdapter.getOnChildLongClickListener() != null &&
                            mAdapter.getOnChildLongClickListener().onChildLongClick(mAdapter, v, getClickPosition());
                }
            });
        }
        return this;
    }

    public BaseViewHolder setOnItemLongClickListener(@IdRes int id, AdapterView.OnItemLongClickListener listener) {
        AdapterView view = findViewById(id);
        view.setOnItemLongClickListener(listener);
        return this;
    }

    public BaseViewHolder setOnItemSelectedClickListener(@IdRes int id, AdapterView.OnItemSelectedListener listener) {
        AdapterView view = findViewById(id);
        view.setOnItemSelectedListener(listener);
        return this;
    }

    public BaseViewHolder setOnCheckedChangeListener(@IdRes int id, CompoundButton.OnCheckedChangeListener listener) {
        CompoundButton view = findViewById(id);
        view.setOnCheckedChangeListener(listener);
        return this;
    }

    @SuppressWarnings("unchecked")
    public final <T extends View> T findViewById(@IdRes int id) {
        View view = mItemViews.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            mItemViews.put(id, view);
        }
        return (T) view;
    }
}
