package space.zhupeng.arch.ui.fragment;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import space.zhupeng.arch.R;
import space.zhupeng.arch.mvp.model.Repository;
import space.zhupeng.arch.mvp.presenter.BasePresenter;
import space.zhupeng.arch.mvp.view.BaseView;
import space.zhupeng.arch.ui.ToolbarDelegate;
import space.zhupeng.arch.ui.ToolbarDelegateImpl;

/**
 * 如果项目使用的是单Activity多Fragment，建议继承此类
 *
 * @author zhupeng
 * @date 2017/9/4
 */

public abstract class BaseToolbarFragment<M extends Repository, V extends BaseView, P extends BasePresenter<M, V>> extends BaseFragment<M, V, P> implements ToolbarDelegate, View.OnClickListener {

    @Nullable
    Toolbar mToolbar;

    private ToolbarDelegateImpl mToolbarDelegate;

    @CallSuper
    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);

        mToolbar = view.findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbarDelegate = new ToolbarDelegateImpl(this, view, mToolbar);
            bindClickEvent(this);
        }
    }

    @CallSuper
    @Override
    public void bindClickEvent(View.OnClickListener listener) {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.bindClickEvent(listener);
        }
    }

    @CallSuper
    @Override
    public void setLeft(AppCompatActivity activity, int resId) {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.setLeft(activity, resId);
        }
    }

    @CallSuper
    @Override
    public void setLeft(CharSequence text) {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.setLeft(text);
        }
    }

    @CallSuper
    @Override
    public void setLeft(AppCompatActivity activity, int resId, CharSequence text) {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.setLeft(activity, resId, text);
        }
    }

    @CallSuper
    @Override
    public void setCenterTitle(CharSequence title) {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.setCenterTitle(title);
        }
    }

    @CallSuper
    @Override
    public void setCenterTitle(AppCompatActivity activity, int resId) {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.setCenterTitle(activity, resId);
        }
    }

    @CallSuper
    @Override
    public void setCenterSubtitle(CharSequence subtitle) {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.setCenterSubtitle(subtitle);
        }
    }

    @CallSuper
    @Override
    public void setCenterSubtitle(CharSequence subtitle, float size) {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.setCenterSubtitle(subtitle, size);
        }
    }

    @CallSuper
    @Override
    public void setCenterSubtitle(CharSequence subtitle, float size, int color) {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.setCenterSubtitle(subtitle, size, color);
        }
    }

    @CallSuper
    @Override
    public void setRightText(CharSequence text) {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.setRightText(text);
        }
    }

    @CallSuper
    @Override
    public void setRightIcon(int resId) {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.setRightIcon(resId);
        }
    }

    @CallSuper
    @Override
    public void showLeft() {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.showLeft();
        }
    }

    @CallSuper
    @Override
    public void hideLeft() {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.hideLeft();
        }
    }

    @CallSuper
    @Override
    public void showCenter() {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.showCenter();
        }
    }

    @CallSuper
    @Override
    public void hideCenter() {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.hideCenter();
        }
    }

    @CallSuper
    @Override
    public void showRightText() {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.showRightText();
        }
    }

    @CallSuper
    @Override
    public void hideRightText() {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.hideRightText();
        }
    }

    @CallSuper
    @Override
    public void showRightIcon() {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.showRightIcon();
        }
    }

    @CallSuper
    @Override
    public void hideRightIcon() {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.hideRightIcon();
        }
    }

    @CallSuper
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
        getActivity().finish();
    }

    protected void onCenterClick() {
    }

    protected void onRightTextClick() {
    }

    protected void onRightIconClick() {
    }
}
