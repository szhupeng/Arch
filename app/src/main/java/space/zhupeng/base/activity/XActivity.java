package space.zhupeng.base.activity;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import space.zhupeng.base.R;
import space.zhupeng.base.analysis.Analysis;
import space.zhupeng.base.anim.FragmentAnimation;
import space.zhupeng.base.anim.Transition;
import space.zhupeng.base.fragment.XFragment;

/**
 * @author zhupeng
 * @date 2016/12/12
 */

@SuppressWarnings("all")
public abstract class XActivity extends AppCompatActivity implements LifecycleRegistryOwner {

    private final LifecycleRegistry mRegistry = new LifecycleRegistry(this);

    @Override
    public LifecycleRegistry getLifecycle() {
        return mRegistry;
    }

    protected XFragment mCurrentFragment;

    protected void pushFragment(final Class<? extends XFragment> cls, final Object data) {
        pushFragment(cls, data, true);
    }

    protected void pushFragment(final Class<? extends XFragment> cls, final Object data, final boolean addToBackStack) {
        if (null == cls) {
            return;
        }

        try {
            final String fname = cls.getName();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            XFragment fragment = (XFragment) fm.findFragmentByTag(fname);
            if (null == fragment) {
                fragment = (XFragment) Fragment.instantiate(this, fname);
            }

            if (this.mCurrentFragment != null && this.mCurrentFragment != fragment) {
                ft.hide(this.mCurrentFragment);
            }

            fragment.setData(data);
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
            Analysis.reportError(this, e);
        }
    }

    protected void replaceFragment(Class<? extends XFragment> cls, Object data) {
        if (null == cls) {
            return;
        }

        try {
            FragmentManager fm = this.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            XFragment fragment = cls.newInstance();
            fragment.setData(data);
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
            Analysis.reportError(this, e);
        }
    }

    /**
     * 所有fragment切换动画
     *
     * @return
     */
    public FragmentAnimation onCreateAnimation() {
        return new FragmentAnimation(R.anim.enter_vertical, R.anim.exit_vertical, R.anim.pop_enter_vertical, R.anim.pop_exit_vertical);
    }

    protected boolean doBeforeBack() {
        return false;
    }

    @Override
    public void onBackPressed() {
        if (doBeforeBack()) {
            return;
        }
        super.onBackPressed();
    }

    @IdRes
    protected abstract int getContainerId();
}
