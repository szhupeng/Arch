package space.zhupeng.arch.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import space.zhupeng.arch.R;

/**
 * 自定义标题栏
 *
 * @author zhupeng
 * @date 2018/2/6
 */

public class CustomTitleBar extends ConstraintLayout implements View.OnClickListener {

    public interface OnTitleBarActionListener {
        void onLeftClick();

        void onCenterClick();

        void onRightClick();

        void onRightIconClick();
    }

    protected AppCompatTextView tvLeft;
    protected AppCompatTextView tvTitle;
    protected AppCompatTextView tvSubtitle;
    protected AppCompatTextView tvRight;
    protected AppCompatImageView ivRight;

    protected OnTitleBarActionListener mOnActionListener;

    public CustomTitleBar(Context context) {
        this(context, null, 0);
    }

    public CustomTitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr);
    }

    @CallSuper
    protected void init(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.layout_toolbar_content, this, true);

        tvLeft = findViewById(R.id.tv_left);
        tvTitle = findViewById(R.id.tv_title);
        tvSubtitle = findViewById(R.id.tv_subtitle);
        tvRight = findViewById(R.id.tv_right);
        ivRight = findViewById(R.id.iv_right);

        tvLeft.setOnClickListener(this);
        tvTitle.setOnClickListener(this);
        tvSubtitle.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        ivRight.setOnClickListener(this);
    }

    public void setOnTitleBarActionListener(OnTitleBarActionListener listener) {
        this.mOnActionListener = listener;
    }

    public CustomTitleBar setLeftIcon(@Nullable Drawable drawable) {
        showLeft();
        tvLeft.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        tvLeft.setText(null);
        return this;
    }

    public CustomTitleBar setLeftIcon(@DrawableRes int resId) {
        showLeft();
        Drawable drawable = ContextCompat.getDrawable(getContext(), resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvLeft.setCompoundDrawables(drawable, null, null, null);
        tvLeft.setText(null);
        return this;
    }

    public CustomTitleBar setLeftText(@StringRes int resId) {
        return setLeftText(getContext().getString(resId));
    }

    public CustomTitleBar setLeftText(@Nullable CharSequence text) {
        showLeft();
        tvLeft.setText(text);
        return this;
    }

    public CustomTitleBar setLeftTextWithIcon(@Nullable CharSequence text, @Nullable Drawable drawable) {
        showLeft();
        tvLeft.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        tvLeft.setText(text);
        return this;
    }

    public CustomTitleBar setLeftTextWithIcon(@Nullable CharSequence text, @DrawableRes int resId) {
        showLeft();
        Drawable drawable = ContextCompat.getDrawable(getContext(), resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvLeft.setCompoundDrawables(drawable, null, null, null);
        tvLeft.setText(text);
        return this;
    }

    public CustomTitleBar setCenterTitle(@NonNull CharSequence text) {
        showCenterTitle();
        tvTitle.setText(text);
        return this;
    }

    public CustomTitleBar setCenterTitle(@StringRes int resId) {
        return setCenterTitle(getContext().getString(resId));
    }

    public CustomTitleBar setCenterSubtitle(@NonNull CharSequence text) {
        showCenterSubtitle();
        tvSubtitle.setText(text);
        return this;
    }

    public CustomTitleBar setCenterSubtitle(@StringRes int resId) {
        return setCenterSubtitle(getContext().getString(resId));
    }

    public CustomTitleBar setRightText(@NonNull CharSequence text) {
        showRight();
        tvRight.setText(text);
        return this;
    }

    public CustomTitleBar setRightText(@StringRes int resId) {
        return setRightText(getContext().getString(resId));
    }

    public CustomTitleBar setRightIcon(@DrawableRes int resId) {
        showRightIcon();
        ivRight.setImageResource(resId);
        return this;
    }

    public CustomTitleBar showLeft() {
        setVisibility(tvLeft, View.VISIBLE);
        return this;
    }

    public CustomTitleBar hideLeft() {
        setVisibility(tvLeft, View.GONE);
        return this;
    }

    public CustomTitleBar showCenterTitle() {
        setVisibility(tvTitle, View.VISIBLE);
        return this;
    }

    public CustomTitleBar hideCenterTitle() {
        setVisibility(tvTitle, View.GONE);
        return this;
    }

    public CustomTitleBar showCenterSubtitle() {
        setVisibility(tvSubtitle, View.VISIBLE);
        return this;
    }

    public CustomTitleBar hideCenterSubtitle() {
        setVisibility(tvSubtitle, View.GONE);
        return this;
    }

    public CustomTitleBar showRight() {
        setVisibility(tvRight, View.VISIBLE);
        return this;
    }

    public CustomTitleBar hideRight() {
        setVisibility(tvRight, View.GONE);
        return this;
    }

    public CustomTitleBar showRightIcon() {
        setVisibility(ivRight, View.VISIBLE);
        return this;
    }

    public CustomTitleBar hideRightIcon() {
        setVisibility(ivRight, View.GONE);
        return this;
    }

    protected void setVisibility(View view, final int visibility) {
        if (visibility != View.VISIBLE && visibility != View.INVISIBLE && visibility != View.GONE) {
            throw new IllegalArgumentException("The value of visibility is invalid");
        }

        if (view.getVisibility() != visibility) {
            view.setVisibility(visibility);
        }
    }

    public void onClick(View v) {
        if (null == mOnActionListener) return;

        final int id = v.getId();
        if (tvLeft.getId() == id) {
            mOnActionListener.onLeftClick();
        } else if (tvTitle.getId() == id || tvSubtitle.getId() == id) {
            mOnActionListener.onCenterClick();
        } else if (tvRight.getId() == id) {
            mOnActionListener.onRightClick();
        } else if (ivRight.getId() == id) {
            mOnActionListener.onRightIconClick();
        }
    }
}
