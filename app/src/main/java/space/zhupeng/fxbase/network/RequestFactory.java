package space.zhupeng.fxbase.network;

import space.zhupeng.fxbase.network.impl.VolleyRequest;

/**
 * @author zhupeng
 * @date 2016/12/21
 */

public final class RequestFactory {

    public static Request getRequest() {
        return new VolleyRequest();
    }
}
