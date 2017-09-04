package space.zhupeng.fxbase.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 用于界面数据未加载之前展示图像轮廓，数据加载后展示图像
 *
 * @author zhupeng
 * @date 2017/9/3
 */

public class OutlineImageView extends OpacityImageView {

    public OutlineImageView(Context context) {
        super(context);
    }

    public OutlineImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OutlineImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
