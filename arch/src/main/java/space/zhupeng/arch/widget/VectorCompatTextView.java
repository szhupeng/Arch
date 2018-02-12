package space.zhupeng.arch.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import space.zhupeng.arch.R;

/**
 * CompoundDrawables支持矢量图
 *
 * @author zhupeng
 * @date 2018/2/6
 */

public class VectorCompatTextView extends AppCompatTextView {

    public VectorCompatTextView(Context context) {
        this(context, null, 0);
    }

    public VectorCompatTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VectorCompatTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VectorCompatTextView, defStyleAttr, 0);
        final int[] ids = new int[4];
        ids[0] = a.getResourceId(R.styleable.VectorCompatTextView_drawableLeftCompat, -1);
        ids[1] = a.getResourceId(R.styleable.VectorCompatTextView_drawableTopCompat, -1);
        ids[2] = a.getResourceId(R.styleable.VectorCompatTextView_drawableRightCompat, -1);
        ids[3] = a.getResourceId(R.styleable.VectorCompatTextView_drawableBottomCompat, -1);
        a.recycle();

        final int len = ids.length;
        final Drawable[] drawables = new Drawable[len];
        for (int i = 0; i < len; i++) {
            if (ids[i] != -1) {
                drawables[i] = AppCompatResources.getDrawable(context, ids[i]);
            }
        }

        setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawables[2], drawables[3]);
    }
}
