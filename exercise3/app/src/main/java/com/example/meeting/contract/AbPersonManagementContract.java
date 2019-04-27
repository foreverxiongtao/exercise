package com.example.meeting.contract;

import com.example.library.base.BasePresenter;
import com.example.library.base.IBaseModel;
import com.example.library.base.IBaseView;
import com.example.meeting.model.entity.User;
import io.reactivex.Maybe;
import io.reactivex.Observable;

import java.util.List;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/26 下午12:28
 * desc   :人员新增通信层
 * version: 1.0
 */
public interface AbPersonManagementContract {

    abstract class AbPersonManagementPresenter extends BasePresenter<IPersonManagementModel, IPersonalManagementView> {

        public abstract void refreshUserList();

        public abstract void refreshUserTotalCount();

        public abstract void loadMoreUserList();

        public abstract void deletePerson(User user, int position);

        public abstract void skipMeeting(User user, int position);

    }

    interface IPersonManagementModel extends IBaseModel {

        Maybe<List<User>> getUsers(int page);

        Observable<Integer> updatePerson(User user);

        Maybe<Integer> getPersonTotalCount();
    }

    interface IPersonalManagementView extends IBaseView {

        void refreshUserListSuccess(List<User> user);

        void refreshUserListFailure(String message);


        void moreUserListSuccess(List<User> user);

        void moreUserListFailure(String message);

        /**
         * 显示没有更多数据
         */
        void showNoMoreData();

        void getUserListrEmpty();

        void onDeletePersonSuccess(int position);

        void onDeletePersonFailure();


        void onSkipMeetingSuccess(User user, int position);

        void onSkipMeetingFailure();

    }
}
