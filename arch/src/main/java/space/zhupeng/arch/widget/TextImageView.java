package space.zhupeng.arch.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import space.zhupeng.arch.R;

/**
 * 圆角ImageView
 *
 * @author zhupeng
 * @date 2016/12/11
 */

@SuppressWarnings("UnusedDeclaration")
public class TextImageView extends AppCompatImageView {

    // Constants for tile mode attributes
    private static final int TILE_MODE_UNDEFINED = -2;
    private static final int TILE_MODE_CLAMP = 0;
    private static final int TILE_MODE_REPEAT = 1;
    private static final int TILE_MODE_MIRROR = 2;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            Corner.TOP_LEFT, Corner.TOP_RIGHT,
            Corner.BOTTOM_LEFT, Corner.BOTTOM_RIGHT
    })
    public @interface Corner {
        int TOP_LEFT = 0;
        int TOP_RIGHT = 1;
        int BOTTOM_RIGHT = 2;
        int BOTTOM_LEFT = 3;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            Shape.OVAL, Shape.RECTANGLE
    })
    public @interface Shape {
        int RECTANGLE = 0;
        int OVAL = 1;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            BorderMode.NONE, BorderMode.FOR_TEXT,
            BorderMode.FOR_IMAGE, BorderMode.ALWAYS
    })
    public @interface BorderMode {
        int NONE = 0;
        int FOR_TEXT = 1;
        int FOR_IMAGE = 2;
        int ALWAYS = 3;
    }

    public static final float DEFAULT_RADIUS = 0f;
    public static final float DEFAULT_BORDER_WIDTH = 0f;
    public static final int DEFAULT_TEXT_SIZE = 15;
    public static final Shader.TileMode DEFAULT_TILE_MODE = Shader.TileMode.CLAMP;
    private static final ScaleType[] SCALE_TYPES = {
            ScaleType.MATRIX,
            ScaleType.FIT_XY,
            ScaleType.FIT_START,
            ScaleType.FIT_CENTER,
            ScaleType.FIT_END,
            ScaleType.CENTER,
            ScaleType.CENTER_CROP,
            ScaleType.CENTER_INSIDE
    };

    private final float[] mCornerRadius =
            new float[]{DEFAULT_RADIUS, DEFAULT_RADIUS, DEFAULT_RADIUS, DEFAULT_RADIUS};

    private Drawable mBackgroundDrawable;
    private ColorStateList mBorderColor = ColorStateList.valueOf(TextImageDrawable.DEFAULT_BORDER_COLOR);
    private float mBorderWidth = DEFAULT_BORDER_WIDTH;
    private int mBorderMode = BorderMode.ALWAYS;
    private ColorFilter mColorFilter = null;
    private boolean mColorMod = false;
    private int mShape = Shape.RECTANGLE;
    private Drawable mDrawable;
    private boolean mHasColorFilter = false;
    private int mResource;
    private int mBackgroundResource;
    private ScaleType mScaleType;
    private Shader.TileMode mTileModeX = DEFAULT_TILE_MODE;
    private Shader.TileMode mTileModeY = DEFAULT_TILE_MODE;

    private String mText;
    private ColorStateList mTextColor = ColorStateList.valueOf(TextImageDrawable.DEFAULT_TEXT_COLOR);
    private int mTextSize = DEFAULT_TEXT_SIZE;
    private Typeface mTypeface;
    private int mTextStyle = Typeface.NORMAL;

    public TextImageView(Context context) {
        super(context);
    }

    public TextImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextImageView, defStyle, 0);

        int index = a.getInt(R.styleable.TextImageView_android_scaleType, -1);
        if (index >= 0) {
            setScaleType(SCALE_TYPES[index]);
        } else {
            setScaleType(ScaleType.FIT_CENTER);
        }

        float cornerRadiusOverride =
                a.getDimensionPixelSize(R.styleable.TextImageView_android_radius, -1);

        mCornerRadius[Corner.TOP_LEFT] =
                a.getDimensionPixelSize(R.styleable.TextImageView_android_topLeftRadius, -1);
        mCornerRadius[Corner.TOP_RIGHT] =
                a.getDimensionPixelSize(R.styleable.TextImageView_android_topRightRadius, -1);
        mCornerRadius[Corner.BOTTOM_RIGHT] =
                a.getDimensionPixelSize(R.styleable.TextImageView_android_bottomRightRadius, -1);
        mCornerRadius[Corner.BOTTOM_LEFT] =
                a.getDimensionPixelSize(R.styleable.TextImageView_android_bottomLeftRadius, -1);

        boolean any = false;
        for (int i = 0, len = mCornerRadius.length; i < len; i++) {
            if (mCornerRadius[i] < 0) {
                mCornerRadius[i] = 0f;
            } else {
                any = true;
            }
        }

        if (!any) {
            if (cornerRadiusOverride < 0) {
                cornerRadiusOverride = DEFAULT_RADIUS;
            }
            for (int i = 0, len = mCornerRadius.length; i < len; i++) {
                mCornerRadius[i] = cornerRadiusOverride;
            }
        }

        mBorderWidth = a.getDimensionPixelSize(R.styleable.TextImageView_tiv_borderWidth, -1);
        if (mBorderWidth < 0) {
            mBorderWidth = DEFAULT_BORDER_WIDTH;
        }

        mBorderColor = a.getColorStateList(R.styleable.TextImageView_tiv_borderColor);
        if (mBorderColor == null) {
            mBorderColor = ColorStateList.valueOf(TextImageDrawable.DEFAULT_BORDER_COLOR);
        }

        mBorderMode = a.getInt(R.styleable.TextImageView_tiv_borderMode, BorderMode.ALWAYS);

        mShape = a.getInt(R.styleable.TextImageView_tiv_shape, Shape.RECTANGLE);

        final int tileMode = a.getInt(R.styleable.TextImageView_android_tileMode, TILE_MODE_UNDEFINED);
        if (tileMode != TILE_MODE_UNDEFINED) {
            setTileModeX(parseTileMode(tileMode));
            setTileModeY(parseTileMode(tileMode));
        }

        final int tileModeX =
                a.getInt(R.styleable.TextImageView_android_tileModeX, TILE_MODE_UNDEFINED);
        if (tileModeX != TILE_MODE_UNDEFINED) {
            setTileModeX(parseTileMode(tileModeX));
        }

        final int tileModeY =
                a.getInt(R.styleable.TextImageView_android_tileModeY, TILE_MODE_UNDEFINED);
        if (tileModeY != TILE_MODE_UNDEFINED) {
            setTileModeY(parseTileMode(tileModeY));
        }

        mText = a.getString(R.styleable.TextImageView_android_text);
        mTextColor = a.getColorStateList(R.styleable.TextImageView_android_textColor);
        if (mTextColor == null) {
            mTextColor = ColorStateList.valueOf(TextImageDrawable.DEFAULT_TEXT_COLOR);
        }
        mTextSize = a.getDimensionPixelSize(R.styleable.TextImageView_android_textSize, DEFAULT_TEXT_SIZE);
        mTextStyle = a.getInt(R.styleable.TextImageView_android_textStyle, Typeface.NORMAL);

        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(true);

        //noinspection deprecation
        super.setBackgroundDrawable(mBackgroundDrawable);

        a.recycle();
    }

    private static Shader.TileMode parseTileMode(int tileMode) {
        switch (tileMode) {
            case TILE_MODE_CLAMP:
                return Shader.TileMode.CLAMP;
            case TILE_MODE_REPEAT:
                return Shader.TileMode.REPEAT;
            case TILE_MODE_MIRROR:
                return Shader.TileMode.MIRROR;
            default:
                return null;
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }

    @Override
    public ScaleType getScaleType() {
        return mScaleType;
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        assert scaleType != null;

        if (mScaleType != scaleType) {
            mScaleType = scaleType;

            switch (scaleType) {
                case CENTER:
                case CENTER_CROP:
                case CENTER_INSIDE:
                case FIT_CENTER:
                case FIT_START:
                case FIT_END:
                case FIT_XY:
                    super.setScaleType(ScaleType.FIT_XY);
                    break;
                default:
                    super.setScaleType(scaleType);
                    break;
            }

            updateDrawableAttrs();
            updateBackgroundDrawableAttrs(false);
            invalidate();
        }
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        mResource = 0;
        mDrawable = TextImageDrawable.fromDrawable(drawable, BorderMode.FOR_IMAGE);
        updateDrawableAttrs();
        super.setImageDrawable(mDrawable);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        mResource = 0;
        mDrawable = TextImageDrawable.fromBitmap(bm, BorderMode.FOR_IMAGE);
        updateDrawableAttrs();
        super.setImageDrawable(mDrawable);
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        if (mResource != resId) {
            mResource = resId;
            mDrawable = resolveResource();
            updateDrawableAttrs();
            super.setImageDrawable(mDrawable);
        }
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        setImageDrawable(getDrawable());
    }

    private Drawable resolveResource() {
        Resources rs = getResources();
        if (rs == null) {
            return null;
        }

        Drawable d = null;

        if (mResource != 0) {
            try {
                d = rs.getDrawable(mResource);
            } catch (Exception e) {
                mResource = 0;
            }
        }
        return TextImageDrawable.fromDrawable(d, BorderMode.FOR_IMAGE);
    }

    @Override
    public void setBackground(Drawable background) {
        setBackgroundDrawable(background);
    }

    @Override
    public void setBackgroundResource(@DrawableRes int resId) {
        if (mBackgroundResource != resId) {
            mBackgroundResource = resId;
            mBackgroundDrawable = resolveBackgroundResource();
            setBackgroundDrawable(mBackgroundDrawable);
        }
    }

    @Override
    public void setBackgroundColor(int color) {
        mBackgroundDrawable = new ColorDrawable(color);
        setBackgroundDrawable(mBackgroundDrawable);
    }

    private Drawable resolveBackgroundResource() {
        Resources rs = getResources();
        if (rs == null) {
            return null;
        }

        Drawable d = null;

        if (mBackgroundResource != 0) {
            try {
                d = rs.getDrawable(mBackgroundResource);
            } catch (Exception e) {
                mBackgroundResource = 0;
            }
        }
        return TextImageDrawable.fromDrawable(d, BorderMode.FOR_TEXT);
    }

    private void updateDrawableAttrs() {
        updateAttrs(mDrawable, false, mScaleType);
    }

    private void updateBackgroundDrawableAttrs(boolean convert) {
        if (convert) {
            mBackgroundDrawable = TextImageDrawable.fromDrawable(mBackgroundDrawable, BorderMode.FOR_TEXT);
        }
        updateAttrs(mBackgroundDrawable, true, ScaleType.FIT_XY);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        if (mColorFilter != cf) {
            mColorFilter = cf;
            mHasColorFilter = true;
            mColorMod = true;
            applyColorMod();
            invalidate();
        }
    }

    private void applyColorMod() {
        if (mDrawable != null && mColorMod) {
            mDrawable = mDrawable.mutate();
            if (mHasColorFilter) {
                mDrawable.setColorFilter(mColorFilter);
            }
        }
    }

    private void updateAttrs(Drawable drawable, boolean drawText, ScaleType scaleType) {
        if (drawable == null) {
            return;
        }

        if (drawable instanceof TextImageDrawable) {
            TextImageDrawable tid = ((TextImageDrawable) drawable);
            tid.setScaleType(scaleType)
                    .setBorderWidth(mBorderWidth)
                    .setBorderColor(mBorderColor)
                    .setBorderMode(mBorderMode)
                    .setShape(mShape)
                    .setTileModeX(mTileModeX)
                    .setTileModeY(mTileModeY);

            if (drawText && !TextUtils.isEmpty(mText)) {
                tid.setText(mText)
                        .setTextColor(mTextColor)
                        .setTextSize(mTextSize)
                        .setFont(mTypeface, mTextStyle);
            }

            if (mCornerRadius != null) {
                ((TextImageDrawable) drawable).setCornerRadius(
                        mCornerRadius[Corner.TOP_LEFT],
                        mCornerRadius[Corner.TOP_RIGHT],
                        mCornerRadius[Corner.BOTTOM_RIGHT],
                        mCornerRadius[Corner.BOTTOM_LEFT]);
            }

            applyColorMod();
        } else if (drawable instanceof LayerDrawable) {
            // loop through layers to and set drawable attrs
            LayerDrawable ld = ((LayerDrawable) drawable);
            for (int i = 0, layers = ld.getNumberOfLayers(); i < layers; i++) {
                updateAttrs(ld.getDrawable(i), drawText, scaleType);
            }
        }
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        mBackgroundDrawable = background;
        updateBackgroundDrawableAttrs(true);
        //noinspection deprecation
        super.setBackgroundDrawable(mBackgroundDrawable);
    }

    public float getCornerRadius(@Corner int corner) {
        return mCornerRadius[corner];
    }

    public void setCornerRadius(@DimenRes int resId) {
        float radius = getResources().getDimension(resId);
        setCornerRadius(radius, radius, radius, radius);
    }

    public void setCornerRadius(@Corner int corner, @DimenRes int resId) {
        setCornerRadius(corner, getResources().getDimensionPixelSize(resId) * 1f);
    }

    public void setCornerRadius(float radius) {
        setCornerRadius(radius, radius, radius, radius);
    }

    public void setCornerRadius(@Corner int corner, float radius) {
        if (mCornerRadius[corner] == radius) {
            return;
        }
        mCornerRadius[corner] = radius;

        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    public void setCornerRadius(float topLeft, float topRight, float bottomLeft, float bottomRight) {
        if (mCornerRadius[Corner.TOP_LEFT] == topLeft
                && mCornerRadius[Corner.TOP_RIGHT] == topRight
                && mCornerRadius[Corner.BOTTOM_RIGHT] == bottomRight
                && mCornerRadius[Corner.BOTTOM_LEFT] == bottomLeft) {
            return;
        }

        mCornerRadius[Corner.TOP_LEFT] = topLeft;
        mCornerRadius[Corner.TOP_RIGHT] = topRight;
        mCornerRadius[Corner.BOTTOM_LEFT] = bottomLeft;
        mCornerRadius[Corner.BOTTOM_RIGHT] = bottomRight;

        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    public float getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(@DimenRes int resId) {
        setBorderWidth(getResources().getDimension(resId));
    }

    public void setBorderWidth(float width) {
        if (mBorderWidth == width) {
            return;
        }

        mBorderWidth = width;
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    @ColorInt
    public int getBorderColor() {
        return mBorderColor.getDefaultColor();
    }

    public void setBorderColor(@ColorInt int color) {
        setBorderColor(ColorStateList.valueOf(color));
    }

    public ColorStateList getBorderColors() {
        return mBorderColor;
    }

    public void setBorderColor(ColorStateList colors) {
        if (mBorderColor.equals(colors)) {
            return;
        }

        mBorderColor =
                (colors != null) ? colors : ColorStateList.valueOf(TextImageDrawable.DEFAULT_BORDER_COLOR);
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        if (mBorderWidth > 0) {
            invalidate();
        }
    }

    @BorderMode
    public int getBorderMode() {
        return mBorderMode;
    }

    public void setBorderMode(@BorderMode int borderMode) {
        mBorderMode = borderMode;
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    public ColorStateList getTextColor() {
        return mTextColor;
    }

    public void setTextColor(@ColorInt int color) {
        mTextColor = ColorStateList.valueOf(color);
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    public void setTextColor(ColorStateList color) {
        mTextColor = color;
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setTextSize(@DimenRes int resId) {
        mTextSize = getResources().getDimensionPixelSize(resId);
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    public void setTextSize(float textSize) {
        mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, getResources().getDisplayMetrics());
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    public void setFont(String fontFamily, @IntRange(from = 0, to = 2) int textStyle) {
        mTypeface = Typeface.createFromAsset(getContext().getAssets(), fontFamily);
        mTextStyle = textStyle;
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    @Shape
    public int getShape() {
        return mShape;
    }

    public void setShape(@Shape int shape) {
        mShape = shape;
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    public Shader.TileMode getTileModeX() {
        return mTileModeX;
    }

    public void setTileModeX(Shader.TileMode tileModeX) {
        if (this.mTileModeX == tileModeX) {
            return;
        }

        this.mTileModeX = tileModeX;
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    public Shader.TileMode getTileModeY() {
        return mTileModeY;
    }

    public void setTileModeY(Shader.TileMode tileModeY) {
        if (this.mTileModeY == tileModeY) {
            return;
        }

        this.mTileModeY = tileModeY;
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }
}
