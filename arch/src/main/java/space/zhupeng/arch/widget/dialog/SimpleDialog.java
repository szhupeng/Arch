package space.zhupeng.arch.widget.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import space.zhupeng.arch.R;

/**
 * @author zhupeng
 * @date 2016/12/4
 */

public class SimpleDialog extends BaseDialogFragment implements DialogInterface.OnDismissListener {

    private TextView tvTitle;
    private TextView tvMessage;
    private TextView tvSubtext;
    private View mDiv;
    private TextView btnNegative;
    private TextView btnPositive;

    private Builder mBuilder;

    @Override
    protected int getLayoutResId() {
        return R.layout.dialog_simple;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        tvTitle = findById(R.id.tv_title);
        tvMessage = findById(R.id.tv_message);
        tvSubtext = findById(R.id.tv_subtext);
        mDiv = findById(R.id.div);
        btnNegative = findById(R.id.btn_negative);
        btnPositive = findById(R.id.btn_positive);

        if (null == mBuilder) return;

        setText(tvTitle, mBuilder.mTitle, mBuilder.mTitleTextSize, mBuilder.mTitleTextColor);
        setText(tvMessage, mBuilder.mMessage, mBuilder.mMessageTextSize, mBuilder.mMessageTextColor);
        setText(tvSubtext, mBuilder.mSubtext, mBuilder.mSubtextTextSize, mBuilder.mSubtextTextColor);

        if (!TextUtils.isEmpty(mBuilder.mNegativeText)) {
            if (btnNegative.getVisibility() != View.VISIBLE) {
                btnNegative.setVisibility(View.VISIBLE);
            }
            if (mDiv.getVisibility() != View.VISIBLE) {
                mDiv.setVisibility(View.VISIBLE);
            }
            btnNegative.setText(mBuilder.mNegativeText);
            if (Float.compare(mBuilder.mNegativeTextSize, 0f) > 0) {
                btnNegative.setTextSize(TypedValue.COMPLEX_UNIT_SP, mBuilder.mNegativeTextSize);
            }
            if (mBuilder.mNegativeTextColor != 0) {
                btnNegative.setTextColor(mBuilder.mNegativeTextColor);
            }
        } else {
            if (btnNegative.getVisibility() != View.GONE) {
                btnNegative.setVisibility(View.GONE);
            }
            if (mDiv.getVisibility() != View.GONE) {
                mDiv.setVisibility(View.GONE);
            }
        }

        if (!TextUtils.isEmpty(mBuilder.mPositiveText)) {
            btnPositive.setText(mBuilder.mPositiveText);
            if (Float.compare(mBuilder.mPositiveTextSize, 0f) > 0) {
                btnPositive.setTextSize(TypedValue.COMPLEX_UNIT_SP, mBuilder.mPositiveTextSize);
            }
            if (mBuilder.mPositiveTextColor != 0) {
                btnPositive.setTextColor(mBuilder.mPositiveTextColor);
            }
        }
    }

