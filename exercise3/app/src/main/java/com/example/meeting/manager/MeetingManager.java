package com.example.meeting.manager;

import com.example.library.helper.RxHelper;
import com.example.library.manager.RxManager;
import com.example.library.utils.LogUtils;
import com.example.meeting.constant.GlobalConstant;
import com.example.meeting.db.AppDatabase;
import com.example.meeting.model.entity.MeetingHistory;
import com.example.meeting.model.entity.User;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/27 下午6:39
 * desc   : 会议发布管理类
 * version: 1.0
 */
public class MeetingManager {
    private static final String TAG = MeetingManager.class.getSimpleName();
    private static MeetingManager mMeetingManager;
    private static final int VALUE_USER_ID_DEFAULT = 0;
    private RxManager mManager;

    private MeetingManager() {
        mManager = new RxManager();
    }

    public static MeetingManager getInstance() {
        if (mMeetingManager == null) {
            synchronized (MeetingManager.class) {
                if (mMeetingManager == null) {
                    mMeetingManager = new MeetingManager();
                }
            }
        }
        return mMeetingManager;
    }


    /***
     * 发布会议
     */
    public void publishMeeting() {
        int lastMeetingHostId = SPDataManager.getLastMeetingHostId();
        if (lastMeetingHostId == SPDataManager.SPDataConstant.VALUE_LAST_MEETING_HOST_ID_DEFAULT) { //第一次会议
            Maybe<User> firstUser = AppDatabase.getInstance().userDao().getAvaiableUser(GlobalConstant.VALUE_IS_NOT_DELETE, VALUE_USER_ID_DEFAULT, GlobalConstant.VALUE_IS_NOT_SKIP);
            mManager.register(firstUser.compose(RxHelper.<User>rxSchedulerHelper()).observeOn(Schedulers.io()).map(new Function<User, MeetingHistory>() {
                @Override
                public MeetingHistory apply(User user) {
                    MeetingHistory meetingHistory = new MeetingHistory();
                    meetingHistory.setHostTime(System.currentTimeMillis());
                    meetingHistory.setUserId(user.getId());
                    long row = AppDatabase.getInstance().meetingHistoryDao().insertUsers(meetingHistory);//插入历史会议
                    if (row > 0) {
                        SPDataManager.saveLastMeetingHostId(user.getId());
                        return meetingHistory;
                    } else {
                        return null;
                    }
                }
            }).subscribe(new Consumer<MeetingHistory>() {
                @Override
                public void accept(MeetingHistory meetingHistory) throws Exception {
                    LogUtils.e("success-----", meetingHistory.getId());
                }
            }));
        } else {  //已经有人发布会议
            //1.先判断是否要开启新一轮的会议主持
            Maybe<User> firstUser = AppDatabase.getInstance().userDao().getAvaiableUser(GlobalConstant.VALUE_IS_NOT_DELETE, lastMeetingHostId, GlobalConstant.VALUE_IS_NOT_SKIP);
            mManager.register(firstUser.compose(RxHelper.<User>rxSchedulerHelper()).observeOn(Schedulers.io()).map(new Function<User, MeetingHistory>() {
                @Override
                public MeetingHistory apply(User user) {
                    MeetingHistory meetingHistory = new MeetingHistory();
                    meetingHistory.setHostTime(System.currentTimeMillis());
                    meetingHistory.setUserId(user.getId());
                    long row = AppDatabase.getInstance().meetingHistoryDao().insertUsers(meetingHistory);//插入历史会议
                    if (row > 0) {
                        SPDataManager.saveLastMeetingHostId(user.getId());
                        return meetingHistory;
                    } else {
                        return null;
                    }
                }
            }).subscribe(new Consumer<MeetingHistory>() {
                @Override
                public void accept(MeetingHistory meetingHistory) throws Exception {
                    LogUtils.e(TAG, "success-----", meetingHistory.getId());
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    LogUtils.e(TAG, throwable.getLocalizedMessage());

                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    LogUtils.e(TAG, "***************************run");
                    //2.开启新一轮的值日主持
                    resetHostMeeting();
                }
            }));
        }
    }

    /***
     * 重置会议发布状态
     */
    private void resetHostMeeting() {
        RxHelper.createObservable(Void.class).compose(RxHelper.<Class<Void>>rxSchedulerHelperIO()).subscribe(new Consumer<Class<Void>>() {
            @Override
            public void accept(Class<Void> voidClass) throws Exception {
                //重置数据库中的跳过标志位
                AppDatabase.getInstance().userDao().resetSkipStatus(GlobalConstant.VALUE_IS_NOT_SKIP);
                SPDataManager.saveLastMeetingHostId(SPDataManager.SPDataConstant.VALUE_LAST_MEETING_HOST_ID_DEFAULT);
                publishMeeting();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                LogUtils.e(TAG, throwable.getLocalizedMessage());
            }
        });
    }

}
