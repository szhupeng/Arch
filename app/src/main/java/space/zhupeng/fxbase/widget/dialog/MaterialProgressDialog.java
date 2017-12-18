package space.zhupeng.fxbase.widget.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
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

    @Override
    protected void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getLayoutResID() > 0) {
            setContentView(getLayoutResID());
        }

        setWindowAttributes(ALPHA, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        initView();
    }

    public void setMessage(CharSequence message) {
        if (TextUtils.isEmpty(message)) {
            tvMessage.setVisibility(View.GONE);
            setWindowAttributes(ALPHA, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            tvMessage.setVisibility(View.VISIBLE);
            tvMessage.setText(message);
            setWindowAttributes(ALPHA, dp2px(260f), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    public void setMessage(@StringRes int resId) {
        if (0 == resId) {
            tvMessage.setVisibility(View.GONE);
            setWindowAttributes(ALPHA, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            tvMessage.setVisibility(View.VISIBLE);
            tvMessage.setText(resId);
            setWindowAttributes(ALPHA, dp2px(260f), ViewGroup.LayoutParams.WRAP_CONTENT);
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
