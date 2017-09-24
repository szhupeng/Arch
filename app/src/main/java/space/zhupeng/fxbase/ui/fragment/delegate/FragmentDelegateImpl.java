package space.zhupeng.fxbase.ui.fragment.delegate;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

/**
 * @author zhupeng
 * @date 2017/9/17
 */

public class FragmentDelegateImpl implements FragmentDelegate {

    private FragmentManager mFragmentManager;
    private Fragment mFragment;

    public FragmentDelegateImpl(FragmentManager manager, Fragment fragment) {
        this.mFragmentManager = manager;
        this.mFragment = fragment;
    }

    @Override
    public void onAttach(Context context) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onCreateView(View view, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityCreate(Bundle savedInstanceState) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onDestroyView() {

    }

    @Override
    public void onDestroy() {
        this.mFragmentManager = null;
        this.mFragment = null;
    }

    @Override
    public void onDetach() {

    }

    @Override
    public boolean isAdded() {
        return this.mFragment != null && this.mFragment.isAdded();
    }
}
