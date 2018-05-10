package space.zhupeng.arch.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;

import java.io.File;

import space.zhupeng.arch.route.Router;
import space.zhupeng.arch.utils.FileProviderCompat;
import space.zhupeng.arch.utils.Utils;

/**
 * @author zhupeng
 * @date 2018/2/22
 */

public abstract class UpgradeStrategy {

    protected Context context;

    protected int mVersionCode;
    protected String mDownloadUrl;
    protected String mApkPath;
    protected String mApkName;

    public UpgradeStrategy(Context context, int versionCode, String url) {
        this.context = context;
        this.mVersionCode = versionCode;
        this.mDownloadUrl = url;
        this.mApkName = context.getPackageName() + ".apk";
    }

    public UpgradeStrategy save(String name) {
        this.mApkName = name;
        return this;
    }

    public UpgradeStrategy save(String path, String name) {
        this.mApkPath = path;
        this.mApkName = name;
        return this;
    }

    public String getDownloadApkPath() {
        if (null == this.mApkPath) {
            this.mApkPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        }

        this.mApkPath = this.mApkPath.endsWith(File.separator) ? this.mApkPath : this.mApkPath + File.separator;
        return this.mApkPath + this.mApkName;
    }

    /**
     * 检查本地是否已经有需要升级版本的安装包
     *
     * @param apkPath
     * @return
     */
    public boolean isApkExists(String apkPath) {
        File targetApkFile = new File(apkPath);
        if (targetApkFile.exists()) {
            PackageManager pm = this.context.getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
            if (info != null) {
                String versionCode = String.valueOf(info.versionCode);
                //比较已下载到本地的apk安装包，与服务器上apk安装包的版本号是否一致
                if (String.valueOf(mVersionCode).equals(versionCode)) {
                    checkAndInstallApk(apkPath);
                    return true;
                }
            }
        }

        return false;
    }

    public void checkAndInstallApk() {
        checkAndInstallApk(getDownloadApkPath());
    }

    public void checkAndInstallApk(final String apkPath) {
        if (TextUtils.isEmpty(apkPath)) {
            //APP安装文件不存在或已损坏
            return;
        }
        final File apkFile = new File(Uri.parse(apkPath).getPath());
        if (!apkFile.exists()) {
            //APP安装文件不存在或已损坏
            return;
        }

        Utils.runOnUiThreadSafely(new Runnable() {
            @Override
            public void run() {
                // 兼容Android 8.0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //先获取是否有安装未知来源应用的权限
                    if (!context.getPackageManager().canRequestPackageInstalls()) {
                        Uri pkgUri = Uri.parse("package:" + context.getPackageName());
                        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, pkgUri);
                        Router router = new Router((Activity) context);
                        router.startActivityForResult(intent, new Router.Callback() {
                            @Override
                            public void onActivityResult(int resultCode, Intent data) {
                                if (Activity.RESULT_OK == resultCode) {
                                    installApk(apkFile);
                                }
                            }
                        });
                        return;
                    } else {
                        installApk(apkFile);
                    }
                } else {
                    installApk(apkFile);
                }
            }
        });
    }

    private void installApk(final File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(FileProviderCompat.getUriForFile(context, apkFile), "application/vnd.android.package-archive");
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public void stop() {
    }

    public abstract void start();
}
