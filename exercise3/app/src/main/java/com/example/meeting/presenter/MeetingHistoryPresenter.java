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
 * desc   :人员管理层现层
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
     * 获取历史记录数据
     */
    @Override
    public void refreshMeetingHistoryList() {
        if (mIModel == null || mIView == null) {
            return;
        }
        mCurrentPage = 0;
        mRxManager.register(mIModel.getMeetingHistory(mCurrentPage).subscribe(new Consumer<List<MeetingHistory>>() {
            @Override
            public void accept(List<MeetingHistory> list) throws Exception {
                if (list != null && !list.isEmpty()) {
                    mCurrentPage++;
                    mIView.refreshMeetingHistorySuccess(list);
                } else {
                    mIView.getHistoryListEmpty();
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mIView.refreshMeetingHistoryFailure(throwable.getMessage());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                mIView.getHistoryListEmpty();
                LogUtils.d("*******************");
            }
        }));
    }


    @Override
    public void refreshMeetingHistoryCount() {
        if (mIModel == null || mIView == null) {
            return;
        }
        mRxManager.register(mIModel.getMeetingHistoryTotalCount().subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer totalPage) throws Exception {
                mTotalPage = totalPage / GlobalConstant.VALUE_PAGING_DEFAULT + 1;  //获取总共的分页
                //会议记录的条数大于要求设置的最大数，删除数据库中的以前的最旧的一条数据
                if (totalPage > GlobalConstant.VALUE_DATA_MAX_COUNT) {
                    Intent intent = new Intent(MeetingApplication.getContext(), TaskService.class);
                    intent.putExtra(GlobalConstant.KEY_TASK_SERVICE, GlobalConstant.ARGUMENT_DELETE_OLD_DATA);
                    MeetingApplication.getContext().startService(intent);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mIView.refreshMeetingHistoryFailure(throwable.getMessage());
            }
        }));
    }


    /***
     * 分页加载更多会议历史
     */
    @Override
    public void loadMoreMeetingHistoryList() {
        if (mIModel == null || mIView == null) {
            return;
        }
        if (mTotalPage >= mCurrentPage && !isLoading) {
            isLoading = true;
            mRxManager.register(mIModel.getMeetingHistory(mCurrentPage).subscribe(new Consumer<List<MeetingHistory>>() {
                @Override
                public void accept(List<MeetingHistory> list) throws Exception {
                    isLoading = false;
                    if (list != null && !list.isEmpty()) {
                        mCurrentPage++;
                        mIView.moreMeetingHistorySuccess(list);
                    } else {
                        mIView.showNoMoreData();
                    }
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    isLoading = false;
                    mIView.moreMeetingHistoryFailure(throwable.getMessage());
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    isLoading = false;
                    mIView.showNoMoreData();
                    LogUtils.d("*******************");
                }
            }));
        }
    }
}
