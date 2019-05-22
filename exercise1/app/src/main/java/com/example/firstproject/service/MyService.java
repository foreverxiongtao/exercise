package com.example.firstproject.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.firstproject.MainActivity;
import com.example.firstproject.R;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/24 下午11:55
 * desc   : foreground service
 * version: 1.0
 */
public class MyService extends Service {

    private static final String TAG = MyService.class.getSimpleName();
    public static final String KEY_REQUEST_CODE =  "key_request_code";
    private final static int FOREGROUND_ID = 10000;
    public final static int REQUEST_CODE_NORMAL = 1;
    public final static int REQUEST_CODE_PREVIOUSE_SONG = 100;
    public final static int REQUEST_CODE_PAUSE_SONG = 200;
    public final static int REQUEST_CODE_NEXT_SONG = 300;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForegroundNotification();
        return super.onStartCommand(intent, flags, startId);
    }


    /***
     * start foreground task notification
     */
    private void startForegroundNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.content_notification_layout);
        remoteViews.setOnClickPendingIntent(R.id.tv_notify_previouse, getPendIntentWithRequestCode(REQUEST_CODE_PREVIOUSE_SONG));
        remoteViews.setOnClickPendingIntent(R.id.tv_notify_start, getPendIntentWithRequestCode(REQUEST_CODE_PAUSE_SONG));
        remoteViews.setOnClickPendingIntent(R.id.tv_notify_next, getPendIntentWithRequestCode(REQUEST_CODE_NEXT_SONG));
        String NOTIFICATION_CHANNEL_ID = getString(R.string.str_default_channel);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Set the notification channel name and the importance of the notification, and build a notification channel
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, getString(R.string.str_channel_name), NotificationManager.IMPORTANCE_LOW);
            notificationChannel.setDescription(getString(R.string.str_channel_describtion));
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        builder.setSmallIcon(R.mipmap.ic_launcher);  //icon
//        builder.setContentTitle(getString(R.string.str_content_title)); //title
//        builder.setContentText(getString(R.string.str_content_text)); // content
        builder.setContent(remoteViews);
//        builder.setWhen(System.currentTimeMillis());  //time
//        builder.setContentIntent(getPendIntentWithRequestCode(REQUEST_CODE_NORMAL));

        Notification notification = builder.build();
        startForeground(FOREGROUND_ID, notification);
    }


    /***
     * get pendintent with request code
     * @param requestCode
     * @return
     */
    private PendingIntent getPendIntentWithRequestCode(int requestCode) {
        Intent activityIntent = new Intent(this, MainActivity.class);
        activityIntent.putExtra(KEY_REQUEST_CODE,requestCode);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);//
    }
}
