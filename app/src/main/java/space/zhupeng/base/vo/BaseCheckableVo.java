package space.zhupeng.base.vo;

import android.widget.Checkable;

/**
 * 可选列表数据实体基类
 *
 * @author zhupeng
 * @date 2017/8/20
 */

public class BaseCheckableVo implements Checkable {

    public boolean isChecked;

    @Override
    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }
}
