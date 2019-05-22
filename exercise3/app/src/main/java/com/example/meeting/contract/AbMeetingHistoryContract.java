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
 * date   : 2019/4/27 下午3:52
 * desc   : meeting history contract
 * version: 1.0
 */
public interface AbMeetingHistoryContract {

    abstract class AbMeetingHistoryPresenter extends BasePresenter<IMeetingHistoryModel, IMeetingHistoryView> {

        public abstract void refreshMeetingHistoryList();

        public abstract void refreshMeetingHistoryCount();

        public abstract void loadMoreMeetingHistoryList(); //page load more

    }

    interface IMeetingHistoryModel extends IBaseModel {

        Maybe<List<MeetingHistory>> getMeetingHistory(int page); //page meeting history

        Maybe<Integer> getMeetingHistoryTotalCount();  //get meeting history count
    }

    interface IMeetingHistoryView extends IBaseView {

        void refreshMeetingHistorySuccess(List<MeetingHistory> user);

        void refreshMeetingHistoryFailure(String message);


        void moreMeetingHistorySuccess(List<MeetingHistory> user);

        void moreMeetingHistoryFailure(String message);

        /**
         * no more data
         */
        void showNoMoreData();


        /***
         * history list empty
         */
        void getHistoryListEmpty();
    }
}
