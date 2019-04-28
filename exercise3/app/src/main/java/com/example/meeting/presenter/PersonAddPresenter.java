package com.example.meeting.presenter;

import android.support.annotation.NonNull;
import com.example.library.utils.LogUtils;
import com.example.meeting.contract.AbPersonAddContract;
import com.example.meeting.manager.MeetingManager;
import com.example.meeting.model.PersonAddModel;
import com.example.meeting.model.entity.NotifyChangedEvent;
import com.example.meeting.model.entity.User;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import org.greenrobot.eventbus.EventBus;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/26 下午12:33
 * desc   :人员添加层现层
 * version: 1.0
 */
public class PersonAddPresenter extends AbPersonAddContract.AbPersonAddPresenter {


    @NonNull
    public static PersonAddPresenter newInstance() {
        return new PersonAddPresenter();
    }

    @Override
    public void getNewestNumber() {
//        if (mIView == null || mIModel == null) {
//            return;
//        }
//        mRxManager.register(mIModel.getNewestNumber().subscribe(new Consumer<User>() {
//            @Override
//            public void accept(User integer) throws Exception {
//                mIView.onGetNewestNumberSuccess(integer);
//            }
//        }, new Consumer<Throwable>() {
//            @Override
//            public void accept(Throwable throwable) throws Exception {
//                mIView.onGetNewestNumberFailure(throwable.getMessage());
//            }
//        }, new Action() {
//            @Override
//            public void run() throws Exception {
//                mIView.onGetNewestNumberEmpty();
//                LogUtils.d("*******************");
//            }
//        }));
    }

    @Override
    public void saveUser(final User user) {
        if (mIView == null || mIModel == null) {
            return;
        }
        mRxManager.register(mIModel.saveUser(user).doOnNext(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                if (aLong > 0) {
                    //每次人员新增时，检测一下当前是否适合生成会议记录
                    MeetingManager.getInstance().checkMeetingPublishAvaiablity();
                }
            }
        }).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long integer) throws Exception {
                long id = integer;
                user.setId((int) id);
                mIView.onSavePersonSuccess(user);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mIView.onSavePersonFailure();
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
