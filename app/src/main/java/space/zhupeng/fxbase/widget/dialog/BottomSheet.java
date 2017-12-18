package space.zhupeng.fxbase.widget.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;

import space.zhupeng.fxbase.R;

/**
 * @author zhupeng
 * @date 2017/12/7
 */

public class BottomSheet extends BottomSheetDialogFragment {

    public static final String ARG_LAYOUT = "layout";

    private BottomSheetDialog mBottomSheetDialog;

    public static BottomSheet newInstance(@LayoutRes int layoutResID) {
        return newInstance(layoutResID, null);
    }

    /**
     * Use from Fragments.
     *
     * @param layoutResId custom layout to use for the bottom sheet
     * @param fragment    target fragment
     * @return a new instance of BottomSheetSectionDialog
     */
    public static BottomSheet newInstance(@LayoutRes int layoutResId, @Nullable Fragment fragment) {
        BottomSheet bottomSheetFragment = new BottomSheet();
        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT, layoutResId);
        bottomSheetFragment.setArguments(args);
        if (fragment != null) bottomSheetFragment.setTargetFragment(fragment, 0);
        return bottomSheetFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mBottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.AppTheme_BottomSheet);
        mBottomSheetDialog.setContentView(getArguments().getInt(ARG_LAYOUT));

        initViews();

        return mBottomSheetDialog;
    }

    protected void initViews() {
    }
}
