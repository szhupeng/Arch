package space.zhupeng.arch.network;

import space.zhupeng.arch.network.impl.OkHttpRequest;

/**
 * @author zhupeng
 * @date 2016/12/21
 */

public final class RequestFactory {

    public static Request getRequest() {
        return new OkHttpRequest();
    }
}
