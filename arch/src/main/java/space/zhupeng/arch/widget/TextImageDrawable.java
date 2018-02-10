package space.zhupeng.arch.widget;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.ImageView;

import java.util.HashSet;
import java.util.Set;

/**
 * @author zhupeng
 * @date 2016/12/11
 */

@SuppressWarnings("UnusedDeclaration")
public class TextImageDrawable extends Drawable {
    public static final int DEFAULT_BORDER_COLOR = Color.BLACK;
    public static final int DEFAULT_TEXT_COLOR = Color.BLACK;

    private final RectF mBounds = new RectF();
    private final RectF mDrawableRect = new RectF();
    private final RectF mBitmapRect = new RectF();
    private final Bitmap mBitmap;
    private final Paint mBitmapPaint;
    private final int mBitmapWidth;
    private final int mBitmapHeight;
    private final RectF mBorderRect = new RectF();
    private final Paint mBorderPaint;
    private final Matrix mShaderMatrix = new Matrix();
    private final RectF mSquareCornersRect = new RectF();
    private final Paint mTextPaint;

    private final int mTypeBorderFor;

    private Shader.TileMode mTileModeX = Shader.TileMode.CLAMP;
    private Shader.TileMode mTileModeY = Shader.TileMode.CLAMP;
    private boolean mRebuildShader = true;

    private float mCornerRadius = 0f;
    // [ topLeft, topRight, bottomLeft, bottomRight ]
    private final boolean[] mCornersRounded = new boolean[]{true, true, true, true};

    private int mShape = TextImageView.Shape.RECTANGLE;
    private float mBorderWidth = 0;
    private ColorStateList mBorderColor = ColorStateList.valueOf(DEFAULT_BORDER_COLOR);
    private int mBorderMode = TextImageView.BorderMode.ALWAYS;
    private ImageView.ScaleType mScaleType = ImageView.ScaleType.FIT_CENTER;

    private String mText;
    private ColorStateList mTextColor = ColorStateList.valueOf(TextImageDrawable.DEFAULT_TEXT_COLOR);
    private int mTextSize = TextImageView.DEFAULT_TEXT_SIZE;
    private Typeface mTypeface = Typeface.DEFAULT;
    private int mTextStyle = Typeface.NORMAL;

