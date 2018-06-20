package space.zhupeng.arch.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import java.util.List;

import space.zhupeng.arch.R;
import space.zhupeng.arch.anim.FragmentAnimation;
import space.zhupeng.arch.anim.Transition;
import space.zhupeng.arch.fragment.XFragment;

/**
 * @author zhupeng
 * @date 2016/12/12
 */
@SuppressWarnings("all")
public abstract class XActivity extends AppCompatActivity {

    protected XFragment mCurrentFragment;

    public void pushFragment(final XFragment fragment) {
        pushFragment(fragment, null, false);
    }

    public void pushFragment(final XFragment fragment, final Bundle args) {
        pushFragment(fragment, args, false);
    }

    public void pushFragment(final XFragment fragment, final Bundle args, final boolean addToBackStack) {
        try {
            final String fname = fragment.getClass().getName();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            if (this.mCurrentFragment != null && this.mCurrentFragment != fragment) {
                ft.hide(this.mCurrentFragment);
            }

            fragment.setArguments(args);
            if (fm.getBackStackEntryCount() > 0) {
                Transition transition = fragment.onCreateTransition();
                if (transition != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        fragment.setSharedElementEnterTransition(transition.sharedElementEnter);
                        mCurrentFragment.setExitTransition(transition.exit);
                        mCurrentFragment.setEnterTransition(transition.enter);
                        fragment.setSharedElementReturnTransition(transition.sharedElementReturn);
                    }

                    for (Transition.SharedElement item : transition.sharedElements) {
                        ft.addSharedElement(item.sharedElement, item.name);
                    }
                } else {
                    FragmentAnimation anim = fragment.onCreateAnimation();
                    if (null == anim) {
                        anim = onCreateAnimation();
                    }

                    if (anim != null) {
                        ft.setCustomAnimations(anim.enter, anim.exit, anim.popEnter, anim.popExit);
                    } else {
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    }
                }
            }

            if (fragment.isAdded()) {
                ft.show(fragment);
            } else {
                ft.add(getContainerId(), fragment, fname);
            }

            mCurrentFragment = fragment;

            if (addToBackStack) {
                ft.addToBackStack(fname);
            }

            ft.commitAllowingStateLoss();
        } catch (Exception e) {
            Log.e("XActivity", e.getMessage());
        }
    }

    public void pushFragment(final Class<? extends XFragment> cls) {
        pushFragment(cls, null, false);
    }

    public void pushFragment(final Class<? extends XFragment> cls, final Bundle args) {
        pushFragment(cls, args, false);
    }

    public void pushFragment(final Class<? extends XFragment> cls, final Bundle args, final boolean addToBackStack) {
        if (null == cls) {
            return;
        }

        XFragment fragment;
        try {
            final String fname = cls.getName();
            FragmentManager fm = getSupportFragmentManager();
            fragment = (XFragment) fm.findFragmentByTag(fname);
            if (null == fragment) {
                fragment = (XFragment) Fragment.instantiate(this, fname);
            }
        } catch (Exception e) {
            Log.e("XActivity", e.getMessage());
            return;
        }

        pushFragment(fragment, args, addToBackStack);
    }

    public void replaceFragment(final XFragment fragment) {
        replaceFragment(fragment, null);
    }

    public void replaceFragment(final XFragment fragment, final Bundle args) {
        try {
            FragmentManager fm = this.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            fragment.setArguments(args);
            if (fm.getBackStackEntryCount() > 0) {
                Transition transition = fragment.onCreateTransition();
                if (transition != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        fragment.setSharedElementEnterTransition(transition.sharedElementEnter);
                        mCurrentFragment.setExitTransition(transition.exit);
                        mCurrentFragment.setEnterTransition(transition.enter);
                        fragment.setSharedElementReturnTransition(transition.sharedElementReturn);
                    }

                    for (Transition.SharedElement item : transition.sharedElements) {
                        ft.addSharedElement(item.sharedElement, item.name);
                    }
                } else {
                    FragmentAnimation anim = fragment.onCreateAnimation();
                    if (null == anim) {
                        anim = onCreateAnimation();
                    }

                    if (anim != null) {
                        ft.setCustomAnimations(anim.enter, anim.exit, anim.popEnter, anim.popExit);
                    } else {
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    }
                }
            }

            ft.replace(getContainerId(), fragment);

            this.mCurrentFragment = fragment;

            ft.commitAllowingStateLoss();
        } catch (Exception e) {
            Log.e("XActivity", e.getMessage());
        }
    }

    public void replaceFragment(final Class<? extends XFragment> cls, final Bundle args) {
        if (null == cls) {
            return;
        }

        XFragment fragment;
        try {
            fragment = cls.newInstance();
        } catch (Exception e) {
            Log.e("XActivity", e.getMessage());
            return;
        }

        replaceFragment(fragment, args);
    }

    /**
     * 所有fragment切换动画
     *
     * @return
     */
    protected FragmentAnimation onCreateAnimation() {
        return new FragmentAnimation(R.anim.enter_vertical, R.anim.exit_vertical, R.anim.pop_enter_vertical, R.anim.pop_exit_vertical);
    }

    protected boolean interceptBack() {
        return false;
    }

    @Override
    public void onBackPressed() {
        if (interceptBack()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mCurrentFragment != null) {
            mCurrentFragment.onActivityResult(requestCode, resultCode, data);
        } else {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            for (Fragment fragment : fragments) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mCurrentFragment != null) {
            if (mCurrentFragment.onKeyDown(keyCode, event)) {
                return true;
            }
        } else {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            for (Fragment fragment : fragments) {
                if (fragment instanceof XFragment && fragment.isVisible()) {
                    if (((XFragment) fragment).onKeyDown(keyCode, event)) {
                        return true;
                    } else {
                        break;
                    }
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1) {
            getResources();
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {
            // 避免系统字体调节以后App字体变化
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();

            res.updateConfiguration(newConfig, res.getDisplayMetrics());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                createConfigurationContext(newConfig);
            } else {
                res.updateConfiguration(newConfig, res.getDisplayMetrics());
            }
        }
        return res;
    }

    @IdRes
    protected abstract int getContainerId();
}
