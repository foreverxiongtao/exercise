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
 * desc   : 前台服务
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
        Log.d(TAG, "onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand()");
        startForegroundNotification();
        return super.onStartCommand(intent, flags, startId);
    }


    /***
     * 开启前台任务通知
     */
    private void startForegroundNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.content_notification_layout);
        remoteViews.setOnClickPendingIntent(R.id.tv_notify_previouse, getPendIntentWithRequestCode(REQUEST_CODE_PREVIOUSE_SONG));
        remoteViews.setOnClickPendingIntent(R.id.tv_notify_start, getPendIntentWithRequestCode(REQUEST_CODE_PAUSE_SONG));
        remoteViews.setOnClickPendingIntent(R.id.tv_notify_next, getPendIntentWithRequestCode(REQUEST_CODE_NEXT_SONG));
        String NOTIFICATION_CHANNEL_ID = getString(R.string.str_default_channel);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //设定的通知渠道名称以及通知的重要程度，并构建通知渠道
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, getString(R.string.str_channel_name), NotificationManager.IMPORTANCE_LOW);
            notificationChannel.setDescription(getString(R.string.str_channel_describtion));
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

//        //在创建的通知渠道上发送通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        builder.setSmallIcon(R.mipmap.ic_launcher);  //通知图标
//        builder.setContentTitle(getString(R.string.str_content_title)); //通知标题
//        builder.setContentText(getString(R.string.str_content_text)); //通知内容
//        builder.setContentInfo(getString(R.string.str_content_info));       //设置服务内容
        builder.setContent(remoteViews);
//        builder.setWhen(System.currentTimeMillis());  //设置通知时间
//        builder.setContentIntent(getPendIntentWithRequestCode(REQUEST_CODE_NORMAL));

        Notification notification = builder.build();
        //将服务置于启动状态 NOTIFICATION_ID指的是创建的通知的ID
        startForeground(FOREGROUND_ID, notification);
    }


    /***
     * 获取延迟意图实例对象
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
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
        stopForeground(true);// 停止前台服务--参数：表示是否移除之前的通知
    }
}
