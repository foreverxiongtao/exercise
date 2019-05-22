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
     * init download task
     */
    private void initDownloadTask() {
        Intent intent = new Intent(this, MyJobService.class);
        intent.putExtra(KEY_MESSENGER, new Messenger(new MyHandler(this))); //Pass messenger to service
        startService(intent);
    }

    /***
     * start download
     * @param view
     */
    public void startDownloadTask(View view) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (mJobScheduler == null) {
                mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            }
            JobInfo.Builder builder = new JobInfo.Builder(mJobId, new ComponentName(this, MyJobService.class));
//            builder.setPeriodic(TIME_PERIOD);  //Set the task to run every intervalMillis
//            builder.setOverrideDeadline(TIME_DELEADLINE);//This method allows you to set the latest delay time for the task.。
// If other conditions are not met when the time is up, your task will be activated.。
//            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);  //This method allows you to perform this task only if it meets the specified network conditions.,
//            builder.setRequiresCharging(true);   //This method tells your application that this task will only be executed when the device is charging.
//            builder.setRequiresDeviceIdle(true); //This method tells you that the task will only be started if the user is not using the device and has not used it for a while.
//            builder.setPersisted(true);//This method tells the system whether your task will continue to execute after your device restarts.

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                builder.setMinimumLatency(TIME_PERIOD); //Minimum delay time for execution
                builder.setOverrideDeadline(TIME_PERIOD);  //Maximum delay time to execute
//                builder.setBackoffCriteria(TIME_PERIOD, JobInfo.BACKOFF_POLICY_LINEAR);//Linear retry
            } else {
                builder.setOverrideDeadline(TIME_PERIOD);  //Maximum delay time to execute
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
     * cancel jobScheduler
     *
     * @param view
     */
    public void cancelDownloadTask(View view) {
        if (mJobScheduler != null) {
            mJobScheduler.cancelAll();
        }
    }
}
