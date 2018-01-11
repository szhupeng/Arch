package space.zhupeng.arch.widget.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import space.zhupeng.arch.R;

/**
 * @author zhupeng
 * @date 2017/12/7
 */

public abstract class BottomSheet extends BottomSheetDialogFragment {

    private BottomSheetDialog mBottomSheetDialog;

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public final Dialog onCreateDialog(Bundle savedInstanceState) {
        mBottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.AppTheme_BottomSheet);
        mBottomSheetDialog.setContentView(getLayoutResId());

        initViews(savedInstanceState);
        bindEvent();
        return mBottomSheetDialog;
    }

    protected void initViews(@Nullable Bundle savedInstanceState) {

    }

    protected void bindEvent() {
    }

    public void show(FragmentManager manager) {
        super.show(manager, "bottom_sheet");
    }

    protected <T extends View> T findView(@IdRes int id) {
        if (null == mBottomSheetDialog) return null;

        return (T) mBottomSheetDialog.findViewById(id);
    }

    @LayoutRes
    protected abstract int getLayoutResId();
}
