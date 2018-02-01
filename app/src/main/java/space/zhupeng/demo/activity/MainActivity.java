package space.zhupeng.demo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import space.zhupeng.arch.ui.activity.BaseToolbarActivity;
import space.zhupeng.demo.R;
import space.zhupeng.demo.fragment.MainListFragment;

public class MainActivity extends BaseToolbarActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getContainerId() {
        return R.id.container;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        hideLeft();
        setCenterTitle("菜谱大全");
        
        replaceFragment(MainListFragment.class, null);
    }
}
