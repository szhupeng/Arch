package space.zhupeng.fxbase.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * 用于界面数据未加载之前展示条形文本轮廓，数据加载后展示文本数据
 *
 * @author zhupeng
 * @date 2017/9/3
 */

public class OutlineTextView extends AppCompatTextView {

    public OutlineTextView(Context context) {
        super(context);
    }

    public OutlineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OutlineTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