    @Override
    protected void bindEvent() {
        if (null == mBuilder) return;

        if (!TextUtils.isEmpty(mBuilder.mNegativeText)) {
            btnNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if (mBuilder.mNegativeClickListener != null) {
                        mBuilder.mNegativeClickListener.onClick(v);
                    }
                }
            });
        }

        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mBuilder.mPositiveClickListener != null) {
                    mBuilder.mPositiveClickListener.onClick(v);
                }
            }
        });
    }

    private void setBuilder(final Builder builder) {
        this.mBuilder = builder;
    }

    private void setText(TextView textView, CharSequence text, float textSize, int textColor) {
        if (!TextUtils.isEmpty(text)) {
            if (textView.getVisibility() != View.VISIBLE) {
                textView.setVisibility(View.VISIBLE);
            }
            textView.setText(text);
            if (Float.compare(textSize, 0f) > 0) {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
            }
            if (textColor != 0) {
                textView.setTextColor(textColor);
            }
        } else {
            if (textView.getVisibility() != View.GONE) {
                textView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDestroyView() {
        if (this.mBuilder != null) {
            this.mBuilder.context = null;
            this.mBuilder = null;
        }
        super.onDestroyView();
    }

    public static class Builder {
        private Context context;

        private CharSequence mTitle;
        private float mTitleTextSize;
        private int mTitleTextColor;

        private CharSequence mMessage;
        private float mMessageTextSize;
        private int mMessageTextColor;

        private CharSequence mSubtext;
        private float mSubtextTextSize;
        private int mSubtextTextColor;

        private CharSequence mNegativeText;
        private float mNegativeTextSize;
        private int mNegativeTextColor;
        private View.OnClickListener mNegativeClickListener;

        private CharSequence mPositiveText;
        private float mPositiveTextSize;
        private int mPositiveTextColor;
        private View.OnClickListener mPositiveClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(@StringRes int resId) {
            this.mTitle = context.getString(resId);
            return this;
        }

        public Builder setTitle(CharSequence title) {
            this.mTitle = title;
            return this;
        }

        public Builder setTitle(@StringRes int resId, float textSize, @ColorInt int textColor) {
            return setTitle(context.getString(resId), textSize, textColor);
        }

        public Builder setTitle(CharSequence title, float textSize, @ColorInt int textColor) {
            this.mTitle = title;
            this.mTitleTextSize = textSize;
            this.mTitleTextColor = textColor;
            return this;
        }

        public Builder setTitleTextSize(float textSize) {
            this.mTitleTextSize = textSize;
            return this;
        }

        public Builder setTitleTextColor(@ColorInt int textColor) {
            this.mTitleTextColor = textColor;
            return this;
        }

        public Builder setMessage(@StringRes int resId) {
            this.mMessage = context.getString(resId);
            return this;
        }

        public Builder setMessage(CharSequence message) {
            this.mMessage = message;
            return this;
        }

        public Builder setMessage(@StringRes int resId, float textSize, @ColorInt int textColor) {
            return setMessage(context.getString(resId), textSize, textColor);
        }

        public Builder setMessage(CharSequence message, float textSize, @ColorInt int textColor) {
            this.mMessage = message;
            this.mMessageTextSize = textSize;
            this.mMessageTextColor = textColor;
            return this;
        }

        public Builder setMessageTextSize(float textSize) {
            this.mMessageTextSize = textSize;
            return this;
        }

        public Builder setMessageTextColor(@ColorInt int textColor) {
            this.mMessageTextColor = textColor;
            return this;
        }

        public Builder setSubtext(@StringRes int resId) {
            this.mSubtext = context.getString(resId);
            return this;
        }

        public Builder setSubtext(CharSequence subtext) {
            this.mSubtext = subtext;
            return this;
        }

        public Builder setSubtext(@StringRes int resId, float textSize, @ColorInt int textColor) {
            return setSubtext(context.getString(resId), textSize, textColor);
        }

        public Builder setSubtext(CharSequence subtext, float textSize, @ColorInt int textColor) {
            this.mSubtext = subtext;
            this.mSubtextTextSize = textSize;
            this.mSubtextTextColor = textColor;
            return this;
        }

        public Builder setSubtext(CharSequence subtext, float textSize) {
            this.mSubtext = subtext;
            this.mSubtextTextSize = textSize;
            return this;
        }

        public Builder setSubtext(CharSequence subtext, @ColorInt int textColor) {
            this.mSubtext = subtext;
            this.mSubtextTextColor = textColor;
            return this;
        }

        public Builder setNegativeButton(@StringRes int resId) {
            this.mNegativeText = context.getString(resId);
            return this;
        }

        public Builder setNegativeButton(CharSequence text) {
            this.mNegativeText = text;
            return this;
        }

        public Builder setNegativeButton(@StringRes int resId, View.OnClickListener listener) {
            this.mNegativeText = context.getString(resId);
            this.mNegativeClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(CharSequence text, View.OnClickListener listener) {
            this.mNegativeText = text;
            this.mNegativeClickListener = listener;
            return this;
        }

        public Builder setNegativeButtonTextSize(float textSize) {
            this.mNegativeTextSize = textSize;
            return this;
        }

        public Builder setNegativeButtonTextColor(@ColorInt int textColor) {
            this.mNegativeTextColor = textColor;
            return this;
        }

        public Builder setPositiveButton(@StringRes int resId) {
            this.mPositiveText = context.getString(resId);
            return this;
        }

        public Builder setPositiveButton(CharSequence text) {
            this.mPositiveText = text;
            return this;
        }

        public Builder setPositiveButton(@StringRes int resId, View.OnClickListener listener) {
            this.mPositiveText = context.getString(resId);
            this.mPositiveClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(CharSequence text, View.OnClickListener listener) {
            this.mPositiveText = text;
            this.mPositiveClickListener = listener;
            return this;
        }

        public Builder setPositiveButtonTextSize(float textSize) {
            this.mPositiveTextSize = textSize;
            return this;
        }

        public Builder setPositiveButtonTextColor(@ColorInt int textColor) {
            this.mPositiveTextColor = textColor;
            return this;
        }

        public SimpleDialog build() {
            SimpleDialog dialog = new SimpleDialog();
            dialog.setBuilder(this);
            return dialog;
        }

        public SimpleDialog buildWithArgs(Bundle args) {
            SimpleDialog dialog = new SimpleDialog();
            dialog.setArguments(args);
            dialog.setBuilder(this);
            return dialog;
        }
    }
}
