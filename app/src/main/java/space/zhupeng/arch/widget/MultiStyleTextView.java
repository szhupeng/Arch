package space.zhupeng.arch.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.MaskFilterSpan;
import android.text.style.QuoteSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.view.View;

/**
 * 显示多种风格的文本控件
 *
 * @author zhupeng
 * @date 2017/8/19
 */

public class MultiStyleTextView extends AppCompatTextView {

    public interface OnSegmentClickListener {
        void onClick(View view, CharSequence text);
    }

    private final SpannableStringBuilder mSpannableBuilder;
    private Spannable mEndSpannable;
    private final int mDefaultColor;

    public MultiStyleTextView(Context context) {
        this(context, null);
    }

    public MultiStyleTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiStyleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mDefaultColor = getTextColors().getDefaultColor();

        mSpannableBuilder = new SpannableStringBuilder();

        if (!TextUtils.isEmpty(getText())) {
            mEndSpannable = new SpannableString(getText());
        }
    }

    /**
     * 添加文本片段
     *
     * @param text
     * @return
     */
    public MultiStyleTextView segment(final CharSequence text) {
        segment(text, 0f, 0, null);
        return this;
    }

    /**
     * 添加特定字体大小的文本片段
     *
     * @param text
     * @param textSize 字体大小，单位为SP
     * @return
     */
    public MultiStyleTextView segment(final CharSequence text, final float textSize) {
        segment(text, textSize, 0, null);
        return this;
    }

    /**
     * 添加特定字体大小并且可点击的文本片段
     *
     * @param text
     * @param textSize 字体大小，单位为SP
     * @param listener 点击事件监听
     * @return
     */
    public MultiStyleTextView segment(final CharSequence text, final float textSize, final OnSegmentClickListener listener) {
        segment(text, textSize, 0, listener);
        return this;
    }

    /**
     * 添加特定颜色的文本片段
     *
     * @param text
     * @param textColor 字体颜色
     * @return
     */
    public MultiStyleTextView segment(final CharSequence text, final int textColor) {
        segment(text, 0f, textColor, null);
        return this;
    }

    /**
     * 添加特定字体大小和颜色的文本片段
     *
     * @param text
     * @param textSize  字体大小
     * @param textColor 字体颜色
     * @return
     */
    public MultiStyleTextView segment(final CharSequence text, final float textSize, final int textColor) {
        segment(text, textSize, textColor, null);
        return this;
    }

    /**
     * 添加特定颜色并且可点击的文本片段
     *
     * @param text
     * @param textColor 字体颜色
     * @return
     */
    public MultiStyleTextView segment(final CharSequence text, final int textColor, final OnSegmentClickListener listener) {
        segment(text, 0f, textColor, listener);
        return this;
    }

    /**
     * 添加特定字体大小和颜色并且可点击的文本片段
     *
     * @param text
     * @param textSize  字体大小
     * @param textColor 字体颜色
     * @param listener
     * @return
     */
    public MultiStyleTextView segment(final CharSequence text, final float textSize, int textColor, final OnSegmentClickListener listener) {
        if (TextUtils.isEmpty(text)) return this;

        mEndSpannable = new SpannableString(text);
        if ((int) textSize > 0) {
            mEndSpannable.setSpan(new AbsoluteSizeSpan((int) textSize), 0, mEndSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if (textColor != 0) {
            mEndSpannable.setSpan(new ForegroundColorSpan(textColor), 0, mEndSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            textColor = mDefaultColor;
        }

        if (listener != null) {
            mEndSpannable.setSpan(new ClickSpan(text, textColor, listener), 0, mEndSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        mSpannableBuilder.append(mEndSpannable);
        return this;
    }

    /**
     * 追加背景色样式
     *
     * @param color 背景颜色
     * @return
     */
    public MultiStyleTextView appendBackgroundColor(final int color) {
        if (color <= 0 || null == mEndSpannable) return this;

        mEndSpannable.setSpan(new BackgroundColorSpan(color), 0, mEndSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 追加引用线的颜色样式
     *
     * @param color 引用线颜色
     * @return
     */
    public MultiStyleTextView appendQuoteColor(final int color) {
        if (color <= 0 || null == mEndSpannable) return this;

        mEndSpannable.setSpan(new QuoteSpan(color), 0, mEndSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 追加缩进样式
     *
     * @param first 首行缩进
     * @param rest  剩余行缩进
     * @return
     */
    public MultiStyleTextView appendLeadingMargin(int first, int rest) {
        if (null == mEndSpannable) return this;

        if (first < 0) first = 0;
        if (rest < 0) rest = 0;

        mEndSpannable.setSpan(new LeadingMarginSpan.Standard(first, rest), 0, mEndSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 追加超链接
     *
     * @param url 链接地址
     * @return
     */
    public MultiStyleTextView appendUrl(final String url) {
        if (TextUtils.isEmpty(url) || null == mEndSpannable) return this;

        setMovementMethod(LinkMovementMethod.getInstance());

        mEndSpannable.setSpan(new URLSpan(url), 0, mEndSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 追加列表标记样式
     *
     * @param padding 列表标记和文字间距离
     * @param color   列表标记的颜色
     * @return
     */
    public MultiStyleTextView appendBullet(final int padding, int color) {
        if (null == mEndSpannable) return this;

        if (0 == color) color = mDefaultColor;

        mEndSpannable.setSpan(new BulletSpan(padding, color), 0, mEndSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 追加删除线样式
     *
     * @return
     */
    public MultiStyleTextView appendStrikeThrough() {
        if (null == mEndSpannable) return this;

        mEndSpannable.setSpan(new StrikethroughSpan(), 0, mEndSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 追加下划线样式
     *
     * @return
     */
    public MultiStyleTextView appendUnderline() {
        if (null == mEndSpannable) return this;

        mEndSpannable.setSpan(new UnderlineSpan(), 0, mEndSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 追加上标样式
     *
     * @return
     */
    public MultiStyleTextView appendSuperscript() {
        if (null == mEndSpannable) return this;

        mEndSpannable.setSpan(new SuperscriptSpan(), 0, mEndSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 追加下标样式
     *
     * @return
     */
    public MultiStyleTextView appendSubscript() {
        if (null == mEndSpannable) return this;

        mEndSpannable.setSpan(new SubscriptSpan(), 0, mEndSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 追加粗体样式
     *
     * @return
     */
    public MultiStyleTextView appendBoldStyle() {
        if (null == mEndSpannable) return this;

        mEndSpannable.setSpan(new StyleSpan(Typeface.BOLD), 0, mEndSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 追加斜体样式
     *
     * @return
     */
    public MultiStyleTextView appendItalicStyle() {
        if (null == mEndSpannable) return this;

        mEndSpannable.setSpan(new StyleSpan(Typeface.ITALIC), 0, mEndSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 追加字体样式
     *
     * @param fontFamily
     * @return
     */
    public MultiStyleTextView appendFontFamily(final String fontFamily) {
        if (TextUtils.isEmpty(fontFamily) || null == mEndSpannable) return this;

        mEndSpannable.setSpan(new TypefaceSpan(fontFamily), 0, mEndSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 追加图片
     *
     * @param bitmap 位图
     * @param start  插入图片的位置
     * @return
     */
    public MultiStyleTextView appendImage(final Bitmap bitmap, int start) {
        if (null == bitmap || null == mEndSpannable) return this;

        if (start < 0) start = 0;
        if (start > mEndSpannable.length()) start = mEndSpannable.length();

        mEndSpannable.setSpan(new ImageSpan(getContext(), bitmap), start, start + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 追加图片
     *
     * @param drawable 图片
     * @param start    插入图片的位置
     * @return
     */
    public MultiStyleTextView appendImage(final Drawable drawable, int start) {
        if (null == drawable || null == mEndSpannable) return this;

        if (start < 0) start = 0;
        if (start > mEndSpannable.length()) start = mEndSpannable.length();

        mEndSpannable.setSpan(new ImageSpan(drawable), start, start + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 追加图片
     *
     * @param resId 图片资源id
     * @param start 插入图片的位置
     * @return
     */
    public MultiStyleTextView appendImage(@DrawableRes final int resId, int start) {
        if (null == mEndSpannable) return this;

        if (start < 0) start = 0;
        if (start > mEndSpannable.length()) start = mEndSpannable.length();

        mEndSpannable.setSpan(new ImageSpan(getContext(), resId), start, start + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 追加图片
     *
     * @param uri
     * @param start 插入图片的位置
     * @return
     */
    public MultiStyleTextView appendImage(final Uri uri, int start) {
        if (null == mEndSpannable) return this;

        mEndSpannable.setSpan(new ImageSpan(getContext(), uri), start, start + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 追加模糊样式
     *
     * @param radius 模糊半径（需大于0）
     * @param style  模糊样式<ul>
     *               <li>{@link BlurMaskFilter.Blur#NORMAL}</li>
     *               <li>{@link BlurMaskFilter.Blur#SOLID}</li>
     *               <li>{@link BlurMaskFilter.Blur#OUTER}</li>
     *               <li>{@link BlurMaskFilter.Blur#INNER}</li>
     *               </ul>
     * @return
     */
    public MultiStyleTextView appendBlurStyle(final float radius, final BlurMaskFilter.Blur style) {
        if (Float.compare(radius, 0) <= 0 || null == style || null == mEndSpannable) return this;

        mEndSpannable.setSpan(new MaskFilterSpan(new BlurMaskFilter(radius, style)), 0, mEndSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 调用此方法来设置最终文本
     */
    public void joint() {
        this.setText(mSpannableBuilder);
    }

    protected static class ClickSpan extends ClickableSpan {
        private CharSequence text;
        private int color;
        private OnSegmentClickListener listener;

        public ClickSpan(CharSequence text, int color, OnSegmentClickListener listener) {
            super();
            this.text = text;
            this.color = color;
            this.listener = listener;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            if (color != 0) {
                ds.setColor(color);
            }
            ds.setUnderlineText(false); //下划线
        }

        @Override
        public void onClick(View widget) {
            listener.onClick(widget, this.text);
        }
    }
}
