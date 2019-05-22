package com.example.meeting.presenter;

import android.support.annotation.NonNull;
import com.example.library.utils.LogUtils;
import com.example.meeting.contract.AbMeetingNoticeContract;
import com.example.meeting.model.MeetingNoticeModel;
import com.example.meeting.model.entity.MeetingHistory;
import com.example.meeting.model.entity.User;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

import java.util.List;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/27 下午11:36
 * desc   : meeting notice presenter
 * version: 1.0
 */
public class MeetingNoticePresenter extends AbMeetingNoticeContract.AbMeetingNoticePresenter {

    @NonNull
    public static MeetingNoticePresenter newInstance() {
        return new MeetingNoticePresenter();
    }

    @Override
    public AbMeetingNoticeContract.IMeetingNoticeModel getModel() {
        return new MeetingNoticeModel();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void getYestodayAndTodayInfo() {
        if (mModel == null || mView == null) {
            return;
        }
        mRxManager.register(mModel.getYestodayAndTodayInfo().subscribe(new Consumer<List<MeetingHistory>>() {
            @Override
            public void accept(List<MeetingHistory> list) throws Exception {
                if (list != null && !list.isEmpty()) {
                    mView.getYesterdayAndTodayMeeting(list);
                } else {
                    mView.getYesterdayAndTodayMeetinFaiure();
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mView.getYesterdayAndTodayMeetinFaiure();
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                mView.getYesterdayAndTodayMeetinFaiure();
                LogUtils.d("getYestodayAndTodayInfo failure");
            }
        }));
    }

    @Override
    public void getTomorrowMeetingInfo(int lastMeetingHostId) {
        if (mModel == null || mView == null) {
            return;
        }

        mRxManager.register(mModel.getTomorrowMeetingInfo(lastMeetingHostId).subscribe(new Consumer<User>() {
            @Override
            public void accept(User user) throws Exception {
                mView.getTomorrowMeeting(user);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mView.getTomorrowMeetingFailure();
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                mView.getTomorrowMeetingFailure();
                LogUtils.d("getTomorrowMeetingInfo failure");
            }
        }));
    }
}
