package com.example.exercise2;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String KEY_MESSENGER = "key_messenger";
    public int mJobId = 1000;
    private static final int TIME_PERIOD = 1000 * 3;
    private static final int TIME_DELEADLINE = 1000 * 60;
    private JobScheduler mJobScheduler;
    private Button mBtnStartDownload;


    private class MyHandler extends Handler {

        WeakReference<Activity> mActivityReference;

        MyHandler(Activity acitvity) {
            mActivityReference = new WeakReference<>(acitvity);
        }

        @Override
        public void handleMessage(Message msg) {
            Activity activity = mActivityReference.get();
            if (activity == null) {
                return;
            }
            mBtnStartDownload.setEnabled(true);
            Object obj = msg.obj;
            if (obj != null) {
                Toast.makeText(activity, (String) obj, Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnStartDownload = findViewById(R.id.btn_main_start_download);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initDownloadTask();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(new Intent(this, MyJobService.class));
    }

    /***
     * 初始化下载服务
     */
    private void initDownloadTask() {
        Intent intent = new Intent(this, MyJobService.class);
        intent.putExtra(KEY_MESSENGER, new Messenger(new MyHandler(this))); //传递messenger到service
        startService(intent);
    }

    /***
     * 开始下载
     * @param view
     */
    public void startDownloadTask(View view) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (mJobScheduler == null) {
                mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            }
            JobInfo.Builder builder = new JobInfo.Builder(mJobId, new ComponentName(this, MyJobService.class));
//            builder.setPeriodic(TIME_PERIOD);  //设置任务每隔intervalMillis运行一次
//            builder.setOverrideDeadline(TIME_DELEADLINE);//这个方法让你可以设置任务最晚的延迟时间。如果到了规定的时间时其他条件还未满足，你的任务也会被启动。与setMinimumLatency(long time)一样，这个方法也会与setPeriodic(long time)，同时调用这两个方法会引发异常
//            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);  //这个方法让你这个任务只有在满足指定的网络条件时才会被执行,
//            builder.setRequiresCharging(true);   //这个方法告诉你的应用，只有当设备在充电时这个任务才会被执行
//            builder.setRequiresDeviceIdle(true); //这个方法告诉你的任务只有当用户没有在使用该设备且有一段时间没有使用时才会启动该任务
//            builder.setPersisted(true);//这个方法告诉系统当你的设备重启之后你的任务是否还要继续执行

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                builder.setMinimumLatency(TIME_PERIOD); //执行的最小延迟时间
                builder.setOverrideDeadline(TIME_PERIOD);  //执行的最长延时时间
//                builder.setBackoffCriteria(TIME_PERIOD, JobInfo.BACKOFF_POLICY_LINEAR);//线性重试方案
            } else {
                builder.setOverrideDeadline(TIME_PERIOD);  //执行的最长延时时间
//                builder.setPeriodic(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS);
            }
            JobInfo jobInfo = builder.build();
            int jobId = mJobScheduler.schedule(jobInfo);
            if (jobId < 0) {
                Log.d(TAG, "job went wrong...............");
                return;
            }
            mJobId++;
            mBtnStartDownload.setEnabled(false);
        } else {
            Toast.makeText(this, R.string.str_lower_version, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 取消调度任务
     *
     * @param view
     */
    public void cancelDownloadTask(View view) {
        if (mJobScheduler != null) {
            mJobScheduler.cancelAll();
        }
    }
}
