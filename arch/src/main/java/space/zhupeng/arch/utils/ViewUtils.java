package space.zhupeng.arch.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.util.TypedValue;
import android.view.View;

/**
 * @author zhupeng
 * @date 2017/12/29
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

    public static Drawable getDrawable(Context context, @DrawableRes int resId, int colorFilter) {
        Drawable drawable = ContextCompat.getDrawable(context, resId);
        drawable.setColorFilter(ContextCompat.getColor(context, colorFilter), PorterDuff.Mode.SRC_IN);
        return drawable;
    }

    public static Bitmap getBitmap(Context context, @DrawableRes int resId) {
        Drawable drawable = ContextCompat.getDrawable(context, resId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
