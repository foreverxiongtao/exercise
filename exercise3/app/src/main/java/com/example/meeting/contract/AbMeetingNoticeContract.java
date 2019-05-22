package com.example.meeting.contract;

import com.example.library.base.BasePresenter;
import com.example.library.base.IBaseModel;
import com.example.library.base.IBaseView;
import com.example.meeting.model.entity.MeetingHistory;
import com.example.meeting.model.entity.User;
import io.reactivex.Maybe;
import io.reactivex.Observable;

import java.util.List;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/27 下午11:30
 * desc   : meeting notice contract
 * version: 1.0
 */
public interface AbMeetingNoticeContract {

    abstract class AbMeetingNoticePresenter extends BasePresenter<IMeetingNoticeModel, IMeetingNoticeView> {

        public abstract void getYestodayAndTodayInfo();  //get the arrangement today and yestory

        public abstract void getTomorrowMeetingInfo(int lastMeetingHostId);  //Get tomorrow meeting schedule information

    }

    interface IMeetingNoticeModel extends IBaseModel {

        Maybe<List<MeetingHistory>> getYestodayAndTodayInfo();

        Observable<User> getTomorrowMeetingInfo(int lastMeetingHostId);

    }

    interface IMeetingNoticeView extends IBaseView {

        void getYesterdayAndTodayMeeting(List<MeetingHistory> user);

        void getYesterdayAndTodayMeetinFaiure();

        void getTomorrowMeeting(User user);

        void getTomorrowMeetingFailure();


    }
}
