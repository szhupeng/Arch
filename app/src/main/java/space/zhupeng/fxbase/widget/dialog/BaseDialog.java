package space.zhupeng.fxbase.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import space.zhupeng.fxbase.R;
import space.zhupeng.fxbase.utils.DensityUtils;

/**
 * @author zhupeng
 * @date 2017/3/27
 */

public abstract class BaseDialog extends Dialog {

    protected Activity mActivity;

    public BaseDialog(@NonNull Context context) {
        super(context, R.style.BaseDialog);

        mActivity = (Activity) context;

        init();
    }

    public BaseDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);

        mActivity = (Activity) context;

        init();
    }

    protected BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);

        mActivity = (Activity) context;

        init();
    }

    protected void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getLayoutResID() > 0) {
            setContentView(getLayoutResID());
        }

        setWindowAttributes(1f, -1f, -1f);
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
