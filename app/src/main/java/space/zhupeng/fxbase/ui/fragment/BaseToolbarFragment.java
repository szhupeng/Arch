package space.zhupeng.fxbase.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import space.zhupeng.fxbase.R;
import space.zhupeng.fxbase.mvp.model.BaseModel;
import space.zhupeng.fxbase.mvp.presenter.BasePresenter;
import space.zhupeng.fxbase.mvp.view.BaseView;
import space.zhupeng.fxbase.ui.ToolbarDelegate;
import space.zhupeng.fxbase.ui.ToolbarDelegateImpl;

/**
 * 如果项目使用的是单Activity多Fragment，建议继承此类
 *
 * @author zhupeng
 * @date 2017/9/4
 */

public abstract class BaseToolbarFragment<M extends BaseModel, V extends BaseView, P extends BasePresenter<M, V>> extends BaseFragment<M, V, P> implements ToolbarDelegate, View.OnClickListener {

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private ToolbarDelegateImpl mToolbarDelegate;

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);

        if (mToolbar != null) {
            mToolbarDelegate = new ToolbarDelegateImpl(this, view, mToolbar);
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
            mToolbarDelegate.setLeft(activity, resId, text);
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
        if (mToolbarDelegate != null) {
            mToolbarDelegate.setCenterTitle(activity, resId);
        }
    }

    @Override
    public void setCenterSubtitle(CharSequence subtitle) {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.setCenterSubtitle(subtitle);
        }
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

    @Override
    public void onDestroy() {
        if (mToolbarDelegate != null) {
            mToolbarDelegate.unbind();
        }
        super.onDestroy();
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
