package space.zhupeng.base.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
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
    }

    /**
     * 添加文本片段
     *
     * @param text
     * @return
     */
    public MultiStyleTextView segment(final CharSequence text) {
        return this;
    }

    /**
     * 添加特定字体大小的文本片段
     *
     * @param text
     * @param textSize 字体大小，单位为SP
     * @return
     */
    public MultiStyleTextView segment(final CharSequence text, final int textSize) {
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
    public MultiStyleTextView segment(final CharSequence text, final int textSize, final OnSegmentClickListener listener) {

        return this;
    }

    /**
     * 添加特定字体大小并且可点击的文本片段
     *
     * @param text
     * @param textSize  字体大小，单位为SP
     * @param underline true 带下划线，false 不带下划线
     * @param listener
     * @return
     */
    public MultiStyleTextView segment(final CharSequence text, final int textSize, final boolean underline, final OnSegmentClickListener listener) {
        if (TextUtils.isEmpty(text)) return this;

        final SpannableString ss = new SpannableString(text);
        if (textSize > 0) {
            ss.setSpan(new AbsoluteSizeSpan(textSize), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if (listener != null) {
            ss.setSpan(new ClickSpan(text, mDefaultColor, underline, listener), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        mSpannableBuilder.append(ss);
        return this;
    }

    public MultiStyleTextView segment(final CharSequence text, final int textSize, final int textColor, final boolean underline, final OnSegmentClickListener listener) {
        if (TextUtils.isEmpty(text)) return this;

        final SpannableString ss = new SpannableString(text);
        if (textSize > 0) {
            ss.setSpan(new AbsoluteSizeSpan(textSize), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if (listener != null) {
            ss.setSpan(new ClickSpan(text, mDefaultColor, underline, listener), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        mSpannableBuilder.append(ss);
        return this;
    }

    /**
     * 追加背景色样式
     *
     * @param color 背景颜色
     * @return
     */
    public MultiStyleTextView appendBackgroundColor(final int color) {
        if (color <= 0) return this;

//        mBuilder.setSpan(new BackgroundColorSpan(backgroundColor), start, end, flag);
        return this;
    }

    /**
     * 追加引用线的颜色样式
     *
     * @param color 引用线颜色
     * @return
     */
    public MultiStyleTextView appendQuoteColor(final int color) {
        if (color <= 0) return this;

//        mBuilder.setSpan(new QuoteSpan(quoteColor), start, end, 0);

        return this;
    }

    /**
     * 追加缩进样式
     *
     * @param first 首行缩进
     * @param rest  剩余行缩进
     * @return
     */
    public MultiStyleTextView appendLeadingMargin(final int first, final int rest) {
//        mBuilder.setSpan(new LeadingMarginSpan.Standard(first, rest), start, end, flag);
        return this;
    }

    /**
     * 追加超链接
     *
     * @param url 链接地址
     * @return
     */
    public MultiStyleTextView appendUrl(final String url) {
//        mBuilder.setSpan(new URLSpan(url), start, end, flag);
        setMovementMethod(LinkMovementMethod.getInstance());
        return this;
    }

    /**
     * 追加列表标记样式
     *
     * @param padding 列表标记和文字间距离
     * @param color   列表标记的颜色
     * @return
     */
    public MultiStyleTextView appendBullet(final int padding, final int color) {
//        mBuilder.setSpan(new BulletSpan(padding, color), start, end, 0);
        return this;
    }

    /**
     * 追加删除线样式
     *
     * @return
     */
    public MultiStyleTextView appendStrikeThrough() {
//        mBuilder.setSpan(new StrikethroughSpan(), start, end, flag);
        return this;
    }

    /**
     * 追加下划线样式
     *
     * @return
     */
    public MultiStyleTextView appendUnderline() {
//        mBuilder.setSpan(new UnderlineSpan(), start, end, flag);
        return this;
    }

    /**
     * 追加上标样式
     *
     * @return
     */
    public MultiStyleTextView appendSuperscript() {
//        mBuilder.setSpan(new SuperscriptSpan(), start, end, flag);
        return this;
    }

    /**
     * 追加下标样式
     *
     * @return
     */
    public MultiStyleTextView appendSubscript() {
//        mBuilder.setSpan(new SubscriptSpan(), start, end, flag);
        return this;
    }

    /**
     * 追加粗体样式
     *
     * @return
     */
    public MultiStyleTextView appendBoldStyle() {
//        mBuilder.setSpan(new StyleSpan(Typeface.BOLD), start, end, flag);
        return this;
    }

    /**
     * 追加斜体样式
     *
     * @return
     */
    public MultiStyleTextView appendItalicStyle() {
//        mBuilder.setSpan(new StyleSpan(Typeface.ITALIC), start, end, flag);
        return this;
    }

    /**
     * 追加字体样式
     *
     * @param fontFamily
     * @return
     */
    public MultiStyleTextView appendFontFamily(final String fontFamily) {
        if (TextUtils.isEmpty(fontFamily)) return this;

//        mBuilder.setSpan(new TypefaceSpan(fontFamily), start, end, flag);
        return this;
    }

    /**
     * 追加图片
     *
     * @param bitmap 位图
     * @return
     */
    public MultiStyleTextView appendImage(final Bitmap bitmap) {
        if (null == bitmap) return this;
//        mBuilder.setSpan(new ImageSpan(getContext(), bitmap), start, end, flag);
        return this;
    }

    /**
     * 追加图片
     *
     * @param drawable
     * @return
     */
    public MultiStyleTextView appendImage(final Drawable drawable) {
        if (null == drawable) return this;

//        mBuilder.setSpan(new ImageSpan(drawable), start, end, flag);
        return this;
    }

    /**
     * 追加图片
     *
     * @param resId 图片资源id
     * @return
     */
    public MultiStyleTextView appendImage(@DrawableRes final int resId) {
//        mBuilder.setSpan(new ImageSpan(getContext(), resourceId), start, end, flag);
        return this;
    }

    /**
     * 追加图片
     *
     * @param uri
     * @return
     */
    public MultiStyleTextView appendImage(final Uri uri) {
//        mBuilder.setSpan(new ImageSpan(getContext(), uri), start, end, flag);
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
        if (Float.compare(radius, 0) <= 0) return this;

//        mBuilder.setSpan(new MaskFilterSpan(new BlurMaskFilter(radius, style)), start, end, flag);
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
        private boolean underline;
        private OnSegmentClickListener listener;

        public ClickSpan(CharSequence text, int color, boolean underline, OnSegmentClickListener listener) {
            super();
            this.text = text;
            this.color = color;
            this.underline = underline;
            this.listener = listener;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            if (color != 0) {
                ds.setColor(color);
            }
            ds.setUnderlineText(underline); //下划线
        }

        @Override
        public void onClick(View widget) {
            listener.onClick(widget, this.text);
        }
    }
}
