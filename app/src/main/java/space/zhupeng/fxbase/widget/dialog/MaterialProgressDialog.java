package space.zhupeng.fxbase.widget.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import space.zhupeng.fxbase.R;

/**
 * Material Design风格的加载框
 *
 * @author zhupeng
 * @date 2017/8/17
 */

public class MaterialProgressDialog extends BaseDialog {

    private static final float ALPHA = 0.7f;

    protected TextView tvMessage;

    public MaterialProgressDialog(@NonNull Context context) {
        super(context, R.style.MaterialProgressDialog);
    }

    public MaterialProgressDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public MaterialProgressDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getLayoutResID() > 0) {
            setContentView(getLayoutResID());
        }

        setWindowAttributes(ALPHA, -1f, -1f);
        setCanceledOnTouchOutside(false);

        initView();
    }

    public void setMessage(CharSequence message) {
        if (TextUtils.isEmpty(message)) {
            tvMessage.setVisibility(View.GONE);
            setWindowAttributes(ALPHA, -1f, -1f);
        } else {
            tvMessage.setVisibility(View.VISIBLE);
            tvMessage.setText(message);
            setWindowAttributes(ALPHA, 260f, -1f);
        }
    }

    public void setMessage(@StringRes int resId) {
        if (0 == resId) {
            tvMessage.setVisibility(View.GONE);
            setWindowAttributes(ALPHA, -1f, -1f);
        } else {
            tvMessage.setVisibility(View.VISIBLE);
            tvMessage.setText(resId);
            setWindowAttributes(ALPHA, 260f, -1f);
        }
    }


    @Override
    protected void initView() {
        tvMessage = findView(R.id.tv_message);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.dialog_material_progress;
    }
}
