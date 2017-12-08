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
import android.widget.ImageView;
import android.widget.TextView;

public class BaseViewHolder extends RecyclerView.ViewHolder {

    private final SparseArray<View> mItemViews;
    private BaseAdapter mAdapter;

    public BaseViewHolder(final View itemView) {
        super(itemView);
        this.mItemViews = new SparseArray<>();
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
    public final <T extends View> T findViewById(@IdRes int id) {
        View view = mItemViews.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            mItemViews.put(id, view);
        }
        return (T) view;
    }
}
