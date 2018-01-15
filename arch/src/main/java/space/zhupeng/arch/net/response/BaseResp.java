package space.zhupeng.arch.net.response;

/**
 * @author zhupeng
 * @date 2016/12/12
 */

public class BaseResp<T> {

    public int status;
    public String msg;
    public T result;
}
