package com.example.exercise2;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;


/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/25 下午1:19
 * desc   :自定义任务调度服务
 * version: 1.0
 */
public class MyJobService extends JobService {

    private Messenger mMessenger;
    private static final String TAG = MyJobService.class.getSimpleName();
    private static final int TIME_DELAY = 1000 * 5;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mMessenger = intent.getParcelableExtra(MainActivity.KEY_MESSENGER);
        return START_NOT_STICKY;
    }


    @Override
    public boolean onStartJob(JobParameters params) {
        //返回false，表示无耗时操作，任务已经执行完毕。
        // 返回true，表示可能执行耗时操作，需要开发者手动去调用jobFinished(JobParameters params, boolean needsRescheduled)来通知系统。
        new DownloadAsyncTask(mMessenger, params).execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        // 返回false来销毁这个工作
        return false;
    }


    class DownloadAsyncTask extends AsyncTask<Void, Void, Void> {

        private JobParameters mJobParams;
        private Messenger mMessenger;

        DownloadAsyncTask(Messenger messenger, JobParameters parameters) {
            this.mMessenger = messenger;
            this.mJobParams = parameters;
        }

        private void sendMessage() {
            SystemClock.sleep(TIME_DELAY);  //模拟耗时操作
            if (mMessenger == null) {
                Log.d(TAG, "Service is bound, not started. There's no callback to send a message to.");
                return;
            }
            Message m = Message.obtain();
            m.obj = MyJobService.this.getResources().getString(R.string.str_download_completed);
            try {
                mMessenger.send(m);
            } catch (RemoteException e) {
                Log.e(TAG, "Error passing service object back to activity.");
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            sendMessage();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //eedsRescheduled参数是告诉系统这个任务如果由于某些原因导致执行失败是否需要重新调度执行，true需要重新调度执行，false不需要
            jobFinished(mJobParams, true);
        }
    }
}
