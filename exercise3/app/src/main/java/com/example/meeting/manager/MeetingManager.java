package com.example.meeting.manager;

import com.example.library.helper.RxHelper;
import com.example.library.manager.RxManager;
import com.example.library.utils.LogUtils;
import com.example.library.utils.TimeUtils;
import com.example.meeting.constant.GlobalConstant;
import com.example.meeting.db.AppDatabase;
import com.example.meeting.model.entity.MeetingHistory;
import com.example.meeting.model.entity.NotifyChangedEvent;
import com.example.meeting.model.entity.User;
import io.reactivex.Maybe;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import org.greenrobot.eventbus.EventBus;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/27 下午6:39
 * desc   : meeting publish manager
 * version: 1.0
 */
public class MeetingManager {
    private static final String TAG = MeetingManager.class.getSimpleName();
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


    /**
     * Check if the meeting is released on the day
     */
    public void checkMeetingPublishAvaiablity() {
        long newestMeetingDate = SPDataManager.getNewestMeetingDate();
        if (newestMeetingDate == SPDataManager.SPDataConstant.VALUE_LAST_MEETING_HOST_ID_DEFAULT) { //判断当天未发布过会议
            publishMeeting();
        } else {
            int result = TimeUtils.compareDate(System.currentTimeMillis(), newestMeetingDate);
            if (result == 1) {  //The current date is larger than the last saved date
                publishMeeting();
            }
        }
    }


    /***
     * publish meeting
     */
    public void publishMeeting() {


        //Get the user who last hosted the meeting
        final int lastMeetingHostId = SPDataManager.getLastMeetingHostId();
        if (lastMeetingHostId == SPDataManager.SPDataConstant.VALUE_LAST_MEETING_HOST_ID_DEFAULT) { //first meeting
            Maybe<User> firstUser = AppDatabase.getInstance().userDao().getAvaiableUser(GlobalConstant.VALUE_IS_NOT_DELETE, GlobalConstant.VALUE_USER_ID_DEFAULT, GlobalConstant.VALUE_IS_NOT_SKIP);
            mManager.register(firstUser.compose(RxHelper.<User>rxSchedulerHelper()).observeOn(Schedulers.io()).map(new Function<User, MeetingHistory>() {
                @Override
                public MeetingHistory apply(User user) {
                    long currentTimeStamp = System.currentTimeMillis();
                    MeetingHistory meetingHistory = new MeetingHistory();
                    meetingHistory.setHostTime(currentTimeStamp);
                    meetingHistory.setUserId(user.getId());
                    long row = AppDatabase.getInstance().meetingHistoryDao().insertUsers(meetingHistory);//inser meeting history record
                    if (row > 0) {
                        SPDataManager.saveLastMeetingHostId(user.getId()); //update user id of the host meeting
                        SPDataManager.saveNewestMeetingDate(currentTimeStamp);
                        AppDatabase.getInstance().userDao().resetSkipStatus(GlobalConstant.VALUE_IS_NOT_SKIP, lastMeetingHostId, user.getId());  //更新区间段的用户跳过状态，模拟跳过功能
                        EventBus.getDefault().post(new NotifyChangedEvent(NotifyChangedEvent.NotifyChangeEventConstant.OBJ_PERSON_ADD));
                        return meetingHistory;
                    } else {
                        return null;
                    }
                }
            }).subscribe(new Consumer<MeetingHistory>() {
                @Override
                public void accept(MeetingHistory meetingHistory) throws Exception {
                    LogUtils.e("publishMeeting success", meetingHistory.getId());
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    LogUtils.d(throwable.getLocalizedMessage());
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    //No suitable person found, all members in the certifier list have been set to skip and need to be modified
                    AppDatabase.getInstance().userDao().getPersonTotalCount(GlobalConstant.VALUE_IS_NOT_DELETE).compose(RxHelper.<Integer>rxSchedulerHelper()).subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Exception {
                            if (integer > 0) {
                                resetHostMeeting(GlobalConstant.VALUE_USER_ID_DEFAULT);
                            }
                        }
                    });
                }
            }));
        } else {  //Someone has already released a meeting
            //1.First determine whether you want to start a new round of conference hosting
            Maybe<User> firstUser = AppDatabase.getInstance().userDao().getAvaiableUser(GlobalConstant.VALUE_IS_NOT_DELETE, lastMeetingHostId, GlobalConstant.VALUE_IS_NOT_SKIP);
            mManager.register(firstUser.compose(RxHelper.<User>rxSchedulerHelper()).observeOn(Schedulers.io()).map(new Function<User, MeetingHistory>() {
                @Override
                public MeetingHistory apply(User user) {
                    long currentTimeStamp = System.currentTimeMillis();
                    MeetingHistory meetingHistory = new MeetingHistory();
                    meetingHistory.setHostTime(currentTimeStamp);
                    meetingHistory.setUserId(user.getId());
                    long row = AppDatabase.getInstance().meetingHistoryDao().insertUsers(meetingHistory);//inser meeting history record
                    if (row > 0) {
                        SPDataManager.saveLastMeetingHostId(user.getId());
                        SPDataManager.saveNewestMeetingDate(currentTimeStamp);
                        AppDatabase.getInstance().userDao().resetSkipStatus(GlobalConstant.VALUE_IS_NOT_SKIP, lastMeetingHostId, user.getId());  //Update the user skip state of the interval segment, simulate skip function
                        EventBus.getDefault().post(new NotifyChangedEvent(NotifyChangedEvent.NotifyChangeEventConstant.OBJ_PERSON_ADD));
                        return meetingHistory;
                    } else {
                        return null;
                    }
                }
            }).subscribe(new Consumer<MeetingHistory>() {
                @Override
                public void accept(MeetingHistory meetingHistory) throws Exception {
                    LogUtils.e(TAG, "publishMeeting success", meetingHistory.getId());
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    LogUtils.e(TAG, throwable.getLocalizedMessage());

                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    LogUtils.e(TAG, "publishMeeting run");
                    //2.Open a new round of value hosting
                    resetHostMeeting(lastMeetingHostId);
                }
            }));
        }
    }

    /***
     * Reset meeting release status
     * @param lastMeetingHostId
     */
    private void resetHostMeeting(final int lastMeetingHostId) {
        RxHelper.createObservable(Void.class).compose(RxHelper.<Class<Void>>rxSchedulerHelperIO()).subscribe(new Consumer<Class<Void>>() {
            @Override
            public void accept(Class<Void> voidClass) throws Exception {
                AppDatabase.getInstance().userDao().resetSkipStatus(GlobalConstant.VALUE_IS_NOT_SKIP, lastMeetingHostId);
                SPDataManager.saveLastMeetingHostId(SPDataManager.SPDataConstant.VALUE_LAST_MEETING_HOST_ID_DEFAULT); //还原上一次主次会议的用户标示
                publishMeeting();  //Open a new round of conference hosting
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                LogUtils.e(TAG, throwable.getLocalizedMessage());
            }
        });
    }


    /***
     * Adjust the person is_skip state between start-end to simulate this skipped
     * @param startUid
     * @param endUid
     */
    private void skip(int startUid, int endUid) {

    }

}
