package space.zhupeng.arch.ui;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import space.zhupeng.arch.R;

/**
 * @author zhupeng
 * @date 2017/12/9
 */

public class ToolbarDelegateImpl implements ToolbarDelegate {

    private TextView tvLeft;
    private LinearLayout llCenter;
    private TextView tvTitle;
    private TextView tvSubtitle;
    private TextView tvRight;
    private ImageView ivRight;

    public ToolbarDelegateImpl(AppCompatActivity activity, Toolbar toolbar) {
        tvLeft = activity.findViewById(R.id.tv_left);
        llCenter = activity.findViewById(R.id.ll_center);
        tvTitle = activity.findViewById(R.id.tv_title);
        tvSubtitle = activity.findViewById(R.id.tv_subtitle);
        tvRight = activity.findViewById(R.id.tv_right);
        ivRight = activity.findViewById(R.id.iv_right);

        setToolbar(activity, toolbar);
    }

    public ToolbarDelegateImpl(@NonNull Object target, @NonNull View source, Toolbar toolbar) {
        tvLeft = source.findViewById(R.id.tv_left);
        llCenter = source.findViewById(R.id.ll_center);
        tvTitle = source.findViewById(R.id.tv_title);
        tvSubtitle = source.findViewById(R.id.tv_subtitle);
        tvRight = source.findViewById(R.id.tv_right);
        ivRight = source.findViewById(R.id.iv_right);

        if (target instanceof Fragment) {
            setToolbar((AppCompatActivity) ((Fragment) target).getActivity(), toolbar);
        }
    }

    private void setToolbar(AppCompatActivity activity, Toolbar toolbar) {
        if (toolbar != null) {
            activity.setSupportActionBar(toolbar);
        }
    }

    @Override
    public void bindClickEvent(View.OnClickListener listener) {
        if (tvLeft != null) {
            tvLeft.setOnClickListener(listener);
        }

        if (llCenter != null) {
            llCenter.setOnClickListener(listener);
        }

        if (tvRight != null) {
            tvRight.setOnClickListener(listener);
        }

        if (ivRight != null) {
            ivRight.setOnClickListener(listener);
        }
    }

    @Override
    public void setLeft(AppCompatActivity activity, @DrawableRes int resId) {
        if (null == tvLeft) return;

        showLeft();

        if (resId != 0) {
            Drawable drawable = ContextCompat.getDrawable(activity, resId);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvLeft.setCompoundDrawables(drawable, null, null, null);
        } else {
            tvLeft.setCompoundDrawables(null, null, null, null);
        }
    }

    @Override
    public void setLeft(CharSequence text) {
        if (null == tvLeft) return;

        showLeft();

        tvLeft.setText(text);
    }

    @Override
    public void setLeft(AppCompatActivity activity, @DrawableRes int resId, CharSequence text) {
        if (null == text) return;

        showLeft();

        if (resId != 0) {
            Drawable drawable = ContextCompat.getDrawable(activity, resId);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvLeft.setCompoundDrawables(drawable, null, null, null);
        } else {
            tvLeft.setCompoundDrawables(null, null, null, null);
        }

        tvLeft.setText(text);
    }

    @Override
    public void setCenterTitle(CharSequence title) {
        if (null == tvTitle) return;

        showCenter();

        tvTitle.setText(title);
    }

    @Override
    public void setCenterTitle(AppCompatActivity activity, @StringRes int resId) {
        setCenterTitle(activity.getString(resId));
    }

    @Override
    public void setCenterSubtitle(CharSequence subtitle) {
        if (null == tvSubtitle) return;

        showCenter();

        if (tvSubtitle.getVisibility() != View.VISIBLE) {
            tvSubtitle.setVisibility(View.VISIBLE);
        }

        tvSubtitle.setText(subtitle);
    }

    @Override
    public void setCenterSubtitle(CharSequence subtitle, float size) {
        if (null == tvSubtitle) return;

        setCenterSubtitle(subtitle);

        tvSubtitle.setTextSize(size);
    }

    @Override
    public void setCenterSubtitle(CharSequence subtitle, float size, int color) {
        if (null == tvSubtitle) return;

        setCenterSubtitle(subtitle);

        tvSubtitle.setTextSize(size);
        tvSubtitle.setTextColor(color);
    }

    @Override
    public void setRightText(CharSequence text) {
        if (null == tvRight) return;

        showRightText();

        tvRight.setText(text);
    }

    @Override
    public void setRightIcon(@DrawableRes int resId) {
        if (null == ivRight) return;

        showRightIcon();

        ivRight.setImageResource(resId);
    }

    @Override
    public void showLeft() {
        setVisibility(tvLeft, View.VISIBLE);
    }

    @Override
    public void hideLeft() {
        setVisibility(tvLeft, View.GONE);
    }

    @Override
    public void showCenter() {
        setVisibility(llCenter, View.VISIBLE);
    }

    @Override
    public void hideCenter() {
        setVisibility(llCenter, View.GONE);
    }

    @Override
    public void showRightText() {
        setVisibility(tvRight, View.VISIBLE);
    }

    @Override
    public void hideRightText() {
        setVisibility(tvRight, View.GONE);
    }

    @Override
    public void showRightIcon() {
        setVisibility(ivRight, View.VISIBLE);
    }

    @Override
    public void hideRightIcon() {
        setVisibility(ivRight, View.GONE);
    }

    protected void setVisibility(View view, int visibility) {
        if (null == view) return;

        if (view.getVisibility() != visibility) {
            view.setVisibility(visibility);
        }
    }

    public boolean isLeftClicked(final int id) {
        return tvLeft != null && tvLeft.getId() == id;
    }

    public boolean isCenterTitleClicked(final int id) {
        return llCenter != null && llCenter.getId() == id;
    }

    public boolean isRightTextClicked(final int id) {
        return tvRight != null && tvRight.getId() == id;
    }

    public boolean isRightIconClicked(final int id) {
        return ivRight != null && ivRight.getId() == id;
    }
}
