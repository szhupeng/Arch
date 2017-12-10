package space.zhupeng.fxbase.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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

public abstract class BaseToolbarFragment<M, V extends BaseView, P extends BasePresenter<M, V>> extends BaseFragment<M, V, P> implements ToolbarDelegate, View.OnClickListener {

    private ToolbarDelegateImpl mToolbarDelegate;

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);

        mToolbarDelegate = new ToolbarDelegateImpl(this, view);

        setToolbar((AppCompatActivity) getActivity());
    }

    @Override
    public void setToolbar(AppCompatActivity activity) {
        mToolbarDelegate.setToolbar(activity);
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
        mToolbarDelegate.setLeft(activity, resId, text);
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
        getActivity().finish();
    }

    protected void onCenterClick() {
    }

    protected void onRightTextClick() {
    }

    protected void onRightIconClick() {
    }
}
