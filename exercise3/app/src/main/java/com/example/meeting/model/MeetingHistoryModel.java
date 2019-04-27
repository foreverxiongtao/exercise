package com.example.meeting.model;

import com.example.library.base.BaseModel;
import com.example.library.helper.RxHelper;
import com.example.meeting.constant.GlobalConstant;
import com.example.meeting.contract.AbMeetingHistoryContract;
import com.example.meeting.db.AppDatabase;
import com.example.meeting.model.entity.MeetingHistory;
import com.example.meeting.model.entity.User;
import com.example.meeting.ui.fragment.HistoryFragment;
import io.reactivex.Maybe;

import java.util.List;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/27 下午3:58
 * desc   :会议历史
 * version: 1.0
 */
public class MeetingHistoryModel extends BaseModel implements AbMeetingHistoryContract.IMeetingHistoryModel {
    @Override
    public Maybe<List<MeetingHistory>> getMeetingHistory(int page) {
        return AppDatabase.getInstance().meetingHistoryDao().getMeetingHistory(page * GlobalConstant.VALUE_PAGING_DEFAULT - 1, GlobalConstant.VALUE_PAGING_DEFAULT).compose(RxHelper.<List<MeetingHistory>>rxSchedulerHelper());
    }

    @Override
    public Maybe<Integer> getMeetingHistoryTotalCount() {
        return AppDatabase.getInstance().meetingHistoryDao().getHistoryTotalCount().compose(RxHelper.<Integer>rxSchedulerHelper());
    }
}
