package com.example.meeting.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import com.example.meeting.constant.GlobalConstant;
import com.example.meeting.db.AppDatabase;
import com.example.meeting.model.entity.NotifyChangedEvent;
import org.greenrobot.eventbus.EventBus;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/28 上午11:48
 * desc   : customer task service handler
 * version: 1.0
 */
public class TaskService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public TaskService(String name) {
        super(name);
    }

    public TaskService() {
        super("task_service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        int key_task = intent.getIntExtra(GlobalConstant.KEY_TASK_SERVICE, GlobalConstant.VALUE_TASK_DEFAULT);
        switch (key_task) {
            case GlobalConstant.ARGUMENT_DELETE_OLD_DATA:
                AppDatabase.getInstance().meetingHistoryDao().deleteOldestHistory();
                EventBus.getDefault().post(new NotifyChangedEvent(NotifyChangedEvent.NotifyChangeEventConstant.OBJ_DELETE_HISTORY));
                break;
        }
    }
}
