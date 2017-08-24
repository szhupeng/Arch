package space.zhupeng.base.network;

import space.zhupeng.base.network.impl.VolleyRequest;

/**
 * @author zhupeng
 * @date 2016/12/21
 */

public final class RequestFactory {

    public static Request getRequest() {
        return new VolleyRequest();
    }
}
