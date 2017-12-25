package space.zhupeng.fxbase.widget.dialog;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import space.zhupeng.fxbase.R;
import space.zhupeng.fxbase.utils.DensityUtils;

/**
 * @author zhupeng
 * @date 2017/3/27
 */

public abstract class BaseDialog extends AppCompatDialog {

    protected Activity mActivity;

    public BaseDialog(@NonNull Context context) {
        this(context, R.style.BaseDialog);
    }

    public BaseDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);

        mActivity = (Activity) context;

        init();
    }

    protected void init() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        if (getLayoutResId() > 0) {
            setContentView(getLayoutResId());
        }

        final int width = getContext().getResources().getDimensionPixelSize(R.dimen.custom_dialog_width);
        setWindowAttributes(width);
        setCanceledOnTouchOutside(false);

        initView();
    }

    @Override
    public void dismiss() {
        if (mActivity != null && !mActivity.isFinishing()) {
            //防止窗体句柄泄漏
            super.dismiss();
        }
    }

    protected void setWindowAttributes(int width) {
        setWindowAttributes(1f, width, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    protected void setWindowAttributes(float alpha, int width, int height) {
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.alpha = alpha;
        attributes.width = width;
        attributes.height = height;
        window.setAttributes(attributes);
    }

    protected int dp2px(float dp) {
        return DensityUtils.dp2px(getContext(), dp);
    }

    protected void initView() {
    }

    protected <T extends View> T findView(@IdRes int id) {
        return (T) findViewById(id);
    }

    @LayoutRes
    protected abstract int getLayoutResId();
}
