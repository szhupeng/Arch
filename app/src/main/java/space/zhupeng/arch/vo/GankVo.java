package space.zhupeng.arch.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhupeng on 2017/12/10.
 */

public class GankVo<T> implements Serializable {

    public GankVo() {
    }

    public boolean error;
    public List<String> category;

    public T results;
}
