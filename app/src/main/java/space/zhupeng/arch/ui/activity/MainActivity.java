package space.zhupeng.arch.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import space.zhupeng.arch.R;
import space.zhupeng.arch.ui.fragment.BaseWebFragment;
import space.zhupeng.arch.ui.fragment.MainListFragment;
import space.zhupeng.arch.widget.dialog.BottomSheet;

public class MainActivity extends BaseBottomBarActivity {

    @Override
    protected int getBottomMenuResId() {
        return R.menu.menu;
    }

    @Override
    protected List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new MainListFragment());
        fragments.add(BaseWebFragment.newInstance("http://www.baidu.com", null));
        fragments.add(new MainListFragment());
        fragments.add(BaseWebFragment.newInstance("http://www.baidu.com", null));
        return fragments;
    }

    @Override
    protected int getContainerId() {
        return R.id.container;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        setCenterTitle("干货集中营");
        hideLeft();
        hideRightText();
//      hideRightIcon();
//        pushFragment(MainListFragment.class, null);
    }

    @Override
    protected void onRightIconClick() {
//        BottomSheet dialog = new SimpleDialog();
//        dialog.show(getSupportFragmentManager());
//        BaseWebActivity.toHere(getActivity(), "http://www.baidu.com/", null, 0);
//        startActivity(new Intent(this, TabActivity.class));

        BaseWebActivity.toHere(getActivity(), "http://www.baidu.com/", null, 0);
    }

    public static class SimpleDialog extends BottomSheet {
        @Override
        protected int getLayoutResId() {
            return R.layout.dialog_simple;
        }

        @Override
        protected void initViews(@Nullable Bundle savedInstanceState) {
            super.initViews(savedInstanceState);

            TextView tvTitle = findView(R.id.tv_title);
            tvTitle.setText("底部标题");
            TextView tvMessage = findView(R.id.tv_message);
            tvMessage.setText("这是信息布冯");
        }
    }
}
