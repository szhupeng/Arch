package space.zhupeng.arch.manager;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import space.zhupeng.arch.utils.AppUtils;

/**
 * 前台下载更新
 *
 * @author zhupeng
 * @date 2018/2/22
 */

public class ForegroundUpgrade extends UpgradeStrategy {

    public interface ProgressListener {
        void onProgress(int downloaded, int total);
    }

    private DownloadManager mDownloader;
    private final ProgressListener mProgressListener;
    private final Handler mProgressHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (mProgressListener != null) {
                mProgressListener.onProgress(msg.arg1, msg.arg2);
            }
        }
    };

    public ForegroundUpgrade(Context context, int versionCode, String url, ProgressListener listener) {
        super(context, versionCode, url);

        this.mProgressListener = listener;
    }

    @Override
    public void stop() {
        if (null == context) return;

        if (mDownloader != null) {
            mDownloader.remove(PreferenceManager.getDefaultSharedPreferences(context).getLong("download_id", -1));
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
        task.setVisibleInDownloadsUi(false);
        task.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        task.setMimeType("application/vnd.android.package-archive");
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            task.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, mApkName);
        }

        final long taskId = mDownloader.enqueue(task);
        sp.edit().putLong("download_id", taskId).commit();
        updateProgress(taskId);
    }

    private void updateProgress(final long taskId) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @SuppressLint("NewApi")
            @Override
            public void run() {
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(taskId);
                Cursor cursor = ((DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE)).query(query);
                if (cursor.moveToFirst()) {
                    int downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    int total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                    cursor.close();

                    if (downloaded > 0) {
                        mProgressHandler.sendMessage(mProgressHandler.obtainMessage(1, downloaded, total));
                        if (downloaded >= total) {
                            timer.cancel();
                            installApk(getDownloadApkPath());
                        }
                    }
                } else {
                    timer.cancel();
                }
            }

        }, 0, 100);
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
}
