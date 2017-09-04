package space.zhupeng.fxbase.fragment;


import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.support.v4.app.Fragment;

import space.zhupeng.fxbase.anim.FragmentAnimation;
import space.zhupeng.fxbase.anim.Transition;

/**
 * @author zhupeng
 * @date 2016/12/21
 */

public abstract class XFragment extends Fragment implements LifecycleRegistryOwner {

    LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    @Override
    public LifecycleRegistry getLifecycle() {
        return mLifecycleRegistry;
    }

    protected Object mPassedData;

    public void setData(Object data) {
        this.mPassedData = data;
    }

    /**
     * 当前fragment切换动画
     *
     * @return
     */
    public FragmentAnimation onCreateAnimation() {
        return null;
    }

    /**
     * 共享元素转场动画
     *
     * @return
     */
    public Transition onCreateTransition() {
        return null;
    }
}
