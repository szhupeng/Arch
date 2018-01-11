package space.zhupeng.arch.ui.activity;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import java.util.List;

import space.zhupeng.arch.R;
import space.zhupeng.arch.widget.BottomNavigationBar;
import space.zhupeng.arch.widget.viewpager.NoScrollViewPager;

/**
 * BottomNavigationView+ViewPager+Fragment实现底部导航基类
 *
 * @author zhupeng
 * @date 2018/1/8
 */

public abstract class BaseBottomBarActivity extends BaseToolbarActivity {

    protected NoScrollViewPager vpBarContent;
    protected BottomNavigationBar mBottomNavigationBar;

    private MenuItem mBottomBarItem;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_base_bottom_bar;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        vpBarContent = (NoScrollViewPager) findViewById(R.id.vp_bar_content);
        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);

        setupBottomNavigation(mBottomNavigationBar);
        mBottomNavigationBar.inflateMenu(getBottomMenuResId());

        vpBarContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (mBottomBarItem != null) {
                    mBottomBarItem.setChecked(false);
                } else {
                    mBottomNavigationBar.getMenu().getItem(0).setChecked(false);
                }
                mBottomBarItem = mBottomNavigationBar.getMenu().getItem(position);
                mBottomBarItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        final BottomBarPagesAdapter adapter = new BottomBarPagesAdapter(getSupportFragmentManager(), getFragments());
        vpBarContent.setAdapter(adapter);
        vpBarContent.setOffscreenPageLimit(adapter.getCount());
        vpBarContent.setScrollEnabled(isScrollEnabled());

        setBadge(0, 1);
    }

    @CallSuper
    protected void setupBottomNavigation(final BottomNavigationView view) {
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                for (int i = 0, size = view.getMenu().size(); i < size; i++) {
                    if (view.getMenu().getItem(i) == item) {
                        vpBarContent.setCurrentItem(i);
                        break;
                    }
                }
                return false;
            }
        });
    }

    public void setBadge(int position, int count) {
        if (mBottomNavigationBar != null) {
            mBottomNavigationBar.setBadgePositionValue(position, count);
        }
    }

    protected boolean isScrollEnabled() {
        return false;
    }

    @MenuRes
    protected abstract int getBottomMenuResId();

    protected abstract List<Fragment> getFragments();

    private static class BottomBarPagesAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragments;

        public BottomBarPagesAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);

            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            if (null == mFragments) return 0;
            return mFragments.size();
        }
    }
}
