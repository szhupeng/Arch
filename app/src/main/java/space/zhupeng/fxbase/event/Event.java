package space.zhupeng.fxbase.event;

/**
 * @author zhupeng
 * @date 2016/12/21
 */

public class Event<T> {
    private T data;
    private int code;

    public Event(T data) {
        this.data = data;
    }

    public Event(T data, int code) {
        this.data = data;
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}