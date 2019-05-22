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
 * desc   :person management presenter
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
     * refresh user list
     */
    @Override
    public void refreshUserList() {
        if (mModel == null || mView == null) {
            return;
        }
        mCurrentPage = 0;
        mRxManager.register(mModel.getUsers(mCurrentPage).subscribe(new Consumer<List<User>>() {
            @Override
            public void accept(List<User> list) throws Exception {
                if (list != null && !list.isEmpty()) {
                    mCurrentPage++;
                    mView.refreshUserListSuccess(list);
                } else {
                    mView.getUserListrEmpty();
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mView.refreshUserListFailure(throwable.getMessage());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                mView.getUserListrEmpty();
                LogUtils.d("refreshUserList failure");
            }
        }));
    }

    @Override
    public void refreshUserTotalCount() {
        if (mModel == null || mView == null) {
            return;
        }
        mRxManager.register(mModel.getPersonTotalCount().subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer totalPage) throws Exception {
                mTotalPage = totalPage / GlobalConstant.VALUE_PAGING_DEFAULT + 1;  //get the person total count
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mView.refreshUserListFailure(throwable.getMessage());
            }
        }));
    }


    /**
     * load more for user list
     */
    @Override
    public void loadMoreUserList() {
        if (mModel == null || mView == null) {
            return;
        }
        if (mTotalPage >= mCurrentPage && !isLoading) {
            isLoading = true;
            mRxManager.register(mModel.getUsers(mCurrentPage).subscribe(new Consumer<List<User>>() {
                @Override
                public void accept(List<User> list) throws Exception {
                    isLoading = false;
                    if (list != null && !list.isEmpty()) {
                        mCurrentPage++;
                        mView.moreUserListSuccess(list);
                    } else {
                        mView.showNoMoreData();
                    }
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    isLoading = false;
                    mView.moreUserListFailure(throwable.getMessage());
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    isLoading = false;
                    mView.showNoMoreData();
                    LogUtils.d("loadMoreUserList failed");
                }
            }));
        }
    }


    /**
     * modify user delete status
     *
     * @param user peronson
     */
    @Override
    public void deletePerson(User user, final int position) {
        if (mModel == null || mView == null) {
            return;
        }
        user.setIsDelete(GlobalConstant.VALUE_IS_DELETE);  //Set the current user deletion status
        mRxManager.register(mModel.updatePerson(user).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer row) throws Exception {
                mView.onDeletePersonSuccess(position);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mView.onDeletePersonFailure();
            }
        }));
    }

    @Override
    public void skipMeeting(final User user, final int position) {
        if (mModel == null || mView == null) {
            return;
        }
        user.setIsSkip(GlobalConstant.VALUE_IS_SKIP);  //Set the current user's status to skip status
        mRxManager.register(mModel.updatePerson(user).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer row) throws Exception {
                mView.onSkipMeetingSuccess(user, position);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mView.onSkipMeetingFailure();
            }
        }));
    }
}
