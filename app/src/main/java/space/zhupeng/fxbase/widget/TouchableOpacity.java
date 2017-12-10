package space.zhupeng.fxbase.widget;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.FloatRange;
import android.support.v4.view.ViewCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * @author zhupeng
 * @date 2017/9/27
 */

public abstract class TouchableOpacity {

    public final void wrap(View view) {
        if (null == view) {
            throw new NullPointerException("The view passed to TouchableOpacity cannot be null");
        }

        final int pressed = android.R.attr.state_pressed;

        StateListDrawable sld = new StateListDrawable();
        if (view instanceof ImageView) {
            Drawable src = ((ImageView) view).getDrawable();
            Drawable alpha = src.getConstantState().newDrawable();
            if (src != null) {
                alpha.mutate().setAlpha(Math.round(255 * activeOpacity()));
                sld.addState(new int[]{pressed}, alpha);
                sld.addState(new int[]{}, src);
                ((ImageView) view).setImageDrawable(sld);
                return;
            }
        }

        Drawable drawable = view.getBackground();
        if (null == drawable) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEvent.ACTION_DOWN == event.getAction()) {
                        v.setAlpha(activeOpacity());
                    } else if (MotionEvent.ACTION_UP == event.getAction()) {
                        v.setAlpha(1f);
                    }
                    return false;
                }
            });
            return;
        }


        ViewCompat.setBackground(view, sld);
    }

    @FloatRange(from = 0.0, to = 1.0)
    protected abstract float activeOpacity();
}
