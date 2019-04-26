package com.example.meeting.presenter;

import com.example.library.utils.LogUtils;
import com.example.meeting.contract.AbPersonAddContract;
import com.example.meeting.model.PersonAddModel;
import com.example.meeting.model.entity.User;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/26 下午12:33
 * desc   :人员添加层现层
 * version: 1.0
 */
public class PersonAddPresenter extends AbPersonAddContract.AbPersonAddPresenter {
    @Override
    public void getNewestNumber() {
        if (mIView == null || mIModel == null) {
            return;
        }
        mRxManager.register(mIModel.getNewestNumber().subscribe(new Consumer<User>() {
            @Override
            public void accept(User integer) throws Exception {
                mIView.onGetNewestNumberSuccess(integer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mIView.onGetNewestNumberFailure(throwable.getMessage());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                LogUtils.e("*******************");
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