    public TextImageDrawable(Bitmap bitmap, int type) {
        mBitmap = bitmap;
        mTypeBorderFor = type;

        mBitmapWidth = bitmap.getWidth();
        mBitmapHeight = bitmap.getHeight();
        mBitmapRect.set(0, 0, mBitmapWidth, mBitmapHeight);

        mBitmapPaint = new Paint();
        mBitmapPaint.setStyle(Paint.Style.FILL);
        mBitmapPaint.setAntiAlias(true);

        mBorderPaint = new Paint();
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor.getColorForState(getState(), DEFAULT_BORDER_COLOR));
        mBorderPaint.setStrokeWidth(mBorderWidth);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(mTextColor.getColorForState(getState(), DEFAULT_TEXT_COLOR));
    }

    public static TextImageDrawable fromBitmap(Bitmap bitmap, int type) {
        if (bitmap != null) {
            return new TextImageDrawable(bitmap, type);
        } else {
            return null;
        }
    }

    public static Drawable fromDrawable(Drawable drawable, int type) {
        if (drawable != null) {
            if (drawable instanceof TextImageDrawable) {
                // just return if it's already a RoundedDrawable
                return drawable;
            } else if (drawable instanceof LayerDrawable) {
                LayerDrawable ld = (LayerDrawable) drawable;
                int num = ld.getNumberOfLayers();

                // loop through layers to and change to RoundedDrawables if possible
                for (int i = 0; i < num; i++) {
                    Drawable d = ld.getDrawable(i);
                    ld.setDrawableByLayerId(ld.getId(i), fromDrawable(d, type));
                }
                return ld;
            }

            // try to get a bitmap from the drawable and
            Bitmap bm = drawableToBitmap(drawable);
            if (bm != null) {
                return new TextImageDrawable(bm, type);
            }
        }
        return drawable;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap;
        int width = Math.max(drawable.getIntrinsicWidth(), 2);
        int height = Math.max(drawable.getIntrinsicHeight(), 2);
        try {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        } catch (Exception e) {
            e.printStackTrace();
            bitmap = null;
        }

        return bitmap;
    }

    public Bitmap getSourceBitmap() {
        return mBitmap;
    }

    @Override
    public boolean isStateful() {
        return mBorderColor.isStateful() || mTextColor.isStateful();
    }

    @Override
    protected boolean onStateChange(int[] state) {
        int newBorderColor = mBorderColor.getColorForState(state, 0);
        int newTextColor = mTextColor.getColorForState(state, 0);
        if (mBorderPaint.getColor() == newBorderColor && mTextPaint.getColor() == newTextColor) {
            return super.onStateChange(state);
        } else {
            if (mBorderPaint.getColor() != newBorderColor) {
                mBorderPaint.setColor(newBorderColor);
            }

            if (mTextPaint.getColor() != newTextColor) {
                mTextPaint.setColor(newTextColor);
            }
            return true;
        }
    }

    private void updateShaderMatrix() {
        float scale;
        float dx;
        float dy;

        switch (mScaleType) {
            case CENTER:
                mBorderRect.set(mBounds);
                if (hasBorder()) {
                    mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2);
                }

                mShaderMatrix.reset();
                mShaderMatrix.setTranslate((int) ((mBorderRect.width() - mBitmapWidth) * 0.5f + 0.5f),
                        (int) ((mBorderRect.height() - mBitmapHeight) * 0.5f + 0.5f));
                break;

            case CENTER_CROP:
                mBorderRect.set(mBounds);
                if (hasBorder()) {
                    mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2);
                }

                mShaderMatrix.reset();

                dx = 0;
                dy = 0;

                if (mBitmapWidth * mBorderRect.height() > mBorderRect.width() * mBitmapHeight) {
                    scale = mBorderRect.height() / (float) mBitmapHeight;
                    dx = (mBorderRect.width() - mBitmapWidth * scale) * 0.5f;
                } else {
                    scale = mBorderRect.width() / (float) mBitmapWidth;
                    dy = (mBorderRect.height() - mBitmapHeight * scale) * 0.5f;
                }

                mShaderMatrix.setScale(scale, scale);
                mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderWidth / 2,
                        (int) (dy + 0.5f) + mBorderWidth / 2);
                break;

            case CENTER_INSIDE:
                mShaderMatrix.reset();

                if (mBitmapWidth <= mBounds.width() && mBitmapHeight <= mBounds.height()) {
                    scale = 1.0f;
                } else {
                    scale = Math.min(mBounds.width() / (float) mBitmapWidth,
                            mBounds.height() / (float) mBitmapHeight);
                }

                dx = (int) ((mBounds.width() - mBitmapWidth * scale) * 0.5f + 0.5f);
                dy = (int) ((mBounds.height() - mBitmapHeight * scale) * 0.5f + 0.5f);

                mShaderMatrix.setScale(scale, scale);
                mShaderMatrix.postTranslate(dx, dy);

                mBorderRect.set(mBitmapRect);
                mShaderMatrix.mapRect(mBorderRect);
                if (hasBorder()) {
                    mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2);
                }
                mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect, Matrix.ScaleToFit.FILL);
                break;

            default:
            case FIT_CENTER:
                mBorderRect.set(mBitmapRect);
                mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.CENTER);
                mShaderMatrix.mapRect(mBorderRect);
                if (hasBorder()) {
                    mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2);
                }
                mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect, Matrix.ScaleToFit.FILL);
                break;

            case FIT_END:
                mBorderRect.set(mBitmapRect);
                mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.END);
                mShaderMatrix.mapRect(mBorderRect);
                if (hasBorder()) {
                    mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2);
                }
                mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect, Matrix.ScaleToFit.FILL);
                break;

            case FIT_START:
                mBorderRect.set(mBitmapRect);
                mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.START);
                mShaderMatrix.mapRect(mBorderRect);
                if (hasBorder()) {
                    mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2);
                }
                mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect, Matrix.ScaleToFit.FILL);
                break;

            case FIT_XY:
                mBorderRect.set(mBounds);
                if (hasBorder()) {
                    mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2);
                }
                mShaderMatrix.reset();
                mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect, Matrix.ScaleToFit.FILL);
                break;
        }

        mDrawableRect.set(mBorderRect);
        mRebuildShader = true;
    }

    @Override
    protected void onBoundsChange(@NonNull Rect bounds) {
        super.onBoundsChange(bounds);

        mBounds.set(bounds);

        updateShaderMatrix();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (mRebuildShader) {
            BitmapShader bitmapShader = new BitmapShader(mBitmap, mTileModeX, mTileModeY);
            if (mTileModeX == Shader.TileMode.CLAMP && mTileModeY == Shader.TileMode.CLAMP) {
                bitmapShader.setLocalMatrix(mShaderMatrix);
            }
            mBitmapPaint.setShader(bitmapShader);
            mRebuildShader = false;
        }

        if (mShape == TextImageView.Shape.OVAL) {
            if (mBorderMode != TextImageView.BorderMode.NONE && hasBorder() && mBorderWidth > 0) {
                canvas.drawOval(mDrawableRect, mBitmapPaint);
                canvas.drawOval(mBorderRect, mBorderPaint);
            } else {
                canvas.drawOval(mDrawableRect, mBitmapPaint);
            }
        } else {
            if (any(mCornersRounded)) {
                float radius = mCornerRadius;
                if (mBorderMode != TextImageView.BorderMode.NONE && hasBorder() && mBorderWidth > 0) {
                    canvas.drawRoundRect(mDrawableRect, radius, radius, mBitmapPaint);
                    canvas.drawRoundRect(mBorderRect, radius, radius, mBorderPaint);
                    redrawBitmapForSquareCorners(canvas);
                    redrawBorderForSquareCorners(canvas);
                } else {
                    canvas.drawRoundRect(mDrawableRect, radius, radius, mBitmapPaint);
                    redrawBitmapForSquareCorners(canvas);
                }
            } else {
                canvas.drawRect(mDrawableRect, mBitmapPaint);
                if (mBorderMode != TextImageView.BorderMode.NONE && hasBorder() && mBorderWidth > 0) {
                    canvas.drawRect(mBorderRect, mBorderPaint);
                }
            }
        }

        if (mTypeBorderFor == TextImageView.BorderMode.FOR_TEXT && !TextUtils.isEmpty(mText)) {
            int count = canvas.save();
            canvas.translate(mBounds.left, mBounds.top);
            mTextPaint.setTextSize(mTextSize);
            canvas.drawText(mText, mBounds.width() / 2, mBounds.height() / 2 - ((mTextPaint.descent() + mTextPaint.ascent()) / 2), mTextPaint);
            canvas.restoreToCount(count);
        }
    }

    private void redrawBitmapForSquareCorners(Canvas canvas) {
        if (all(mCornersRounded)) {
            // no square corners
            return;
        }

        if (mCornerRadius == 0) {
            return; // no round corners
        }

        float left = mDrawableRect.left;
        float top = mDrawableRect.top;
        float right = left + mDrawableRect.width();
        float bottom = top + mDrawableRect.height();
        float radius = mCornerRadius;

        if (!mCornersRounded[TextImageView.Corner.TOP_LEFT]) {
            mSquareCornersRect.set(left, top, left + radius, top + radius);
            canvas.drawRect(mSquareCornersRect, mBitmapPaint);
        }

        if (!mCornersRounded[TextImageView.Corner.TOP_RIGHT]) {
            mSquareCornersRect.set(right - radius, top, right, radius);
            canvas.drawRect(mSquareCornersRect, mBitmapPaint);
        }

        if (!mCornersRounded[TextImageView.Corner.BOTTOM_RIGHT]) {
            mSquareCornersRect.set(right - radius, bottom - radius, right, bottom);
            canvas.drawRect(mSquareCornersRect, mBitmapPaint);
        }

        if (!mCornersRounded[TextImageView.Corner.BOTTOM_LEFT]) {
            mSquareCornersRect.set(left, bottom - radius, left + radius, bottom);
            canvas.drawRect(mSquareCornersRect, mBitmapPaint);
        }
    }

    private void redrawBorderForSquareCorners(Canvas canvas) {
        if (all(mCornersRounded)) {
            // no square corners
            return;
        }

        if (mCornerRadius == 0) {
            return; // no round corners
        }

        float left = mDrawableRect.left;
        float top = mDrawableRect.top;
        float right = left + mDrawableRect.width();
        float bottom = top + mDrawableRect.height();
        float radius = mCornerRadius;
        float offset = mBorderWidth / 2;

        if (!mCornersRounded[TextImageView.Corner.TOP_LEFT]) {
            canvas.drawLine(left - offset, top, left + radius, top, mBorderPaint);
            canvas.drawLine(left, top - offset, left, top + radius, mBorderPaint);
        }

        if (!mCornersRounded[TextImageView.Corner.TOP_RIGHT]) {
            canvas.drawLine(right - radius - offset, top, right, top, mBorderPaint);
            canvas.drawLine(right, top - offset, right, top + radius, mBorderPaint);
        }

        if (!mCornersRounded[TextImageView.Corner.BOTTOM_RIGHT]) {
            canvas.drawLine(right - radius - offset, bottom, right + offset, bottom, mBorderPaint);
            canvas.drawLine(right, bottom - radius, right, bottom, mBorderPaint);
        }

        if (!mCornersRounded[TextImageView.Corner.BOTTOM_LEFT]) {
            canvas.drawLine(left - offset, bottom, left + radius, bottom, mBorderPaint);
            canvas.drawLine(left, bottom - radius, left, bottom, mBorderPaint);
        }
    }

    private boolean hasBorder() {
        return mTypeBorderFor == mBorderMode || mBorderMode == TextImageView.BorderMode.ALWAYS;
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getAlpha() {
        return mBitmapPaint.getAlpha();
    }

    @Override
    public void setAlpha(int alpha) {
        mBitmapPaint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public ColorFilter getColorFilter() {
        return mBitmapPaint.getColorFilter();
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mBitmapPaint.setColorFilter(cf);
        invalidateSelf();
    }

    @Override
    public void setDither(boolean dither) {
        mBitmapPaint.setDither(dither);
        invalidateSelf();
    }

    @Override
    public void setFilterBitmap(boolean filter) {
        mBitmapPaint.setFilterBitmap(filter);
        invalidateSelf();
    }

    @Override
    public int getIntrinsicWidth() {
        return mBitmapWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return mBitmapHeight;
    }

    public float getCornerRadius() {
        return mCornerRadius;
    }

    public float getCornerRadius(@TextImageView.Corner int corner) {
        return mCornersRounded[corner] ? mCornerRadius : 0f;
    }

    public TextImageDrawable setCornerRadius(float radius) {
        setCornerRadius(radius, radius, radius, radius);
        return this;
    }

    public TextImageDrawable setCornerRadius(@TextImageView.Corner int corner, float radius) {
        if (radius != 0 && mCornerRadius != 0 && mCornerRadius != radius) {
            throw new IllegalArgumentException("Multiple nonzero corner radii not yet supported.");
        }

        if (radius == 0) {
            if (only(corner, mCornersRounded)) {
                mCornerRadius = 0;
            }
            mCornersRounded[corner] = false;
        } else {
            if (mCornerRadius == 0) {
                mCornerRadius = radius;
            }
            mCornersRounded[corner] = true;
        }

        return this;
    }

    public TextImageDrawable setCornerRadius(float topLeft, float topRight, float bottomRight,
                                             float bottomLeft) {
        Set<Float> radiusSet = new HashSet<>(4);
        radiusSet.add(topLeft);
        radiusSet.add(topRight);
        radiusSet.add(bottomRight);
        radiusSet.add(bottomLeft);

        radiusSet.remove(0f);

        if (radiusSet.size() > 1) {
            throw new IllegalArgumentException("Multiple nonzero corner radii not yet supported.");
        }

        if (!radiusSet.isEmpty()) {
            float radius = radiusSet.iterator().next();
            if (Float.isInfinite(radius) || Float.isNaN(radius) || radius < 0) {
                throw new IllegalArgumentException("Invalid radius value: " + radius);
            }
            mCornerRadius = radius;
        } else {
            mCornerRadius = 0f;
        }

        mCornersRounded[TextImageView.Corner.TOP_LEFT] = topLeft > 0;
        mCornersRounded[TextImageView.Corner.TOP_RIGHT] = topRight > 0;
        mCornersRounded[TextImageView.Corner.BOTTOM_RIGHT] = bottomRight > 0;
        mCornersRounded[TextImageView.Corner.BOTTOM_LEFT] = bottomLeft > 0;
        return this;
    }

    public float getBorderWidth() {
        return mBorderWidth;
    }

    public TextImageDrawable setBorderWidth(float width) {
        mBorderWidth = width;
        mBorderPaint.setStrokeWidth(mBorderWidth);
        return this;
    }

    public int getBorderColor() {
        return mBorderColor.getDefaultColor();
    }

    public TextImageDrawable setBorderColor(@ColorInt int color) {
        return setBorderColor(ColorStateList.valueOf(color));
    }

    @TextImageView.BorderMode
    public int getBorderMode() {
        return mBorderMode;
    }

    public TextImageDrawable setBorderMode(@TextImageView.BorderMode int borderMode) {
        if (mBorderMode != borderMode) {
            mBorderMode = borderMode;
            updateShaderMatrix();
        }
        return this;
    }

    public ColorStateList getBorderColors() {
        return mBorderColor;
    }

    public TextImageDrawable setBorderColor(ColorStateList colors) {
        mBorderColor = colors != null ? colors : ColorStateList.valueOf(0);
        mBorderPaint.setColor(mBorderColor.getColorForState(getState(), DEFAULT_BORDER_COLOR));
        return this;
    }

    public String getText() {
        return mText;
    }

    public TextImageDrawable setText(String text) {
        mText = text;
        return this;
    }

    public ColorStateList getTextColor() {
        return mTextColor;
    }

    public TextImageDrawable setTextColor(@ColorInt int color) {
        return setTextColor(ColorStateList.valueOf(color));
    }

    public TextImageDrawable setTextColor(ColorStateList colors) {
        mTextColor = colors != null ? colors : ColorStateList.valueOf(0);
        mTextPaint.setColor(mTextColor.getColorForState(getState(), DEFAULT_TEXT_COLOR));
        return this;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public TextImageDrawable setTextSize(int textSize) {
        mTextSize = textSize;
        mTextPaint.setTextSize(mTextSize);
        return this;
    }

    public TextImageDrawable setFont(Typeface typeface, @IntRange(from = 0, to = 2) int textStyle) {
        mTypeface = typeface;
        mTextStyle = textStyle;
        mTextPaint.setTypeface(Typeface.create(typeface, textStyle));
        return this;
    }

    @TextImageView.Shape
    public int getShape() {
        return mShape;
    }

    public TextImageDrawable setShape(@TextImageView.Shape int shape) {
        mShape = shape;
        return this;
    }

    public ImageView.ScaleType getScaleType() {
        return mScaleType;
    }

    public TextImageDrawable setScaleType(ImageView.ScaleType scaleType) {
        if (scaleType == null) {
            scaleType = ImageView.ScaleType.FIT_CENTER;
        }
        if (mScaleType != scaleType) {
            mScaleType = scaleType;
            updateShaderMatrix();
        }
        return this;
    }

    public Shader.TileMode getTileModeX() {
        return mTileModeX;
    }

    public TextImageDrawable setTileModeX(Shader.TileMode tileModeX) {
        if (mTileModeX != tileModeX) {
            mTileModeX = tileModeX;
            mRebuildShader = true;
            invalidateSelf();
        }
        return this;
    }

    public Shader.TileMode getTileModeY() {
        return mTileModeY;
    }

    public TextImageDrawable setTileModeY(Shader.TileMode tileModeY) {
        if (mTileModeY != tileModeY) {
            mTileModeY = tileModeY;
            mRebuildShader = true;
            invalidateSelf();
        }
        return this;
    }

    private static boolean only(int index, boolean[] booleans) {
        for (int i = 0, len = booleans.length; i < len; i++) {
            if (booleans[i] != (i == index)) {
                return false;
            }
        }
        return true;
    }

    private static boolean any(boolean[] booleans) {
        for (boolean b : booleans) {
            if (b) {
                return true;
            }
        }
        return false;
    }

    private static boolean all(boolean[] booleans) {
        for (boolean b : booleans) {
            if (b) {
                return false;
            }
        }
        return true;
    }

    public Bitmap toBitmap() {
        return drawableToBitmap(this);
    }
}
