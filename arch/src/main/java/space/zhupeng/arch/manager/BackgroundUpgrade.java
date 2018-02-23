package space.zhupeng.arch.manager;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.File;

import space.zhupeng.arch.utils.AppUtils;

/**
 * 后台下载更新
 *
 * @author zhupeng
 * @date 2018/2/22
 */

public class BackgroundUpgrade extends UpgradeStrategy {

    private DownloadManager mDownloader;
    private NotificationClickReceiver mNotificationClickReceiver;
    private DownloadReceiver mDownloadReceiver;

    public BackgroundUpgrade(Context context, int versionCode, String url) {
        super(context, versionCode, url);

        mDownloadReceiver = new DownloadReceiver();
        mNotificationClickReceiver = new NotificationClickReceiver();
    }

    @Override
    public void stop() {
        if (null == context) return;

        if (mDownloadReceiver != null) {
            context.unregisterReceiver(mDownloadReceiver);
        }

        if (mNotificationClickReceiver != null) {
            context.unregisterReceiver(mNotificationClickReceiver);
        }
    }

    @Override
    public void start() {
        final String path = getDownloadApkPath();

        if (isApkExists(path)) return;

        //检查本地是否有安装包，有则删除
        File apkFile = new File(path);
        if (apkFile.exists()) {
            apkFile.delete();
        }

        if (mDownloader == null) {
            mDownloader = (DownloadManager) this.context.getSystemService(Context.DOWNLOAD_SERVICE);
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        //开始下载
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(sp.getLong("download_id", -1));
        Cursor cursor = mDownloader.query(query);
        //检查下载任务是否已经存在
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            int status = cursor.getInt(columnIndex);
            if (DownloadManager.STATUS_PENDING == status || DownloadManager.STATUS_RUNNING == status || DownloadManager.STATUS_PAUSED == status) {
                //更新任务已在后台进行中，无需重复更新
                cursor.close();
                return;
            }
        }
        cursor.close();

        DownloadManager.Request task = new DownloadManager.Request(Uri.parse(mDownloadUrl));
        task.setTitle(AppUtils.getAppName(context));
        //task.setDescription("本次更新:\n1.增强系统稳定性\n2.修复已知bug");
        task.setVisibleInDownloadsUi(true);
        //设置是否允许手机在漫游状态下下载
        task.setAllowedOverRoaming(false);
        //限定在WiFi下进行下载
        task.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        task.setMimeType("application/vnd.android.package-archive");
        // 在通知栏通知下载中和下载完成
        // 下载完成后该Notification才会被显示
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            // 3.0(11)以后才有该方法
            //在下载过程中通知栏会一直显示该下载的Notification，在下载完成后该Notification会继续显示，直到用户点击该Notification或者消除该Notification
            task.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        // 可能无法创建Download文件夹，如无sdcard情况，系统会默认将路径设置为/data/data/com.android.providers.downloads/cache/xxx.apk
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            task.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, mApkName);
        }

        final long taskId = mDownloader.enqueue(task);
        sp.edit().putLong("download_id", taskId).commit();

        context.registerReceiver(mDownloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        context.registerReceiver(mNotificationClickReceiver, new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
    }

    /**
     * 下载完成的广播
     */
    class DownloadReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mDownloader == null) {
                return;
            }

            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            long downloadTaskId = sp.getLong("download_id", -1);
            long completeId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            if (completeId != downloadTaskId) {
                return;
            }

            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadTaskId);
            Cursor cur = mDownloader.query(query);
            if (!cur.moveToFirst()) {
                return;
            }

            int columnIndex = cur.getColumnIndex(DownloadManager.COLUMN_STATUS);
            if (DownloadManager.STATUS_SUCCESSFUL == cur.getInt(columnIndex)) {
                installApk(getDownloadApkPath());
            } else {
                Toast.makeText(context, "下载App最新版本失败!", Toast.LENGTH_LONG).show();
            }

            sp.edit().remove("download_id").commit();
            cur.close();
        }
    }

    /**
     * 点击通知栏下载项目，下载完成前点击都会进来，下载完成后点击不会进来
     */
    public class NotificationClickReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            long[] completeIds = intent.getLongArrayExtra(
                    DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
            //正在下载的任务ID
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            long downloadTaskId = sp.getLong("download_id", -1);
            if (completeIds == null || completeIds.length <= 0) {
                openDownloadsPage(context);
                return;
            }

            for (long completeId : completeIds) {
                if (completeId == downloadTaskId) {
                    openDownloadsPage(context);
                    break;
                }
            }
        }

        /**
         * Open the Activity which shows a list of all downloads.
         *
         * @param context 上下文
         */
        private void openDownloadsPage(Context context) {
            Intent intent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
