package com.example.meeting.presenter;

import android.support.annotation.NonNull;
import com.example.meeting.contract.AbPersonAddContract;
import com.example.meeting.manager.MeetingManager;
import com.example.meeting.model.PersonAddModel;
import com.example.meeting.model.entity.User;
import io.reactivex.functions.Consumer;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/26 下午12:33
 * desc   :person add presenter
 * version: 1.0
 */
public class PersonAddPresenter extends AbPersonAddContract.AbPersonAddPresenter {


    @NonNull
    public static PersonAddPresenter newInstance() {
        return new PersonAddPresenter();
    }

    @Override
    public void getNewestNumber() {
    }

    @Override
    public void saveUser(final User user) {
        if (mView == null || mModel == null) {
            return;
        }
        mRxManager.register(mModel.saveUser(user).doOnNext(new Consumer<Long>() {
            @Override
            public void accept(Long row) throws Exception {
                if (row > 0) {
                    //Every time a new person is added, check if it is suitable for generating a meeting record.
                    MeetingManager.getInstance().checkMeetingPublishAvaiablity();
                }
            }
        }).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long row) throws Exception {
                long id = row;
                user.setId((int) id);
                mView.onSavePersonSuccess(user);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mView.onSavePersonFailure();
            }
        }));
    }

    @Override
    public AbPersonAddContract.IPersonAddModel getModel() {
        return new PersonAddModel();
    }

    @Override
    public void onStart() {

    }
}
