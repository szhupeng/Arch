package space.zhupeng.base.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import space.zhupeng.base.R;
import space.zhupeng.base.utils.DensityUtils;

/**
 * @author zhupeng
 * @date 2017/3/27
 */

public abstract class BaseDialog extends Dialog {

    public BaseDialog(@NonNull Context context) {
        super(context, R.style.BaseDialog);

        init();
    }

    public BaseDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);

        init();
    }

    protected BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);

        init();
    }

    protected void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getLayoutResID() > 0) {
            setContentView(getLayoutResID());
        }

        DisplayMetrics metrics = getContext().getResources()
                .getDisplayMetrics();
        setWindowAttributes(Math.round(metrics.widthPixels * 0.65f));
        setCanceledOnTouchOutside(false);

        initView();
    }

    protected void setWindowAttributes(float width) {
        setWindowAttributes(1f, width, -1f);
    }

    protected void setWindowAttributes(float alpha, float width, float height) {
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.alpha = alpha;
        if (width > 0) {
            attributes.width = dp2px(width);
        } else {
            attributes.width = WindowManager.LayoutParams.WRAP_CONTENT;
        }
        if (height > 0) {
            attributes.height = dp2px(height);
        } else {
            attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        }
        window.setAttributes(attributes);
    }

    protected int dp2px(float dp) {
        return DensityUtils.dp2px(getContext(), dp);
    }

    protected void initView() {
    }

    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    @LayoutRes
    protected abstract int getLayoutResID();
}
