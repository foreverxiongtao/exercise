package com.example.meeting.manager;

import com.example.library.helper.RxHelper;
import com.example.library.manager.RxManager;
import com.example.library.utils.LogUtils;
import com.example.meeting.constant.GlobalConstant;
import com.example.meeting.db.AppDatabase;
import com.example.meeting.model.entity.MeetingHistory;
import com.example.meeting.model.entity.User;
import io.reactivex.Maybe;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MeetingManager {
    private static MeetingManager mMeetingManager;
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
        //第一次会议
        if (lastMeetingHostId == SPDataManager.SPDataConstant.VALUE_LAST_MEETING_HOST_ID_DEFAULT) {
            Maybe<User> firstUser = AppDatabase.getInstance().userDao().getAvaiableUser(GlobalConstant.VALUE_IS_NOT_DELETE, 0, GlobalConstant.VALUE_IS_NOT_SKIP);
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
                    LogUtils.e("success-----", meetingHistory.getId());
                }
            }));
        }

    }

}
