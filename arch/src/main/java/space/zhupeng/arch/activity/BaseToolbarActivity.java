package space.zhupeng.arch.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import java.lang.reflect.Method;

import space.zhupeng.arch.R;
import space.zhupeng.arch.mvp.model.Repository;
import space.zhupeng.arch.mvp.presenter.BasePresenter;
import space.zhupeng.arch.mvp.view.BaseView;
import space.zhupeng.arch.utils.ActionModeHelper;
import space.zhupeng.arch.widget.CustomTitleBar;

/**
 * 包含Toolbar的Activity基类
 *
 * @author zhupeng
 * @date 2017/1/14
 */

public abstract class BaseToolbarActivity<M extends Repository, V extends BaseView, P extends BasePresenter<M, V>> extends BaseActivity<M, V, P> implements CustomTitleBar.OnTitleBarActionListener {

    @Nullable
    protected Toolbar mToolbar;
    @Nullable
    protected CustomTitleBar mCustomTitleBar;

    protected ActionModeHelper mActionModeHelper;

    @CallSuper
    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        mToolbar = findById(R.id.toolbar);
        mCustomTitleBar = findById(R.id.title_bar);
        if (mCustomTitleBar != null) {
            mCustomTitleBar.setOnTitleBarActionListener(this);
        }
    }

    public void setLeftIcon(@Nullable Drawable drawable) {
        if (mCustomTitleBar != null) {
            mCustomTitleBar.setLeftIcon(drawable);
        }
    }

    public void setLeftIcon(@DrawableRes int resId) {
        if (mCustomTitleBar != null) {
            mCustomTitleBar.setLeftIcon(resId);
        }
    }

    public void setLeftText(@StringRes int resId) {
        if (mCustomTitleBar != null) {
            mCustomTitleBar.setLeftText(resId);
        }
    }

    public void setLeftText(@Nullable CharSequence text) {
        if (mCustomTitleBar != null) {
            mCustomTitleBar.setLeftText(text);
        }
    }

    public void setLeftTextWithIcon(@Nullable CharSequence text, @Nullable Drawable drawable) {
        if (mCustomTitleBar != null) {
            mCustomTitleBar.setLeftTextWithIcon(text, drawable);
        }
    }

    public void setLeftTextWithIcon(@Nullable CharSequence text, @DrawableRes int resId) {
        if (mCustomTitleBar != null) {
            mCustomTitleBar.setLeftTextWithIcon(text, resId);
        }
    }

    public void setCenterTitle(@NonNull CharSequence text) {
        if (mCustomTitleBar != null) {
            mCustomTitleBar.setCenterTitle(text);
        }
    }

    public void setCenterTitle(@StringRes int resId) {
        if (mCustomTitleBar != null) {
            mCustomTitleBar.setCenterTitle(resId);
        }
    }

    public void setCenterSubtitle(@NonNull CharSequence text) {
        if (mCustomTitleBar != null) {
            mCustomTitleBar.setCenterSubtitle(text);
        }
    }

    public void setCenterSubtitle(@StringRes int resId) {
        if (mCustomTitleBar != null) {
            mCustomTitleBar.setCenterSubtitle(resId);
        }
    }

    public void setRightText(@NonNull CharSequence text) {
        if (mCustomTitleBar != null) {
            mCustomTitleBar.setRightText(text);
        }
    }

    public void setRightText(@StringRes int resId) {
        if (mCustomTitleBar != null) {
            mCustomTitleBar.setRightText(resId);
        }
    }

    public void setRightIcon(@DrawableRes int resId) {
        if (mCustomTitleBar != null) {
            mCustomTitleBar.setRightIcon(resId);
        }
    }

    public void showLeft() {
        if (mCustomTitleBar != null) {
            mCustomTitleBar.showLeft();
        }
    }

    public void hideLeft() {
        if (mCustomTitleBar != null) {
            mCustomTitleBar.hideLeft();
        }
    }

    public void showCenterTitle() {
        if (mCustomTitleBar != null) {
            mCustomTitleBar.showCenterTitle();
        }
    }

    public void hideCenterTitle() {
        if (mCustomTitleBar != null) {
            mCustomTitleBar.hideCenterTitle();
        }
    }

    public void showCenterSubtitle() {
        if (mCustomTitleBar != null) {
            mCustomTitleBar.showCenterSubtitle();
        }
    }

    public void hideCenterSubtitle() {
        if (mCustomTitleBar != null) {
            mCustomTitleBar.hideCenterSubtitle();
        }
    }

    public void showRight() {
        if (mCustomTitleBar != null) {
            mCustomTitleBar.showRight();
        }
    }

    public void hideRight() {
        if (mCustomTitleBar != null) {
            mCustomTitleBar.hideRight();
        }
    }

    public void showRightIcon() {
        if (mCustomTitleBar != null) {
            mCustomTitleBar.showRightIcon();
        }
    }

    public void hideRightIcon() {
        if (mCustomTitleBar != null) {
            mCustomTitleBar.hideRightIcon();
        }
    }

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onCenterClick() {
    }

    @Override
    public void onRightClick() {
    }

    @Override
    public void onRightIconClick() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getContextMenuResId() != 0) {
            getMenuInflater().inflate(getContextMenuResId(), menu);
            return true;
        } else {
            return super.onCreateOptionsMenu(menu);
        }
    }

    /**
     * 通过反射，设置menu显示自定义icon
     *
     * @param featureId
     * @param menu
     * @return
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (getContextMenuResId() != 0 && menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    protected int getContextMenuResId() {
        return 0;
    }
}
