package space.zhupeng.base.widget.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import space.zhupeng.base.R;

/**
 * 自定义对话框基类
 *
 * @author zhupeng
 * @date 2016/12/11
 */

public abstract class BaseDialogFragment extends DialogFragment {

    protected Bundle mArgs;
    protected Activity mActivity;
    private boolean isCancelable, isCancelableTouchOutSide;

    public static <T extends BaseDialogFragment> T newInstance(Class cls, Bundle args) {
        T fragment = null;
        try {
            fragment = (T) cls.newInstance();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        fragment.setArguments(args);
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
        View root = inflater.inflate(getLayoutId(), container, false);
        initialize();
        initView(root);
        return root;
    }
    
    private void initialize() {
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.BaseDialog);
        getDialog().getWindow().getAttributes().windowAnimations = getAnimationId();

        //设置对话框背景色，否则有虚框
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCancelable(isCancelable);
        getDialog().setCanceledOnTouchOutside(isCancelable);

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (isCancelable) {
                        onCancel();
                    }
                    return !isCancelable;
                }

                return false;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        //设置对话框显示在底部
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        //设置让对话框宽度充满屏幕
        getDialog().getWindow().setLayout(getDialog().getWindow().getAttributes().width, getDialog().getWindow().getAttributes().height);
    }

    public BaseDialogFragment setGravity(int gravity) {

        return this;
    }

    /**
     * 初始化对话框视图
     *
     * @param view
     */
    protected abstract void initView(View view);

    /**
     * 当对话框消失时的监听事件
     */
    protected void onCancel() {
    }

    @LayoutRes
    protected abstract int getLayoutId();

    @AnimRes
    protected int getAnimationId() {
        return 0;
    }
}
