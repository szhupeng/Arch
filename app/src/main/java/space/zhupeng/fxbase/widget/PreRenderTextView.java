package space.zhupeng.fxbase.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;

import space.zhupeng.fxbase.R;

/**
 * 用于界面数据未加载之前展示条形文本轮廓，数据加载后展示文本数据
 *
 * @author zhupeng
 * @date 2017/9/3
 */

public class PreRenderTextView extends AppCompatTextView {

    public static final int DEFAULT_COLOR = Color.parseColor("#f3f6f3");
    public static final int DEFAULT_WIDTH = 100;
    public static final int DEFAULT_HEIGHT = 6;

    private boolean isPreRender = true;
    private int mRenderColor;
    private Paint mPreRenderPaint;

    public PreRenderTextView(Context context) {
        this(context, null);
    }

    public PreRenderTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PreRenderTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PreRenderTextView, defStyleAttr, 0);
        mRenderColor = a.getColor(R.styleable.PreRenderTextView_renderColor, DEFAULT_COLOR);
        a.recycle();

        mPreRenderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPreRenderPaint.setColor(mRenderColor);
        mPreRenderPaint.setStyle(Paint.Style.FILL);
    }

    public void setIsPreRender(boolean isPreRender) {
        this.isPreRender = isPreRender;
        invalidate();
    }

    public void setPreRenderColor(int color) {
        this.mRenderColor = color;
        mPreRenderPaint.setColor(this.mRenderColor);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isPreRender && TextUtils.isEmpty(getText())) {
            canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPreRenderPaint);
            isPreRender = false;
        } else {
            super.onDraw(canvas);
        }
    }
}
