package space.zhupeng.fxbase.vo;

import android.os.Parcelable;

/**
 * @author zhupeng
 * @date 2017/8/19
 */

public abstract class BaseVo implements Parcelable {

    public BaseVo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
