package space.zhupeng.arch.ui.activity;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import java.lang.reflect.Method;

import space.zhupeng.arch.R;
import space.zhupeng.arch.mvp.model.Repository;
import space.zhupeng.arch.mvp.presenter.BasePresenter;
import space.zhupeng.arch.mvp.view.BaseView;
import space.zhupeng.arch.ui.ToolbarDelegate;
import space.zhupeng.arch.ui.ToolbarDelegateImpl;
import space.zhupeng.arch.utils.ActionModeHelper;

/**
 * 包含Toolbar的Activity基类
 *
 * @author zhupeng
 * @date 2017/1/14
 */

public abstract class BaseToolbarActivity<M extends Repository, V extends BaseView, P extends BasePresenter<M, V>> extends BaseActivity<M, V, P> implements ToolbarDelegate, View.OnClickListener {

    @Nullable
    protected Toolbar mToolbar;

    private ToolbarDelegateImpl mToolbarDelegate;
    protected ActionModeHelper mActionModeHelper;

    @CallSuper
    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        mToolbar = findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbarDelegate = new ToolbarDelegateImpl(this, mToolbar);
            bindClickEvent(this);
        }
    }

    @Override
    public void bindClickEvent(View.OnClickListener listener) {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.bindClickEvent(listener);
        }
    }

    @Override
    public void setLeft(AppCompatActivity activity, int resId) {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.setLeft(activity, resId);
        }
    }

    @Override
    public void setLeft(CharSequence text) {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.setLeft(text);
        }
    }

    @Override
    public void setLeft(AppCompatActivity activity, int resId, CharSequence text) {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.setLeft(this, resId, text);
        }
    }

    @Override
    public void setCenterTitle(CharSequence title) {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.setCenterTitle(title);
        }
    }

    @Override
    public void setCenterTitle(AppCompatActivity activity, int resId) {
        mToolbarDelegate.setCenterTitle(activity, resId);
    }

    @Override
    public void setCenterSubtitle(CharSequence subtitle) {
        mToolbarDelegate.setCenterSubtitle(subtitle);
    }

    @Override
    public void setCenterSubtitle(CharSequence subtitle, float size) {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.setCenterSubtitle(subtitle, size);
        }
    }

    @Override
    public void setCenterSubtitle(CharSequence subtitle, float size, int color) {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.setCenterSubtitle(subtitle, size, color);
        }
    }

    @Override
    public void setRightText(CharSequence text) {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.setRightText(text);
        }
    }

    @Override
    public void setRightIcon(int resId) {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.setRightIcon(resId);
        }
    }

    @Override
    public void showLeft() {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.showLeft();
        }
    }

    @Override
    public void hideLeft() {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.hideLeft();
        }
    }

    @Override
    public void showCenter() {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.showCenter();
        }
    }

    @Override
    public void hideCenter() {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.hideCenter();
        }
    }

    @Override
    public void showRightText() {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.showRightText();
        }
    }

    @Override
    public void hideRightText() {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.hideRightText();
        }
    }

    @Override
    public void showRightIcon() {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.showRightIcon();
        }
    }

    @Override
    public void hideRightIcon() {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.hideRightIcon();
        }
    }

    @Override
    public void onClick(View v) {
        if (null == mToolbarDelegate) return;

        if (mToolbarDelegate.isLeftClicked(v.getId())) {
            onLeftClick();
        } else if (mToolbarDelegate.isCenterTitleClicked(v.getId())) {
            onCenterClick();
        } else if (mToolbarDelegate.isRightTextClicked(v.getId())) {
            onRightTextClick();
        } else if (mToolbarDelegate.isRightIconClicked(v.getId())) {
            onRightIconClick();
        }
    }

    protected void onLeftClick() {
        finish();
    }

    protected void onCenterClick() {
    }

    protected void onRightTextClick() {
    }

    protected void onRightIconClick() {
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
