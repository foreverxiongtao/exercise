package com.example.meeting.presenter;

import android.content.Intent;
import android.support.annotation.NonNull;
import com.example.library.utils.LogUtils;
import com.example.meeting.MeetingApplication;
import com.example.meeting.service.TaskService;
import com.example.meeting.constant.GlobalConstant;
import com.example.meeting.contract.AbMeetingHistoryContract;
import com.example.meeting.model.MeetingHistoryModel;
import com.example.meeting.model.entity.MeetingHistory;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

import java.util.List;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/27 上午12:15
 * desc   :person management presenter
 * version: 1.0
 */
public class MeetingHistoryPresenter extends AbMeetingHistoryContract.AbMeetingHistoryPresenter {

    private int mCurrentPage;
    private int mTotalPage;
    private boolean isLoading;

    @NonNull
    public static MeetingHistoryPresenter newInstance() {
        return new MeetingHistoryPresenter();
    }

    @Override
    public AbMeetingHistoryContract.IMeetingHistoryModel getModel() {
        return new MeetingHistoryModel();
    }

    @Override
    public void onStart() {

    }

    /**
     * Get history data
     */
    @Override
    public void refreshMeetingHistoryList() {
        if (mModel == null || mView == null) {
            return;
        }
        mCurrentPage = 0;
        mRxManager.register(mModel.getMeetingHistory(mCurrentPage).subscribe(new Consumer<List<MeetingHistory>>() {
            @Override
            public void accept(List<MeetingHistory> list) throws Exception {
                if (list != null && !list.isEmpty()) {
                    mCurrentPage++;
                    mView.refreshMeetingHistorySuccess(list);
                } else {
                    mView.getHistoryListEmpty();
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mView.refreshMeetingHistoryFailure(throwable.getMessage());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                mView.getHistoryListEmpty();
                LogUtils.d("refreshMeetingHistoryList failure");
            }
        }));
    }


    @Override
    public void refreshMeetingHistoryCount() {
        if (mModel == null || mView == null) {
            return;
        }
        mRxManager.register(mModel.getMeetingHistoryTotalCount().subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer totalPage) throws Exception {
                mTotalPage = totalPage / GlobalConstant.VALUE_PAGING_DEFAULT + 1;  //Get total pagination
                //The number of meeting records is greater than the maximum number required to be set,
                // and the oldest one of the data in the database is deleted.
                if (totalPage > GlobalConstant.VALUE_DATA_MAX_COUNT) {
                    Intent intent = new Intent(MeetingApplication.getContext(), TaskService.class);
                    intent.putExtra(GlobalConstant.KEY_TASK_SERVICE, GlobalConstant.ARGUMENT_DELETE_OLD_DATA);
                    MeetingApplication.getContext().startService(intent);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mView.refreshMeetingHistoryFailure(throwable.getMessage());
            }
        }));
    }


    /***
     * load more for meeting list
     */
    @Override
    public void loadMoreMeetingHistoryList() {
        if (mModel == null || mView == null) {
            return;
        }
        if (mTotalPage >= mCurrentPage && !isLoading) {
            isLoading = true;
            mRxManager.register(mModel.getMeetingHistory(mCurrentPage).subscribe(new Consumer<List<MeetingHistory>>() {
                @Override
                public void accept(List<MeetingHistory> list) throws Exception {
                    isLoading = false;
                    if (list != null && !list.isEmpty()) {
                        mCurrentPage++;
                        mView.moreMeetingHistorySuccess(list);
                    } else {
                        mView.showNoMoreData();
                    }
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    isLoading = false;
                    mView.moreMeetingHistoryFailure(throwable.getMessage());
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    isLoading = false;
                    mView.showNoMoreData();
                    LogUtils.d("loadMoreMeetingHistoryList failure");
                }
            }));
        }
    }
}
