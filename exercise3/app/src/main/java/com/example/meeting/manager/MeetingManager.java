package com.example.meeting.manager;

import android.os.SystemClock;
import com.example.library.helper.RxHelper;
import com.example.library.manager.RxManager;
import com.example.library.utils.LogUtils;
import com.example.library.utils.TimeUtils;
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


    /**
     * 检查当天是否满足会议发布
     */
    public void checkMeetingPublishAvaiablity() {
        long newestMeetingDate = SPDataManager.getNewestMeetingDate();
        if (newestMeetingDate == SPDataManager.SPDataConstant.VALUE_LAST_MEETING_HOST_ID_DEFAULT) { //判断当天未发布过会议
            publishMeeting();
        } else {
            int result = TimeUtils.compareDate(System.currentTimeMillis(), newestMeetingDate);
            if (result == 1) {  //当前的日期比上一次保存的日期大
                publishMeeting();
            }
        }
    }


    /***
     * 发布会议
     */
    public void publishMeeting() {


        //获取上一次主持会议的用户
        final int lastMeetingHostId = SPDataManager.getLastMeetingHostId();
        if (lastMeetingHostId == SPDataManager.SPDataConstant.VALUE_LAST_MEETING_HOST_ID_DEFAULT) { //第一次会议
            Maybe<User> firstUser = AppDatabase.getInstance().userDao().getAvaiableUser(GlobalConstant.VALUE_IS_NOT_DELETE, VALUE_USER_ID_DEFAULT, GlobalConstant.VALUE_IS_NOT_SKIP);
            mManager.register(firstUser.compose(RxHelper.<User>rxSchedulerHelper()).observeOn(Schedulers.io()).map(new Function<User, MeetingHistory>() {
                @Override
                public MeetingHistory apply(User user) {
                    long currentTimeStamp = System.currentTimeMillis();
                    MeetingHistory meetingHistory = new MeetingHistory();
                    meetingHistory.setHostTime(currentTimeStamp);
                    meetingHistory.setUserId(user.getId());
                    long row = AppDatabase.getInstance().meetingHistoryDao().insertUsers(meetingHistory);//插入历史会议
                    if (row > 0) {
                        SPDataManager.saveLastMeetingHostId(user.getId()); //更新主持会议的用户
                        SPDataManager.saveNewestMeetingDate(currentTimeStamp);
                        AppDatabase.getInstance().userDao().resetSkipStatus(GlobalConstant.VALUE_IS_NOT_SKIP, lastMeetingHostId, user.getId());  //更新区间段的用户跳过状态，模拟跳过功能
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
                    long currentTimeStamp = System.currentTimeMillis();
                    MeetingHistory meetingHistory = new MeetingHistory();
                    meetingHistory.setHostTime(currentTimeStamp);
                    meetingHistory.setUserId(user.getId());
                    long row = AppDatabase.getInstance().meetingHistoryDao().insertUsers(meetingHistory);//插入历史会议
                    if (row > 0) {
                        SPDataManager.saveLastMeetingHostId(user.getId());
                        SPDataManager.saveNewestMeetingDate(currentTimeStamp);
                        AppDatabase.getInstance().userDao().resetSkipStatus(GlobalConstant.VALUE_IS_NOT_SKIP, lastMeetingHostId, user.getId());  //更新区间段的用户跳过状态，模拟跳过功能
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
                    resetHostMeeting(lastMeetingHostId);
                }
            }));
        }
    }

    /***
     * 重置会议发布状态
     * @param lastMeetingHostId
     */
    private void resetHostMeeting(final int lastMeetingHostId) {
        RxHelper.createObservable(Void.class).compose(RxHelper.<Class<Void>>rxSchedulerHelperIO()).subscribe(new Consumer<Class<Void>>() {
            @Override
            public void accept(Class<Void> voidClass) throws Exception {
                AppDatabase.getInstance().userDao().resetSkipStatus(GlobalConstant.VALUE_IS_NOT_SKIP,lastMeetingHostId);
                SPDataManager.saveLastMeetingHostId(SPDataManager.SPDataConstant.VALUE_LAST_MEETING_HOST_ID_DEFAULT); //还原上一次主次会议的用户标示
                publishMeeting();  //开启新一轮的会议主持
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                LogUtils.e(TAG, throwable.getLocalizedMessage());
            }
        });
    }


    /***
     * 将start-end 之间的人员is_skip 状态调整，从而模拟本次已跳过
     * @param startUid
     * @param endUid
     */
    private void skip(int startUid, int endUid) {

    }

}
