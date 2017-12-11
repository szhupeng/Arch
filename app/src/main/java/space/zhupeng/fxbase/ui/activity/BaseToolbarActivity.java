package space.zhupeng.fxbase.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;

import java.lang.reflect.Method;

import space.zhupeng.fxbase.mvp.presenter.BasePresenter;
import space.zhupeng.fxbase.mvp.view.BaseView;
import space.zhupeng.fxbase.ui.ToolbarDelegate;
import space.zhupeng.fxbase.ui.ToolbarDelegateImpl;
import space.zhupeng.fxbase.utils.ActionModeHelper;

/**
 * 包含Toolbar的Activity基类
 *
 * @author zhupeng
 * @date 2017/1/14
 */

public abstract class BaseToolbarActivity<M, V extends BaseView, P extends BasePresenter<M, V>> extends BaseActivity<M, V, P> implements ToolbarDelegate, View.OnClickListener {

    private ToolbarDelegateImpl mToolbarDelegate;

    protected ActionModeHelper mActionModeHelper;

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        mToolbarDelegate = new ToolbarDelegateImpl(this);

        setToolbar(this);
        bindClickEvent(this);
    }

    @Override
    public void setToolbar(AppCompatActivity activity) {
        mToolbarDelegate.setToolbar(this);
    }

    @Override
    public void bindClickEvent(View.OnClickListener listener) {
        mToolbarDelegate.bindClickEvent(this);
    }

    @Override
    public void setLeft(AppCompatActivity activity, int resId) {
        mToolbarDelegate.setLeft(activity, resId);
    }

    @Override
    public void setLeft(CharSequence text) {
        mToolbarDelegate.setLeft(text);
    }

    @Override
    public void setLeft(AppCompatActivity activity, int resId, CharSequence text) {
        mToolbarDelegate.setLeft(this, resId, text);
    }

    @Override
    public void setCenterTitle(CharSequence title) {
        mToolbarDelegate.setCenterTitle(title);
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
        mToolbarDelegate.setCenterSubtitle(subtitle, size);
    }

    @Override
    public void setCenterSubtitle(CharSequence subtitle, float size, int color) {
        mToolbarDelegate.setCenterSubtitle(subtitle, size, color);
    }

    @Override
    public void setRightText(CharSequence text) {
        mToolbarDelegate.setRightText(text);
    }

    @Override
    public void setRightIcon(int resId) {
        mToolbarDelegate.setRightIcon(resId);
    }

    @Override
    public void showLeft() {
        mToolbarDelegate.showLeft();
    }

    @Override
    public void hideLeft() {
        mToolbarDelegate.hideLeft();
    }

    @Override
    public void showCenter() {
        mToolbarDelegate.showCenter();
    }

    @Override
    public void hideCenter() {
        mToolbarDelegate.hideCenter();
    }

    @Override
    public void showRightText() {
        mToolbarDelegate.showRightText();
    }

    @Override
    public void hideRightText() {
        mToolbarDelegate.hideRightText();
    }

    @Override
    public void showRightIcon() {
        mToolbarDelegate.showRightIcon();
    }

    @Override
    public void hideRightIcon() {
        mToolbarDelegate.hideRightIcon();
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
        if (getContextMenuResID() != 0) {
            getMenuInflater().inflate(getContextMenuResID(), menu);
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
        if (getContextMenuResID() != 0 && menu != null) {
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

    protected int getContextMenuResID() {
        return 0;
    }

    @Override
    protected void onDestroy() {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.unbind();
        }
        super.onDestroy();
    }
}
