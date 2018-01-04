package space.zhupeng.arch.components.activity;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.List;

import butterknife.BindView;
import space.zhupeng.arch.R;
import space.zhupeng.arch.mvp.model.Repository;
import space.zhupeng.arch.mvp.presenter.BasePresenter;
import space.zhupeng.arch.mvp.view.BaseView;

/**
 * tab切换的基类
 *
 * @author zhupeng
 * @date 2017/1/14
 */

public abstract class BaseTabActivity<M extends Repository, V extends BaseView, P extends BasePresenter<M, V>> extends BaseToolbarActivity<M, V, P> implements TabLayout.OnTabSelectedListener {

    @BindView(R.id.layout_tabs_bar)
    TabLayout mTabLayout;

    @BindView(R.id.vp_tab_content)
    ViewPager vpTabContent;

    private PagerAdapter mPagerAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_base_tab;
    }

    @CallSuper
    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        vpTabContent.setAdapter(getPagerAdapter());
        vpTabContent.setOffscreenPageLimit(getOffscreenPageLimit());
        mTabLayout.addOnTabSelectedListener(this);
        mTabLayout.setupWithViewPager(vpTabContent);

        setupTabLayout(mTabLayout);
    }

    protected void setupTabLayout(TabLayout tabLayout) {
    }

    protected PagerAdapter getPagerAdapter() {
        return new TabPagerAdapter(getSupportFragmentManager(), buildUpTabs());
    }

    protected int getOffscreenPageLimit() {
        return 2;
    }

    protected abstract List<Tab> buildUpTabs();

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    public class TabPagerAdapter extends FragmentStatePagerAdapter {

        private List<Tab> mTabs;

        public TabPagerAdapter(FragmentManager fm, List<Tab> tabs) {
            super(fm);
            this.mTabs = tabs;
        }

        @Override
        public Fragment getItem(int position) {
            final Tab tab = mTabs.get(position);
            return Fragment.instantiate(getActivity(), tab.cls.getName(), tab.args);
        }

        @Override
        public int getCount() {
            if (null == mTabs) return 0;
            return mTabs.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabs.get(position).title;
        }
    }

    public static class Tab {
        public CharSequence title;
        public Class<? extends Fragment> cls;
        public Bundle args;

        public Tab(CharSequence title, Class<? extends Fragment> cls) {
            this.title = title;
            this.cls = cls;
        }

        public Tab(CharSequence title, Class<? extends Fragment> cls, Bundle args) {
            this.title = title;
            this.cls = cls;
            this.args = args;
        }
    }
}
