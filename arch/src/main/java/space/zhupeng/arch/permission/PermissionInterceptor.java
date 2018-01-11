package space.zhupeng.arch.permission;

/**
 * Created by zhupeng on 2017/12/16.
 */

public interface PermissionInterceptor {

    boolean check(String[] permissions);

    void request(String[] permissions);
}
