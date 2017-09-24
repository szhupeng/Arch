package space.zhupeng.fxbase.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import space.zhupeng.fxbase.ui.fragment.delegate.FragmentDelegate;
import space.zhupeng.fxbase.ui.fragment.delegate.FragmentDelegateImpl;

/**
 * @author zhupeng
 * @date 2017/9/17
 */

public class FragmentLifecycle extends FragmentManager.FragmentLifecycleCallbacks {

    private FragmentDelegate mDelegate;

    public FragmentLifecycle(FragmentDelegate delegate) {
        this.mDelegate = delegate;
    }

    @Override
    public void onFragmentAttached(FragmentManager fm, Fragment f, Context context) {
        if (null == mDelegate || !mDelegate.isAdded()) {
            mDelegate = new FragmentDelegateImpl(fm, f);
        }
        mDelegate.onAttach(context);
    }

    @Override
    public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
        if (mDelegate != null) {
            mDelegate.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onFragmentActivityCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
        if (mDelegate != null) {
            mDelegate.onActivityCreate(savedInstanceState);
        }
    }

    @Override
    public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
        if (mDelegate != null) {
            mDelegate.onCreateView(v, savedInstanceState);
        }
    }

    @Override
    public void onFragmentStarted(FragmentManager fm, Fragment f) {
        if (mDelegate != null) {
            mDelegate.onStart();
        }
    }

    @Override
    public void onFragmentResumed(FragmentManager fm, Fragment f) {
        if (mDelegate != null) {
            mDelegate.onResume();
        }
    }

    @Override
    public void onFragmentPaused(FragmentManager fm, Fragment f) {
        if (mDelegate != null) {
            mDelegate.onPause();
        }
    }

    @Override
    public void onFragmentStopped(FragmentManager fm, Fragment f) {
        if (mDelegate != null) {
            mDelegate.onStop();
        }
    }

    @Override
    public void onFragmentSaveInstanceState(FragmentManager fm, Fragment f, Bundle outState) {
        if (mDelegate != null) {
            mDelegate.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
        if (mDelegate != null) {
            mDelegate.onDestroyView();
        }
    }

    @Override
    public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
        if (mDelegate != null) {
            mDelegate.onDestroy();
        }
    }

    @Override
    public void onFragmentDetached(FragmentManager fm, Fragment f) {
        if (mDelegate != null) {
            mDelegate.onDetach();
            f.getArguments().clear();
        }
    }
}
