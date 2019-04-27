package com.example.meeting.model;

import com.example.library.base.BaseModel;
import com.example.library.helper.RxHelper;
import com.example.meeting.constant.GlobalConstant;
import com.example.meeting.contract.AbMeetingNoticeContract;
import com.example.meeting.db.AppDatabase;
import com.example.meeting.model.entity.MeetingHistory;
import com.example.meeting.model.entity.User;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

import java.util.List;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/27 下午11:34
 * desc   :会议通知
 * version: 1.0
 */
public class MeetingNoticeModel extends BaseModel implements AbMeetingNoticeContract.IMeetingNoticeModel {


    @Override
    public Maybe<List<MeetingHistory>> getYestodayAndTodayInfo() {
        return AppDatabase.getInstance().meetingHistoryDao().getYesterdayAndTodayMeeting(GlobalConstant.VALUE_YESTERDAY_AND_TODAY).compose(RxHelper.<List<MeetingHistory>>rxSchedulerHelper());
    }

    @Override
    public Observable<User> getTomorrowMeetingInfo(final int lastMeetingHostId) {
        return Observable.create(new ObservableOnSubscribe<User>() {

            @Override
            public void subscribe(ObservableEmitter<User> e) throws Exception {
                User firstUser = AppDatabase.getInstance().userDao().getAvaiableUserByUid(GlobalConstant.VALUE_IS_NOT_DELETE, lastMeetingHostId,
                        GlobalConstant.VALUE_IS_NOT_SKIP);
                if (firstUser != null) {
                    e.onNext(firstUser);
                } else {
                    firstUser = AppDatabase.getInstance().userDao().getAvaiableUserByUid(GlobalConstant.VALUE_IS_NOT_DELETE, GlobalConstant.VALUE_USER_ID_DEFAULT,
                            GlobalConstant.VALUE_IS_NOT_SKIP);
                    if (firstUser != null) {
                        e.onNext(firstUser);
                    } else {
                        e.onError(new Throwable());
                    }
                }
            }
        }).compose(RxHelper.<User>rxObservaleSchedulerHelper());

    }
}
