package space.zhupeng.arch.mvp.view;

import android.support.v4.util.ArrayMap;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import space.zhupeng.compiler.CacheMethod;

/**
 * 视图代理和一级缓存
 *
 * @author zhupeng
 * @date 2018/3/1
 */

public abstract class AbstractViewProxy<V extends BaseView> implements InvocationHandler {

    private final ArrayMap<Method, Object[]> mMethodCaches = new ArrayMap<>();
    private WeakReference<V> mViewRef;

    /**
     * 通过Proxy.newProxyInstance()构建一个动态代理的对象
     *
     * @param cls
     * @return
     */
    public V proxy(Class<V> cls) {
        if (null == cls) {
            throw new NullPointerException("Proxy class is NULL!");
        }
        return (V) Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, this);
    }

    /**
     * 将真实的View提供到代理对象
     *
     * @param view
     */
    public void bind(V view) {
        if (null == view) return;

        unbind();

        mViewRef = new WeakReference<V>(view);
        for (int i = 0, size = mMethodCaches.size(); i < size; i++) {
            invokeMethod(view, mMethodCaches.keyAt(i), mMethodCaches.valueAt(i));
        }

        view.onProxyBound();
    }

    public void unbind() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

    public boolean isBound() {
        return mViewRef != null && mViewRef.get() != null;
    }

    void destroy() {
        unbind();
        mMethodCaches.clear();
        onDestroy();
    }

    /**
     * 在代理对象的任一方法被调用的时候，会回调此方法
     *
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (isMethodCached(method)) {
            mMethodCaches.put(method, args);
        }

        if (mViewRef != null && mViewRef.get() != null) {
            return invokeMethod(mViewRef.get(), method, args);
        }

        return null;
    }

    /**
     * 当前代理对象调用的方法是否需要被缓存
     *
     * @param method
     * @return
     */
    private boolean isMethodCached(Method method) {
        CacheMethod annotation = method.getAnnotation(CacheMethod.class);
        return annotation != null && annotation.enable();
    }

    private Object invokeMethod(Object view, Method method, Object[] args) {
        if (null == view || null == method) return null;

        try {
            return method.invoke(view, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected abstract void onDestroy();
}
