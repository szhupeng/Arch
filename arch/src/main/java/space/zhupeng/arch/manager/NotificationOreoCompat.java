package space.zhupeng.arch.manager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import space.zhupeng.arch.utils.AppUtils;


/**
 * 兼容Android 8.0通知的封装类
 *
 * @author zhupeng
 * @date 2018/4/1
 */
public class NotificationOreoCompat extends NotificationCompat.Builder {

    private static final int ID = 0x01;

    private String mChannelId;
    private String mChannelName;

    private boolean mLights;
    private int mLightColor = Color.RED;
    private boolean mShowBadge;

    private NotificationChannel mChannel;

    public NotificationOreoCompat(@NonNull Context context, @NonNull String channelId) {
        super(context, channelId);

        this.mChannelId = channelId;
        this.mChannelName = AppUtils.getAppName(context);
    }

    public NotificationOreoCompat(Context context) {
        this(context, AppUtils.getAppPackageName(context));
    }

    public NotificationOreoCompat setSound(String sound) {
        setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + sound));
        return this;
    }

    public NotificationOreoCompat setChannelName(String channelName) {
        this.mChannelName = channelName;
        return this;
    }

    public NotificationOreoCompat enableLights(boolean lights) {
        this.mLights = lights;
        return this;
    }

    public NotificationOreoCompat setLightColor(int argb) {
        this.mLightColor = argb;
        return this;
    }

    public NotificationOreoCompat setShowBadge(boolean showBadge) {
        this.mShowBadge = showBadge;
        return this;
    }

    public NotificationOreoCompat setNotificationChannel(NotificationChannel channel) {
        this.mChannel = channel;
        return this;
    }

    public NotificationOreoCompat setActivityAction(Class<?> cls, int requestCode, int flags) {
        Intent intent = new Intent(mContext, cls);
        TaskStackBuilder tsb = TaskStackBuilder.create(mContext);
        tsb.addParentStack(cls);
        tsb.addNextIntent(intent);
        PendingIntent pi = tsb.getPendingIntent(requestCode, flags);

        setContentIntent(pi);
        return this;
    }

    public NotificationOreoCompat setActivityAction(Class<?> cls, int requestCode) {
        return setActivityAction(cls, requestCode, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void send(final int id, final Notification notification) {
        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (null == this.mChannel) {
                this.mChannel = new NotificationChannel(mChannelId, mChannelName, NotificationManager.IMPORTANCE_HIGH);
                this.mChannel.enableLights(this.mLights);
                this.mChannel.setLightColor(this.mLightColor);
                this.mChannel.setShowBadge(this.mShowBadge);
            }
            manager.createNotificationChannel(this.mChannel);
        }

        if (manager != null) {
            manager.notify(id, notification);
        }
    }

    public void send(final Notification notification) {
        send(ID, notification);
    }

    public void send() {
        send(ID, build());
    }

    public void cancel() {
        cancel(ID);
    }

    public void cancel(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.deleteNotificationChannel(mChannelId);
            manager.cancel(id);
        } else {
            NotificationManagerCompat manager = NotificationManagerCompat.from(mContext);
            manager.cancel(id);
        }
    }

    public void cancelAll() {
        NotificationManagerCompat manager = NotificationManagerCompat.from(mContext);
        manager.cancelAll();
    }

    @Override
    public NotificationOreoCompat setWhen(long when) {
        return (NotificationOreoCompat) super.setWhen(when);
    }

    @Override
    public NotificationOreoCompat setShowWhen(boolean show) {
        return (NotificationOreoCompat) super.setShowWhen(show);
    }

    @Override
    public NotificationOreoCompat setUsesChronometer(boolean b) {
        return (NotificationOreoCompat) super.setUsesChronometer(b);
    }

    @Override
    public NotificationOreoCompat setSmallIcon(int icon) {
        return (NotificationOreoCompat) super.setSmallIcon(icon);
    }

    @Override
    public NotificationOreoCompat setSmallIcon(int icon, int level) {
        return (NotificationOreoCompat) super.setSmallIcon(icon, level);
    }

    @Override
    public NotificationOreoCompat setContentTitle(CharSequence title) {
        return (NotificationOreoCompat) super.setContentTitle(title);
    }

    @Override
    public NotificationOreoCompat setContentText(CharSequence text) {
        return (NotificationOreoCompat) super.setContentText(text);
    }

    @Override
    public NotificationOreoCompat setSubText(CharSequence text) {
        return (NotificationOreoCompat) super.setSubText(text);
    }

    @Override
    public NotificationOreoCompat setRemoteInputHistory(CharSequence[] text) {
        return (NotificationOreoCompat) super.setRemoteInputHistory(text);
    }

    @Override
    public NotificationOreoCompat setNumber(int number) {
        return (NotificationOreoCompat) super.setNumber(number);
    }

    @Override
    public NotificationOreoCompat setContentInfo(CharSequence info) {
        return (NotificationOreoCompat) super.setContentInfo(info);
    }

    @Override
    public NotificationOreoCompat setProgress(int max, int progress, boolean indeterminate) {
        return (NotificationOreoCompat) super.setProgress(max, progress, indeterminate);
    }

    @Override
    public NotificationOreoCompat setContent(RemoteViews views) {
        return (NotificationOreoCompat) super.setContent(views);
    }

    @Override
    public NotificationOreoCompat setContentIntent(PendingIntent intent) {
        return (NotificationOreoCompat) super.setContentIntent(intent);
    }

    @Override
    public NotificationOreoCompat setDeleteIntent(PendingIntent intent) {
        return (NotificationOreoCompat) super.setDeleteIntent(intent);
    }

    @Override
    public NotificationOreoCompat setFullScreenIntent(PendingIntent intent, boolean highPriority) {
        return (NotificationOreoCompat) super.setFullScreenIntent(intent, highPriority);
    }

    @Override
    public NotificationOreoCompat setTicker(CharSequence tickerText) {
        return (NotificationOreoCompat) super.setTicker(tickerText);
    }

    @Override
    public NotificationOreoCompat setTicker(CharSequence tickerText, RemoteViews views) {
        return (NotificationOreoCompat) super.setTicker(tickerText, views);
    }

    @Override
    public NotificationOreoCompat setLargeIcon(Bitmap icon) {
        return (NotificationOreoCompat) super.setLargeIcon(icon);
    }

    @Override
    public NotificationOreoCompat setSound(Uri sound) {
        return (NotificationOreoCompat) super.setSound(sound);
    }

    @Override
    public NotificationOreoCompat setSound(Uri sound, int streamType) {
        return (NotificationOreoCompat) super.setSound(sound, streamType);
    }

    @Override
    public NotificationOreoCompat setVibrate(long[] pattern) {
        return (NotificationOreoCompat) super.setVibrate(pattern);
    }

    @Override
    public NotificationOreoCompat setLights(int argb, int onMs, int offMs) {
        return (NotificationOreoCompat) super.setLights(argb, onMs, offMs);
    }

    @Override
    public NotificationOreoCompat setOngoing(boolean ongoing) {
        return (NotificationOreoCompat) super.setOngoing(ongoing);
    }

    @Override
    public NotificationOreoCompat setColorized(boolean colorize) {
        return (NotificationOreoCompat) super.setColorized(colorize);
    }

    @Override
    public NotificationOreoCompat setOnlyAlertOnce(boolean onlyAlertOnce) {
        return (NotificationOreoCompat) super.setOnlyAlertOnce(onlyAlertOnce);
    }

    @Override
    public NotificationOreoCompat setAutoCancel(boolean autoCancel) {
        return (NotificationOreoCompat) super.setAutoCancel(autoCancel);
    }

    @Override
    public NotificationOreoCompat setLocalOnly(boolean b) {
        return (NotificationOreoCompat) super.setLocalOnly(b);
    }

    @Override
    public NotificationOreoCompat setCategory(String category) {
        return (NotificationOreoCompat) super.setCategory(category);
    }

    @Override
    public NotificationOreoCompat setDefaults(int defaults) {
        return (NotificationOreoCompat) super.setDefaults(defaults);
    }

    @Override
    public NotificationOreoCompat setPriority(int pri) {
        return (NotificationOreoCompat) super.setPriority(pri);
    }

    @Override
    public NotificationOreoCompat addPerson(String uri) {
        return (NotificationOreoCompat) super.addPerson(uri);
    }

    @Override
    public NotificationOreoCompat setGroup(String groupKey) {
        return (NotificationOreoCompat) super.setGroup(groupKey);
    }

    @Override
    public NotificationOreoCompat setGroupSummary(boolean isGroupSummary) {
        return (NotificationOreoCompat) super.setGroupSummary(isGroupSummary);
    }

    @Override
    public NotificationOreoCompat setSortKey(String sortKey) {
        return (NotificationOreoCompat) super.setSortKey(sortKey);
    }

    @Override
    public NotificationOreoCompat addExtras(Bundle extras) {
        return (NotificationOreoCompat) super.addExtras(extras);
    }

    @Override
    public NotificationOreoCompat setExtras(Bundle extras) {
        return (NotificationOreoCompat) super.setExtras(extras);
    }

    @Override
    public NotificationOreoCompat addAction(int icon, CharSequence title, PendingIntent intent) {
        return (NotificationOreoCompat) super.addAction(icon, title, intent);
    }

    @Override
    public NotificationOreoCompat addAction(NotificationCompat.Action action) {
        return (NotificationOreoCompat) super.addAction(action);
    }

    @Override
    public NotificationOreoCompat setStyle(NotificationCompat.Style style) {
        return (NotificationOreoCompat) super.setStyle(style);
    }

    @Override
    public NotificationOreoCompat setColor(int argb) {
        return (NotificationOreoCompat) super.setColor(argb);
    }

    @Override
    public NotificationOreoCompat setVisibility(int visibility) {
        return (NotificationOreoCompat) super.setVisibility(visibility);
    }

    @Override
    public NotificationOreoCompat setPublicVersion(Notification n) {
        return (NotificationOreoCompat) super.setPublicVersion(n);
    }

    @Override
    public NotificationOreoCompat setCustomContentView(RemoteViews contentView) {
        return (NotificationOreoCompat) super.setCustomContentView(contentView);
    }

    @Override
    public NotificationOreoCompat setCustomBigContentView(RemoteViews contentView) {
        return (NotificationOreoCompat) super.setCustomBigContentView(contentView);
    }

    @Override
    public NotificationOreoCompat setCustomHeadsUpContentView(RemoteViews contentView) {
        return (NotificationOreoCompat) super.setCustomHeadsUpContentView(contentView);
    }

    @Override
    public NotificationOreoCompat setChannelId(@NonNull String channelId) {
        return (NotificationOreoCompat) super.setChannelId(channelId);
    }

    @Override
    public NotificationOreoCompat setTimeoutAfter(long durationMs) {
        return (NotificationOreoCompat) super.setTimeoutAfter(durationMs);
    }

    @Override
    public NotificationOreoCompat setShortcutId(String shortcutId) {
        return (NotificationOreoCompat) super.setShortcutId(shortcutId);
    }

    @Override
    public NotificationOreoCompat setBadgeIconType(int icon) {
        return (NotificationOreoCompat) super.setBadgeIconType(icon);
    }

    @Override
    public NotificationOreoCompat setGroupAlertBehavior(int groupAlertBehavior) {
        return (NotificationOreoCompat) super.setGroupAlertBehavior(groupAlertBehavior);
    }

    @Override
    public NotificationOreoCompat extend(NotificationCompat.Extender extender) {
        return (NotificationOreoCompat) super.extend(extender);
    }
}
