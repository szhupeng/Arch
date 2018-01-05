package space.zhupeng.arch.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;

import java.lang.reflect.Field;

import space.zhupeng.arch.anim.FragmentAnimation;
import space.zhupeng.arch.anim.Transition;

/**
 * @author zhupeng
 * @date 2016/12/21
 */

public abstract class XFragment extends Fragment {

    protected boolean isViewCreated;
    protected boolean isVisibleToUser;
    protected boolean isDataLoaded;

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

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isViewCreated = true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        this.isVisibleToUser = isVisibleToUser;
        fetchData();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getUserVisibleHint()) {
            setUserVisibleHint(true);
        }
    }

    /**
     * 懒加载数据
     */
    protected abstract void loadDataLazily();

    final void fetchData() {
        fetchData(false);
    }

    final void fetchData(boolean forceUpdate) {
        if (isVisibleToUser && isViewCreated && (!isDataLoaded || forceUpdate)) {
            loadDataLazily();
            isDataLoaded = true;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
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
