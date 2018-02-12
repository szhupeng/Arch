package space.zhupeng.arch.widget.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import space.zhupeng.arch.R;
import space.zhupeng.arch.widget.progress.MaterialProgressView;

/**
 * 更新弹窗
 *
 * @author zhupeng
 * @date 2016/12/4
 */

public class UpdateDialog extends BaseDialogFragment {

    private TextView tvUpdateTitle;
    private TextView tvUpdateLog;
    private MaterialProgressView mProgressBar;
    private View mDiv;
    private TextView btnNegative;
    private TextView btnPositive;

    private Builder mBuilder;

    @Override
    protected int getLayoutResId() {
        return R.layout.dialog_update;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        tvUpdateTitle = findById(R.id.tv_update_title);
        tvUpdateLog = findById(R.id.tv_update_log);
        mProgressBar = findById(R.id.progress_bar);
        mDiv = findById(R.id.div);
        btnNegative = findById(R.id.btn_negative);
        btnPositive = findById(R.id.btn_positive);

        if (null == mBuilder) return;

        if (!TextUtils.isEmpty(mBuilder.mUpdateTitle)) {
            if (tvUpdateTitle.getVisibility() != View.VISIBLE) {
                tvUpdateTitle.setVisibility(View.VISIBLE);
            }
            tvUpdateTitle.setText(mBuilder.mUpdateTitle);
            if (Float.compare(mBuilder.mTitleTextSize, 0f) > 0) {
                tvUpdateTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, mBuilder.mTitleTextSize);
            }
            if (mBuilder.mTitleTextColor != 0) {
                tvUpdateTitle.setTextColor(mBuilder.mTitleTextColor);
            }
        } else {
            if (tvUpdateTitle.getVisibility() != View.GONE) {
                tvUpdateTitle.setVisibility(View.GONE);
            }
        }

        tvUpdateLog.setText(mBuilder.mUpdateLog);
        if (Float.compare(mBuilder.mLogTextSize, 0f) > 0) {
            tvUpdateLog.setTextSize(TypedValue.COMPLEX_UNIT_SP, mBuilder.mLogTextSize);
        }
        if (mBuilder.mLogTextColor != 0) {
            tvUpdateLog.setTextColor(mBuilder.mLogTextColor);
        }

        if (mBuilder.isForceUpdate) {
            if (btnNegative.getVisibility() != View.GONE) {
                btnNegative.setVisibility(View.GONE);
            }
        } else {
            if (btnNegative.getVisibility() != View.VISIBLE) {
                btnNegative.setVisibility(View.VISIBLE);
            }
        }

        if (!TextUtils.isEmpty(mBuilder.mNegativeText)) {
            btnNegative.setText(mBuilder.mNegativeText);
            if (Float.compare(mBuilder.mNegativeTextSize, 0f) > 0) {
                btnNegative.setTextSize(TypedValue.COMPLEX_UNIT_SP, mBuilder.mNegativeTextSize);
            }
            if (mBuilder.mNegativeTextColor != 0) {
                btnNegative.setTextColor(mBuilder.mNegativeTextColor);
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

        if (!mBuilder.isForceUpdate) {
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

            }
        });
    }

    private void setBuilder(final Builder builder) {
        this.mBuilder = builder;
    }

    public static class Builder {
        private CharSequence mUpdateTitle;
        private float mTitleTextSize;
        private int mTitleTextColor;

        private CharSequence mUpdateLog;
        private float mLogTextSize;
        private int mLogTextColor;

        private CharSequence mNegativeText;
        private float mNegativeTextSize;
        private int mNegativeTextColor;
        private View.OnClickListener mNegativeClickListener;

        private CharSequence mPositiveText;
        private float mPositiveTextSize;
        private int mPositiveTextColor;
        private View.OnClickListener mPositiveClickListener;

        private String mDownloadUrl;
        private boolean isForceUpdate;
        private boolean isDownloadInBack;

        public Builder(Context context) {
        }

        public Builder setUpdateTitle(CharSequence title) {
            this.mUpdateTitle = title;
            return this;
        }

        public Builder setUpdateTitleTextSize(float textSize) {
            this.mTitleTextSize = textSize;
            return this;
        }

        public Builder setUpdateTitleTextColor(@ColorInt int textColor) {
            this.mTitleTextColor = textColor;
            return this;
        }

        public Builder setUpdateLog(CharSequence log) {
            this.mUpdateLog = log;
            return this;
        }

        public Builder setUpdateLogTextSize(float textSize) {
            this.mLogTextSize = textSize;
            return this;
        }

        public Builder setUpdateLogTextColor(@ColorInt int textColor) {
            this.mLogTextColor = textColor;
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

        public Builder setPositiveButton(CharSequence text, View.OnClickListener listener) {
            this.mPositiveText = text;
            this.mPositiveClickListener = listener;
            return this;
        }

        public Builder setPositiveButtonTextSize(float textSize) {
            this.mPositiveTextSize = textSize;
            return this;
        }

        public Builder setPositiveButtonTextColor(int textColor) {
            this.mPositiveTextColor = textColor;
            return this;
        }

        public Builder setDownloadUrl(String url) {
            this.mDownloadUrl = url;
            return this;
        }

        public Builder setForceUpdate(boolean forceUpdate) {
            isForceUpdate = forceUpdate;
            return this;
        }

        public Builder setDownloadInBack(boolean inBack) {
            this.isDownloadInBack = inBack;
            return this;
        }

        public UpdateDialog build() {
            UpdateDialog dialog = new UpdateDialog();
            dialog.setCancelable(!isForceUpdate);
            dialog.setBuilder(this);
            return dialog;
        }

        public void show(FragmentManager manager) {
            UpdateDialog dialog = build();
            dialog.show(manager);
        }
    }
}
