package com.example.meeting.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.support.annotation.NonNull;
import com.example.library.utils.LogUtils;
import com.example.meeting.MeetingApplication;
import com.example.meeting.db.dao.MeetingHistoryDao;
import com.example.meeting.db.dao.UserDao;
import com.example.meeting.model.entity.MeetingHistory;
import com.example.meeting.model.entity.User;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/26 上午10:13
 * desc   :Room database entity class
 * version: 1.0
 */
@Database(entities = {User.class, MeetingHistory.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    private static final String TAG = AppDatabase.class.getSimpleName();

    private static AppDatabase mAppDatabase;

    public abstract UserDao userDao();   //get user dao

    public abstract MeetingHistoryDao meetingHistoryDao(); //meeting history


    /***
     * AppDatabase More resource consumption, so here is a singleton method to get
     * @return
     */
    public static AppDatabase getInstance() {
        if (mAppDatabase == null) {
            synchronized (AppDatabase.class) {
                if (mAppDatabase == null) {
                    mAppDatabase = Room.databaseBuilder(MeetingApplication.getContext(), AppDatabase.class, "meeting_db").addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            LogUtils.d(TAG, "AppDatabase onCreate");
                        }

                        @Override
                        public void onOpen(@NonNull SupportSQLiteDatabase db) {
                            super.onOpen(db);
                            LogUtils.d(TAG, "AppDatabase onOpen");
                        }
                    }).allowMainThreadQueries()                 //Whether to allow the thread to query
                            .fallbackToDestructiveMigration()  //If the database is migrated, an error will occur and the database will be recreated instead of crashing.
                            .build();
                }
            }
        }
        return mAppDatabase;
    }
}
