package com.example.meeting.presenter;

import android.support.annotation.NonNull;
import com.example.library.utils.LogUtils;
import com.example.meeting.constant.GlobalConstant;
import com.example.meeting.contract.AbPersonAddContract;
import com.example.meeting.contract.AbPersonManagementContract;
import com.example.meeting.model.PersonAddModel;
import com.example.meeting.model.PersonManagementModel;
import com.example.meeting.model.entity.User;
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
public class PersonManagementPresenter extends AbPersonManagementContract.AbPersonManagementPresenter {

    private int mCurrentPage;
    private int mTotalPage;
    private boolean isLoading;

    @NonNull
    public static PersonManagementPresenter newInstance() {
        return new PersonManagementPresenter();
    }

    @Override
    public AbPersonManagementContract.IPersonManagementModel getModel() {
        return new PersonManagementModel();
    }

    @Override
    public void onStart() {

    }

    /***
     * 刷新人员列表
     */
    @Override
    public void refreshUserList() {
        if (mIModel == null || mIView == null) {
            return;
        }
        mCurrentPage = 0;
        mRxManager.register(mIModel.getUsers(mCurrentPage).subscribe(new Consumer<List<User>>() {
            @Override
            public void accept(List<User> list) throws Exception {
                if (list != null && !list.isEmpty()) {
                    mCurrentPage++;
                    mIView.refreshUserListSuccess(list);
                } else {
                    mIView.getUserListrEmpty();
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mIView.refreshUserListFailure(throwable.getMessage());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                mIView.getUserListrEmpty();
                LogUtils.d("*******************");
            }
        }));
    }

    @Override
    public void refreshUserTotalCount() {
        if (mIModel == null || mIView == null) {
            return;
        }
        mRxManager.register(mIModel.getPersonTotalCount().subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer totalPage) throws Exception {
                mTotalPage = totalPage / GlobalConstant.VALUE_PAGING_DEFAULT + 1;  //获取总共的分页
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mIView.refreshUserListFailure(throwable.getMessage());
            }
        }));
    }


    /**
     * 分页加载人员列表
     */
    @Override
    public void loadMoreUserList() {
        if (mIModel == null || mIView == null) {
            return;
        }
        if (mTotalPage >= mCurrentPage && !isLoading) {
            isLoading = true;
            mRxManager.register(mIModel.getUsers(mCurrentPage).subscribe(new Consumer<List<User>>() {
                @Override
                public void accept(List<User> list) throws Exception {
                    isLoading = false;
                    if (list != null && !list.isEmpty()) {
                        mCurrentPage++;
                        mIView.moreUserListSuccess(list);
                    } else {
                        mIView.showNoMoreData();
                    }
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    isLoading = false;
                    mIView.moreUserListFailure(throwable.getMessage());
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


    /**
     * 更改用户的删除状态
     *
     * @param user 人员
     */
    @Override
    public void deletePerson(User user, final int position) {
        if (mIModel == null || mIView == null) {
            return;
        }
        user.setIsDelete(GlobalConstant.VALUE_IS_DELETE);  //设置当前的用户删除状态
        mRxManager.register(mIModel.updatePerson(user).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer row) throws Exception {
                mIView.onDeletePersonSuccess(position);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mIView.onDeletePersonFailure();
            }
        }));
    }

    @Override
    public void skipMeeting(final User user, final int position) {
        if (mIModel == null || mIView == null) {
            return;
        }
        user.setIsSkip(GlobalConstant.VALUE_IS_SKIP);  //设置当前的用户的状态为跳过状态
        mRxManager.register(mIModel.updatePerson(user).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer row) throws Exception {
                mIView.onSkipMeetingSuccess(user, position);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mIView.onSkipMeetingFailure();
            }
        }));
    }
}
