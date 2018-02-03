package space.zhupeng.arch.permission;

import android.support.annotation.NonNull;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author zhupeng
 * @date 2017/12/16
 */

public final class PermissionProxy implements InvocationHandler {

    private final PermissionInterceptor mTarget;

    private PermissionProxy(PermissionInterceptor target) {
        this.mTarget = target;
    }

    public static PermissionInterceptor bind(@NonNull PermissionInterceptor target) {
        PermissionProxy proxy = new PermissionProxy(target);
        return (PermissionInterceptor) Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(), proxy);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        CheckPermission check = method.getAnnotation(CheckPermission.class);
        if (check != null) {
            String[] permissions = check.value();
            if (!mTarget.check(permissions)) {
                mTarget.request(permissions);
                return null;
            }
        }
        return method.invoke(mTarget, args);
    }
}
