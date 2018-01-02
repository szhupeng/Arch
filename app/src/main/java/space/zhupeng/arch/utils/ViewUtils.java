package space.zhupeng.arch.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by zhupeng on 2017/12/29.
 */

public class ViewUtils {

    public static void setRippleBackground(Context context, View view) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);
        int[] attribute = new int[]{android.R.attr.selectableItemBackground};
        TypedArray ta = context.getTheme().obtainStyledAttributes(typedValue.resourceId, attribute);
        ViewCompat.setBackground(view, ta.getDrawable(0));
        ta.recycle();
    }
}
