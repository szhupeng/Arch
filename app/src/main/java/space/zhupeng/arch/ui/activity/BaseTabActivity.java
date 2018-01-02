package space.zhupeng.arch.ui.activity;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import butterknife.BindView;
import space.zhupeng.arch.R;
import space.zhupeng.arch.mvp.model.BaseModel;
import space.zhupeng.arch.mvp.presenter.BasePresenter;
import space.zhupeng.arch.mvp.view.BaseView;

/**
 * tab切换的基类
 *
 * @author zhupeng
 * @date 2017/1/14
 */

public abstract class BaseTabActivity<M extends BaseModel, V extends BaseView, P extends BasePresenter<M, V>> extends BaseToolbarActivity<M, V, P> implements TabLayout.OnTabSelectedListener {

    @BindView(R.id.layout_tabs_bar)
    TabLayout mTabLayout;

    @BindView(R.id.vp_tab_content)
    ViewPager vpTabContent;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_base_tab;
    }

    @CallSuper
    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        setupTabLayout(mTabLayout);

        mTabLayout.setupWithViewPager(vpTabContent);
        mTabLayout.addOnTabSelectedListener(this);
    }

    protected void setupTabLayout(TabLayout tabLayout) {
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    public static class TabFragment extends FragmentPagerAdapter {

        public TabFragment(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return 0;
        }
    }
}
