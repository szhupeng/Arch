package space.zhupeng.fxbase.widget.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import space.zhupeng.fxbase.R;

/**
 * 自定义对话框基类
 *
 * @author zhupeng
 * @date 2016/12/11
 */

public abstract class BaseDialogFragment extends DialogFragment {

    protected Bundle mArgs;
    protected Activity mActivity;
    private int width;
    private int gravity = Gravity.CENTER;

    protected static <T extends BaseDialogFragment> T newInstance(Class<T> cls, Bundle args) {
        T fragment = null;
        try {
            fragment = cls.newInstance();
            fragment.setArguments(args);
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mArgs = getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View root = inflater.inflate(getLayoutResID(), null);
        initialize();
        initView(root);
        return root;
    }

    private void initialize() {
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.BaseDialog);
        getDialog().getWindow().getAttributes().windowAnimations = getAnimStyles();

        //设置对话框背景色，否则有虚框
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCancelable(isCancelable());
        getDialog().setCanceledOnTouchOutside(false);

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (isCancelable()) {
                        onCancel();
                    }
                    return !isCancelable();
                }

                return false;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setGravity(gravity);
        if (0 == width) {
            width = getResources().getDimensionPixelSize(R.dimen.custom_dialog_width);
        }
        getDialog().getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    /**
     * 初始化对话框视图
     *
     * @param view
     */
    protected void initView(View view) {
    }

    /**
     * 当对话框消失时的监听事件
     */
    protected void onCancel() {
    }

    @LayoutRes
    protected abstract int getLayoutResID();

    @StyleRes
    protected int getAnimStyles() {
        return 0;
    }

    @Override
    public void dismiss() {
        if (mActivity != null && !mActivity.isFinishing()) {
            //防止窗体句柄泄漏
            super.dismiss();
        }
    }

    public void show(FragmentManager manager) {
        super.show(manager, "dialog");
    }
}
