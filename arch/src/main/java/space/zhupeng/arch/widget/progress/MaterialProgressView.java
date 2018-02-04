package space.zhupeng.arch.widget.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import space.zhupeng.arch.R;

/**
 * MD风格的ProgressBar
 */

public class MaterialProgressView extends View implements ThemeManager.OnThemeChangedListener {

    public static final long FRAME_DURATION = 1000 / 60;

    protected int mStyleId;
    protected int mCurrentStyle = ThemeManager.THEME_UNDEFINED;

    private boolean mAutostart = false;
    private boolean mCircular = true;
    private int mProgressId;

    public static final int MODE_DETERMINATE = 0;
    public static final int MODE_INDETERMINATE = 1;
    public static final int MODE_BUFFER = 2;
    public static final int MODE_QUERY = 3;

    private Drawable mProgressDrawable;

    public MaterialProgressView(Context context) {
        super(context);

        init(context, null, 0, 0);
    }

    public MaterialProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs, 0, 0);
    }

    public MaterialProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr, 0);
    }

    protected void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        applyStyle(context, attrs, defStyleAttr, defStyleRes);
        if (!isInEditMode())
            mStyleId = ThemeManager.getStyleId(context, attrs, defStyleAttr, defStyleRes);
    }

    private boolean needCreateProgress(boolean circular) {
        if (mProgressDrawable == null)
            return true;

        if (circular)
            return !(mProgressDrawable instanceof CircularProgressDrawable);
        else
            return !(mProgressDrawable instanceof LinearProgressDrawable);
    }

    protected void applyStyle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ProgressView, defStyleAttr, defStyleRes);

        int leftPadding = -1;
        int topPadding = -1;
        int rightPadding = -1;
        int bottomPadding = -1;
        int startPadding = Integer.MIN_VALUE;
        int endPadding = Integer.MIN_VALUE;
        int padding = -1;

        boolean startPaddingDefined = false;
        boolean endPaddingDefined = false;
        boolean leftPaddingDefined = false;
        boolean rightPaddingDefined = false;

        int progressId = 0;
        int progressMode = -1;
        float progress = -1;
        float secondaryProgress = -1;

        for (int i = 0, count = a.getIndexCount(); i < count; i++) {
            int attr = a.getIndex(i);

            if (attr == R.styleable.ProgressView_android_background) {
                Drawable bg = a.getDrawable(attr);
                ViewCompat.setBackground(this, bg);
            } else if (attr == R.styleable.ProgressView_android_backgroundTint) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    setBackgroundTintList(a.getColorStateList(attr));
            } else if (attr == R.styleable.ProgressView_android_backgroundTintMode) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int value = a.getInt(attr, 3);
                    switch (value) {
                        case 3:
                            setBackgroundTintMode(PorterDuff.Mode.SRC_OVER);
                            break;
                        case 5:
                            setBackgroundTintMode(PorterDuff.Mode.SRC_IN);
                            break;
                        case 9:
                            setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
                            break;
                        case 14:
                            setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
                            break;
                        case 15:
                            setBackgroundTintMode(PorterDuff.Mode.SCREEN);
                            break;
                        case 16:
                            setBackgroundTintMode(PorterDuff.Mode.ADD);
                            break;
                    }
                }
            } else if (attr == R.styleable.ProgressView_android_elevation) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    setElevation(a.getDimensionPixelOffset(attr, 0));
            } else if (attr == R.styleable.ProgressView_android_padding) {
                padding = a.getDimensionPixelSize(attr, -1);
                leftPaddingDefined = true;
                rightPaddingDefined = true;
            } else if (attr == R.styleable.ProgressView_android_paddingLeft) {
                leftPadding = a.getDimensionPixelSize(attr, -1);
                leftPaddingDefined = true;
            } else if (attr == R.styleable.ProgressView_android_paddingTop)
                topPadding = a.getDimensionPixelSize(attr, -1);
            else if (attr == R.styleable.ProgressView_android_paddingRight) {
                rightPadding = a.getDimensionPixelSize(attr, -1);
                rightPaddingDefined = true;
            } else if (attr == R.styleable.ProgressView_android_paddingBottom)
                bottomPadding = a.getDimensionPixelSize(attr, -1);
            else if (attr == R.styleable.ProgressView_android_paddingStart) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    startPadding = a.getDimensionPixelSize(attr, Integer.MIN_VALUE);
                    startPaddingDefined = (startPadding != Integer.MIN_VALUE);
                }
            } else if (attr == R.styleable.ProgressView_android_paddingEnd) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    endPadding = a.getDimensionPixelSize(attr, Integer.MIN_VALUE);
                    endPaddingDefined = (endPadding != Integer.MIN_VALUE);
                }
            } else if (attr == R.styleable.ProgressView_android_minHeight)
                setMinimumHeight(a.getDimensionPixelSize(attr, 0));
            else if (attr == R.styleable.ProgressView_android_minWidth)
                setMinimumWidth(a.getDimensionPixelSize(attr, 0));
            else if (attr == R.styleable.ProgressView_android_soundEffectsEnabled)
                setSoundEffectsEnabled(a.getBoolean(attr, true));
            else if (attr == R.styleable.ProgressView_android_visibility) {
                int value = a.getInteger(attr, 0);
                switch (value) {
                    case 0:
                        setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        setVisibility(View.GONE);
                        break;
                }
            } else if (attr == R.styleable.ProgressView_pv_autostart)
                mAutostart = a.getBoolean(attr, false);
            else if (attr == R.styleable.ProgressView_pv_circular)
                mCircular = a.getBoolean(attr, true);
            else if (attr == R.styleable.ProgressView_pv_progressStyle)
                progressId = a.getResourceId(attr, 0);
            else if (attr == R.styleable.ProgressView_pv_progressMode)
                progressMode = a.getInteger(attr, 0);
            else if (attr == R.styleable.ProgressView_pv_progress)
                progress = a.getFloat(attr, 0);
            else if (attr == R.styleable.ProgressView_pv_secondaryProgress)
                secondaryProgress = a.getFloat(attr, 0);
        }

        a.recycle();

        if (padding >= 0)
            setPadding(padding, padding, padding, padding);
        else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (startPaddingDefined)
                leftPadding = startPadding;
            if (endPaddingDefined)
                rightPadding = endPadding;

            setPadding(leftPadding >= 0 ? leftPadding : getPaddingLeft(),
                    topPadding >= 0 ? topPadding : getPaddingTop(),
                    rightPadding >= 0 ? rightPadding : getPaddingRight(),
                    bottomPadding >= 0 ? bottomPadding : getPaddingBottom());
        } else {
            if (leftPaddingDefined || rightPaddingDefined)
                setPadding(leftPaddingDefined ? leftPadding : getPaddingLeft(),
                        topPadding >= 0 ? topPadding : getPaddingTop(),
                        rightPaddingDefined ? rightPadding : getPaddingRight(),
                        bottomPadding >= 0 ? bottomPadding : getPaddingBottom());

            if (startPaddingDefined || endPaddingDefined)
                setPaddingRelative(startPaddingDefined ? startPadding : getPaddingStart(),
                        topPadding >= 0 ? topPadding : getPaddingTop(),
                        endPaddingDefined ? endPadding : getPaddingEnd(),
                        bottomPadding >= 0 ? bottomPadding : getPaddingBottom());
        }

        boolean needStart = false;

        if (needCreateProgress(mCircular)) {
            mProgressId = progressId;
            if (mProgressId == 0)
                mProgressId = mCircular ? R.style.Material_Drawable_CircularProgress : R.style.Material_Drawable_LinearProgress;

            needStart = mProgressDrawable != null && ((Animatable) mProgressDrawable).isRunning();
            mProgressDrawable = mCircular ? new CircularProgressDrawable.Builder(context, mProgressId).build() : new LinearProgressDrawable.Builder(context, mProgressId).build();

            ViewCompat.setBackground(this, mProgressDrawable);
        } else if (mProgressId != progressId) {
            mProgressId = progressId;
            if (mProgressDrawable instanceof CircularProgressDrawable)
                ((CircularProgressDrawable) mProgressDrawable).applyStyle(context, mProgressId);
            else
                ((LinearProgressDrawable) mProgressDrawable).applyStyle(context, mProgressId);
        }

        if (progressMode >= 0) {
            if (mProgressDrawable instanceof CircularProgressDrawable)
                ((CircularProgressDrawable) mProgressDrawable).setProgressMode(progressMode);
            else
                ((LinearProgressDrawable) mProgressDrawable).setProgressMode(progressMode);
        }

        if (progress >= 0)
            setProgress(progress);

        if (secondaryProgress >= 0)
            setSecondaryProgress(secondaryProgress);

        if (needStart)
            start();
    }

    @Override
    public void onThemeChanged(ThemeManager.OnThemeChangedEvent event) {
        int style = ThemeManager.getInstance().getCurrentStyle(mStyleId);
        if (mCurrentStyle != style) {
            mCurrentStyle = style;
            applyStyle(getContext(), null, 0, mCurrentStyle);
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if (changedView != this)
            return;

        if (mAutostart) {
            if (visibility == GONE || visibility == INVISIBLE)
                stop();
            else
                start();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (getVisibility() == View.VISIBLE && mAutostart)
            start();
        if (mStyleId != 0) {
            ThemeManager.getInstance().registerOnThemeChangedListener(this);
            onThemeChanged(null);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mAutostart)
            stop();

        super.onDetachedFromWindow();
        if (mStyleId != 0)
            ThemeManager.getInstance().unregisterOnThemeChangedListener(this);
    }

    public int getProgressMode() {
        if (mCircular)
            return ((CircularProgressDrawable) mProgressDrawable).getProgressMode();
        else
            return ((LinearProgressDrawable) mProgressDrawable).getProgressMode();
    }

    /**
     * @return The current progress of this view in [0..1] range.
     */
    public float getProgress() {
        if (mCircular)
            return ((CircularProgressDrawable) mProgressDrawable).getProgress();
        else
            return ((LinearProgressDrawable) mProgressDrawable).getProgress();
    }

    /**
     * @return The current secondary progress of this view in [0..1] range.
     */
    public float getSecondaryProgress() {
        if (mCircular)
            return ((CircularProgressDrawable) mProgressDrawable).getSecondaryProgress();
        else
            return ((LinearProgressDrawable) mProgressDrawable).getSecondaryProgress();
    }

    /**
     * Set the current progress of this view.
     *
     * @param percent The progress value in [0..1] range.
     */
    public void setProgress(float percent) {
        if (mCircular)
            ((CircularProgressDrawable) mProgressDrawable).setProgress(percent);
        else
            ((LinearProgressDrawable) mProgressDrawable).setProgress(percent);
    }

    /**
     * Set the current secondary progress of this view.
     *
     * @param percent The progress value in [0..1] range.
     */
    public void setSecondaryProgress(float percent) {
        if (mCircular)
            ((CircularProgressDrawable) mProgressDrawable).setSecondaryProgress(percent);
        else
            ((LinearProgressDrawable) mProgressDrawable).setSecondaryProgress(percent);
    }

    /**
     * Start showing progress.
     */
    public void start() {
        if (mProgressDrawable != null)
            ((Animatable) mProgressDrawable).start();
    }

    /**
     * Stop showing progress.
     */
    public void stop() {
        if (mProgressDrawable != null)
            ((Animatable) mProgressDrawable).stop();
    }
}