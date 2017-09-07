package space.zhupeng.fxbase.fragment;

import android.support.v4.app.Fragment;

import java.lang.reflect.Field;

import space.zhupeng.fxbase.anim.FragmentAnimation;
import space.zhupeng.fxbase.anim.Transition;

/**
 * @author zhupeng
 * @date 2016/12/21
 */

public abstract class XFragment extends Fragment {

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

    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class
                    .getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
