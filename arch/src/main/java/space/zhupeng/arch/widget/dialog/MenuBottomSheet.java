package space.zhupeng.arch.widget.dialog;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import space.zhupeng.arch.R;

/**
 * @author zhupeng
 * @date 2017/12/20
 */

public class MenuBottomSheet extends BottomSheet {

    private MenuAdapter mMenuAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.dialog_bottom_menu;
    }

    @Override
    protected void initViews(@Nullable Bundle savedInstanceState) {
        super.initViews(savedInstanceState);


    }

    public void setMenuAdapter(MenuAdapter adapter) {
        this.mMenuAdapter = adapter;
    }

    public static abstract class MenuAdapter {

        private MenuInflater mMenuInflater;

        public MenuAdapter(MenuInflater inflater) {
            this.mMenuInflater = inflater;
        }

        @MenuRes
        public abstract int getMenuResId();

        @LayoutRes
        public abstract int getMenuItemLayoutResId();

        public abstract void bindMenuItem(MenuItem item, View view);
    }
}
