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
import space.zhupeng.arch.manager.BackgroundUpgrade;
import space.zhupeng.arch.manager.ForegroundUpgrade;
import space.zhupeng.arch.manager.UpgradeManager;
import space.zhupeng.arch.widget.progress.MaterialProgressView;

/**
 * 更新弹窗
 *
 * @author zhupeng
 * @date 2016/12/4
 */

public class UpgradeDialog extends BaseDialogFragment {

    private TextView tvUpgradeTitle;
    private TextView tvUpgradeLog;
    private MaterialProgressView mProgressBar;
    private View mDiv;
    private TextView btnNegative;
    private TextView btnPositive;

    private Builder mBuilder;

    @Override
    protected int getLayoutResId() {
        return R.layout.dialog_upgrade;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        tvUpgradeTitle = findById(R.id.tv_upgrade_title);
        tvUpgradeLog = findById(R.id.tv_upgrade_log);
        mProgressBar = findById(R.id.progress_bar);
        mDiv = findById(R.id.div);
        btnNegative = findById(R.id.btn_negative);
        btnPositive = findById(R.id.btn_positive);

        if (null == mBuilder) return;

        if (!TextUtils.isEmpty(mBuilder.mUpgradeTitle)) {
            if (tvUpgradeTitle.getVisibility() != View.VISIBLE) {
                tvUpgradeTitle.setVisibility(View.VISIBLE);
            }
            tvUpgradeTitle.setText(mBuilder.mUpgradeTitle);
            if (Float.compare(mBuilder.mTitleTextSize, 0f) > 0) {
                tvUpgradeTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, mBuilder.mTitleTextSize);
            }
            if (mBuilder.mTitleTextColor != 0) {
                tvUpgradeTitle.setTextColor(mBuilder.mTitleTextColor);
            }
        } else {
            if (tvUpgradeTitle.getVisibility() != View.GONE) {
                tvUpgradeTitle.setVisibility(View.GONE);
            }
        }

        tvUpgradeLog.setText(mBuilder.mUpgradeLog);
        if (Float.compare(mBuilder.mLogTextSize, 0f) > 0) {
            tvUpgradeLog.setTextSize(TypedValue.COMPLEX_UNIT_SP, mBuilder.mLogTextSize);
        }
        if (mBuilder.mLogTextColor != 0) {
            tvUpgradeLog.setTextColor(mBuilder.mLogTextColor);
        }

        if (mBuilder.isForceUpgrade) {
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

        if (!mBuilder.isForceUpgrade) {
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
                if (mBuilder.mPositiveClickListener != null) {
                    mBuilder.mPositiveClickListener.onClick(v);
                } else if (mBuilder.isDownloadInBack) {
                    dismissAllowingStateLoss();
                    new UpgradeManager(new BackgroundUpgrade(getActivity(), mBuilder.mDownloadUrl).save(mBuilder.mApkPath, mBuilder.mApkName)).start();
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    new UpgradeManager(new ForegroundUpgrade(getActivity(), mBuilder.mDownloadUrl, new ForegroundUpgrade.ProgressListener() {
                        @Override
                        public void onProgress(int downloaded, int total) {
                            mProgressBar.setProgress(downloaded * 1.0f / total);
                            if (downloaded > 0 && downloaded == total) {
                                mProgressBar.setVisibility(View.GONE);
                                dismissAllowingStateLoss();
                            }
                        }
                    }).save(mBuilder.mApkPath, mBuilder.mApkName)).start();
                }
            }
        });
    }

    private void setBuilder(final Builder builder) {
        this.mBuilder = builder;
    }

    public static class Builder {
        private CharSequence mUpgradeTitle;
        private float mTitleTextSize;
        private int mTitleTextColor;

        private CharSequence mUpgradeLog;
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

        private int mVersionCode;
        private String mDownloadUrl;
        private boolean isForceUpgrade;
        private boolean isDownloadInBack;
        private String mApkPath;
        private String mApkName;

        public Builder(Context context) {
        }

        public Builder setUpgradeTitle(CharSequence title) {
            this.mUpgradeTitle = title;
            return this;
        }

        public Builder setUpgradeTitleTextSize(float textSize) {
            this.mTitleTextSize = textSize;
            return this;
        }

        public Builder setUpgradeTitleTextColor(@ColorInt int textColor) {
            this.mTitleTextColor = textColor;
            return this;
        }

        public Builder setUpgradeLog(CharSequence log) {
            this.mUpgradeLog = log;
            return this;
        }

        public Builder setUpgradeLogTextSize(float textSize) {
            this.mLogTextSize = textSize;
            return this;
        }

        public Builder setUpgradeLogTextColor(@ColorInt int textColor) {
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

        /**
         * 服务器返回的最新版本号
         *
         * @param versionCode
         * @return
         */
        public Builder setVersionCode(int versionCode) {
            this.mVersionCode = versionCode;
            return this;
        }

        /**
         * 服务器返回的下载链接
         *
         * @param url
         * @return
         */
        public Builder setDownloadUrl(String url) {
            this.mDownloadUrl = url;
            return this;
        }

        /**
         * 是否强制更新
         *
         * @param forceUpgrade
         * @return
         */
        public Builder setForceUpgrade(boolean forceUpgrade) {
            isForceUpgrade = forceUpgrade;
            return this;
        }

        /**
         * 是否开启后台下载
         *
         * @param inBack
         * @return
         */
        public Builder setDownloadInBack(boolean inBack) {
            this.isDownloadInBack = inBack;
            return this;
        }

        /**
         * 下载安装包的保存路径
         *
         * @param path
         * @param name
         * @return
         */
        public Builder setAppPath(String path, String name) {
            this.mApkPath = path;
            this.mApkName = name;
            return this;
        }

        public Builder setAppPath(String name) {
            this.mApkName = name;
            return this;
        }

        public UpgradeDialog build() {
            UpgradeDialog dialog = new UpgradeDialog();
            dialog.setCancelable(!isForceUpgrade);
            dialog.setBuilder(this);
            return dialog;
        }

        public void show(FragmentManager manager) {
            UpgradeDialog dialog = build();
            dialog.show(manager, "upgrade");
        }
    }
}
