package space.zhupeng.fxbase;

/**
 * fragment实现该接口，在activity中数据变化的时候通知各fragment进行更新
 *
 * @author zhupeng
 * @date 2017/12/10
 */

public interface Observer<T> {
    void update(T data);
}
