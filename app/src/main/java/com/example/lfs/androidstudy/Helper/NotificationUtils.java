package com.example.lfs.androidstudy.Helper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.lfs.androidstudy.R;

/**
 * Created by lfs on 2018/8/17.
 */

public class NotificationUtils {
    private NotificationUtils() {
        throw new UnsupportedOperationException("u can't instance me");
    }

    public static NotificationCompat.Builder getNotificationBuilder(final Context context, final int nId, final String strTitle, final String strText, final String strChannelId) {
        if (nId < 0 || null == strTitle || null == strText || null == strChannelId) {
            return null;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, strChannelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(strTitle)
                .setContentText(strText)
                .setAutoCancel(true);

        return builder;
    }

    public static void showNotification(final Context context, final int nId, final NotificationCompat.Builder builder) {
        if (null == builder || null == context || nId < 0) {
            return;
        }

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("ChannelId", "Channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }

        manager.notify(nId, builder.build());
    }
}
