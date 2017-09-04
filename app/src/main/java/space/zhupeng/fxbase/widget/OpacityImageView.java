package space.zhupeng.fxbase.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 用于图像可点击而没有设置selector时，默认点击态改变图像透明度
 *
 * @author zhupeng
 * @date 2017/9/3
 */

public class OpacityImageView extends RoundImageView {

    public OpacityImageView(Context context) {
        super(context);
    }

    public OpacityImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OpacityImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
