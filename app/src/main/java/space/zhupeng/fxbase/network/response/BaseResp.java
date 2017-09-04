package space.zhupeng.fxbase.network.response;

/**
 * @author zhupeng
 * @date 2016/12/12
 */

public class BaseResp<T> {

    public int code;
    public String msg;
    public T data;
}
