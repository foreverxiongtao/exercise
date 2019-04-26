package com.example.meeting.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.support.annotation.NonNull;
import com.example.library.utils.LogUtils;
import com.example.meeting.MeetingApplication;
import com.example.meeting.db.dao.UserDao;
import com.example.meeting.model.entity.User;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/26 上午10:13
 * desc   :room数据库实体类
 * version: 1.0
 */
@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static final String TAG = AppDatabase.class.getSimpleName();

    private static AppDatabase mAppDatabase;

    public abstract UserDao userDao();   //获取userdao层


    /***
     * AppDatabase 比较消耗资源，所以这里采用单例方式来获取
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
                            LogUtils.d(TAG, "**********onCreate*********");
                        }

                        @Override
                        public void onOpen(@NonNull SupportSQLiteDatabase db) {
                            super.onOpen(db);
                            LogUtils.d(TAG, "**********onOpen*********");
                        }
                    }).allowMainThreadQueries()                 //是否允许在祝线程进行查询
                            .fallbackToDestructiveMigration()  //迁移数据库如果发生错误，将会重新创建数据库，而不是发生崩溃
                            .build();
                }
            }
        }
        return mAppDatabase;
    }
}
