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
 * desc   :custom job service
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
        //return false，Indicates that there is no time-consuming operation and the task has been executed.。
        //return true，Indicates that it is possible to perform time-consuming operations, which requires the developer to manually call jobFinished(JobParameters params, boolean needsRescheduled)
        new DownloadAsyncTask(mMessenger, params).execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        // Return false to destroy this job
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
            SystemClock.sleep(TIME_DELAY);  //Simulated time-consuming operation
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
            //Tell the system that if the task fails for some reason, it needs to re-schedule execution, true needs to re-schedule execution, false does not need
            jobFinished(mJobParams, true);
        }
    }
}
