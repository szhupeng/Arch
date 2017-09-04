package space.zhupeng.fxbase.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * 用于文本可点击而没有设置selector时，默认点击态改变文本透明度
 *
 * @author zhupeng
 * @date 2017/9/3
 */

public class OpacityTextView extends AppCompatTextView {

    public OpacityTextView(Context context) {
        super(context);
    }

    public OpacityTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OpacityTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
