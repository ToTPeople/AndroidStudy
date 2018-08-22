package com.example.lfs.androidstudy.ContentProviderLoad;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.lfs.androidstudy.MainActivity;
import com.example.lfs.androidstudy.R;

/**
 * Created by lfs on 2018/8/17.
 */

public class NotificationService extends Service {
    private int m_id = 1;

    public NotificationService() {
        Log.i("ServiceTest", "[construct] process id: " + android.os.Process.myPid());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null != intent) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Channel")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Android Study")
                    .setContentText("Go to android study app.")
                    .setAutoCancel(true)
                    ;

            // 设置通知栏点击响应事件
            Intent intent1 = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, 0);
            builder.setContentIntent(pendingIntent);

            NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

            // Android 8.0及以上版本需要设置Channel
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("Channel", "Test", NotificationManager.IMPORTANCE_DEFAULT);
                builder.setChannelId("Channel");
                manager.createNotificationChannel(channel);
            }


            manager.notify(m_id, builder.build());
        }

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("ServiceTest", "[onDestroy] process id: " + android.os.Process.myPid());
    }
}
