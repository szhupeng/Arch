package space.zhupeng.arch.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.xmlpull.v1.XmlPullParser.END_DOCUMENT;
import static org.xmlpull.v1.XmlPullParser.START_TAG;

/**
 * 兼容Android 7.0文件共享帮助类
 * 注意：Android N以下版本使用getFilesDir()/getCacheDir()作为系统相机拍照存储路径无法正常运行
 *
 * @author zhupeng
 * @date 2017/8/1
 */

public class FileProviderCompat {

    private static final String META_DATA_FILE_PROVIDER_PATHS = "android.support.FILE_PROVIDER_PATHS";

    private static final String TAG_ROOT_PATH = "root-path";
    private static final String TAG_FILES_PATH = "files-path";
    private static final String TAG_CACHE_PATH = "cache-path";
    private static final String TAG_EXTERNAL = "external-path";
    private static final String TAG_EXTERNAL_FILES = "external-files-path";
    private static final String TAG_EXTERNAL_CACHE = "external-cache-path";

    private static final String ATTR_NAME = "name";
    private static final String ATTR_PATH = "path";

    public static Uri getUriForFile(Context context, File file) {
        Uri fileUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fileUri = getUriForNougatFile(context, file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }

    private static Uri getUriForNougatFile(Context context, File file) {
        Uri fileUri = FileProvider.getUriForFile(context, getProviderAuthority(context), file);
        return fileUri;
    }

    public static void setIntentDataAndType(Context context, Intent intent, String type, File file, boolean writable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setDataAndType(getUriForFile(context, file), type);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (writable) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        } else {
            intent.setDataAndType(Uri.fromFile(file), type);
        }
    }

    public static void setIntentData(Context context, Intent intent, File file, boolean writable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setData(getUriForFile(context, file));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (writable) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        } else {
            intent.setData(Uri.fromFile(file));
        }
    }

    public static void grantPermissions(Context context, Intent intent, Uri uri, boolean writable) {
        int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;
        if (writable) {
            flag |= Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
        }
        intent.addFlags(flag);
        List<ResolveInfo> resInfoList = context.getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, uri, flag);
        }
    }

    @SuppressLint("NewApi")
    public static String getPathWithUri(final Context context, @NonNull final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return new StringBuilder()
                            .append(Environment.getExternalStorageDirectory().getPath())
                            .append("/")
                            .append(split[1])
                            .toString();
                }
            } else if (isDownloadsDocument(uri)) {
                // DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                // MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if (ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(uri.getScheme())) {
            // MediaStore (and general)
            String filePath = getDataColumn(context, uri, null, null);
            if (TextUtils.isEmpty(filePath) && TextUtils.equals(uri.getAuthority(), getProviderAuthority(context))) {
                filePath = parseUri(context, uri);
            }
            return filePath;
        } else if (ContentResolver.SCHEME_FILE.equalsIgnoreCase(uri.getScheme())) {
            // File
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String filePath = null;
        Cursor cursor = null;
        final String column = MediaStore.Images.Media.DATA;
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int columnIndex = cursor.getColumnIndexOrThrow(column);
                filePath = cursor.getString(columnIndex);
            }
        } catch (IllegalArgumentException e) {
            Log.e("FileProviderCompat", e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return filePath;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * 将Uri解析出文件绝对路径
     *
     * @param uri
     * @return
     */
    private static String parseUri(Context context, final Uri uri) {
        if (uri == null) return null;

        if (TextUtils.equals(uri.getAuthority(), getProviderAuthority(context))) {
            final String path = uri.getPath();
            try {
                return parseUriByXml(context, path);
            } catch (Exception e) {
                return null;
            }
        } else {
            return uri.getPath();
        }
    }

    public final static String getProviderAuthority(Context context) {
        return new StringBuilder(context.getPackageName()).append(".fileprovider").toString();
    }

    private static String parseUriByXml(Context context, final String filePath) throws IOException, XmlPullParserException {
        final ProviderInfo info = context.getPackageManager().resolveContentProvider(getProviderAuthority(context), PackageManager.GET_META_DATA);
        final XmlResourceParser in = info.loadXmlMetaData(
                context.getPackageManager(), META_DATA_FILE_PROVIDER_PATHS);
        if (in == null) {
            throw new IllegalArgumentException(
                    "Missing " + META_DATA_FILE_PROVIDER_PATHS + " meta-data");
        }

        int type;
        while ((type = in.next()) != END_DOCUMENT) {
            if (type == START_TAG) {
                final String tag = in.getName();
                final String name = in.getAttributeValue(null, ATTR_NAME);
                final String path = in.getAttributeValue(null, ATTR_PATH);

                if (TextUtils.isEmpty(name) && TextUtils.isEmpty(path)) continue;

                final String prefix = new StringBuilder("/").append(name).append("/").toString();
                StringBuilder target = new StringBuilder();

                if (TAG_FILES_PATH.equals(tag)) {
                    target.append(context.getFilesDir().getPath()).append("/");
                } else if (TAG_CACHE_PATH.equals(tag)) {
                    target.append(context.getCacheDir().getPath()).append("/");
                } else if (TAG_EXTERNAL.equals(tag)) {
                    target.append(Environment.getExternalStorageDirectory().getPath()).append("/");
                } else if (TAG_EXTERNAL_FILES.equals(tag)) {
                    File[] externalFilesDirs = ContextCompat.getExternalFilesDirs(context, null);
                    if (externalFilesDirs.length > 0) {
                        target.append(externalFilesDirs[0].getPath()).append("/");
                    }
                } else if (TAG_EXTERNAL_CACHE.equals(tag)) {
                    File[] externalCacheDirs = ContextCompat.getExternalCacheDirs(context);
                    if (externalCacheDirs.length > 0) {
                        target.append(externalCacheDirs[0].getPath()).append("/");
                    }
                }

                if (!TextUtils.isEmpty(path)) {
                    target.append(path).append("/");
                }

                if (filePath.startsWith(prefix)) {
                    return new File(filePath.replaceFirst(prefix, target.toString())).getAbsolutePath();
                }
            }
        }

        return filePath;
    }
}
