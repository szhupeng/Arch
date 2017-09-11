package space.zhupeng.fxbase.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import space.zhupeng.fxbase.R;
import space.zhupeng.fxbase.fragment.MainListFragment;

public class MainActivity extends BaseToolbarActivity {

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected int getContainerId() {
        return R.id.container;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        replaceFragment(MainListFragment.class, null);
    }
}
