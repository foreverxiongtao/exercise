package com.example.meeting.model;

import com.example.library.base.BaseModel;
import com.example.library.helper.RxHelper;
import com.example.meeting.constant.GlobalConstant;
import com.example.meeting.contract.AbPersonManagementContract;
import com.example.meeting.db.AppDatabase;
import com.example.meeting.model.entity.User;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

import java.util.List;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/27 上午12:21
 * desc   : person management model
 * version: 1.0
 */
public class PersonManagementModel extends BaseModel implements AbPersonManagementContract.IPersonManagementModel {
    @Override
    public Maybe<List<User>> getUsers(int page) {
        return AppDatabase.getInstance().userDao().getAllUsers(GlobalConstant.VALUE_IS_NOT_DELETE, page * GlobalConstant.VALUE_PAGING_DEFAULT - 1, GlobalConstant.VALUE_PAGING_DEFAULT).compose(RxHelper.<List<User>>rxSchedulerHelper());
    }

    @Override
    public Observable<Integer> updatePerson(final User user) {
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                int id = AppDatabase.getInstance().userDao().updateUser(user);
                if (id > 0) {
                    e.onNext(id);
                } else {
                    e.onError(new Throwable());
                }
            }
        }).compose(RxHelper.<Integer>rxObservaleSchedulerHelper());
        return observable;
    }

    @Override
    public Maybe<Integer> getPersonTotalCount() {
        return AppDatabase.getInstance().userDao().getPersonTotalCount(GlobalConstant.VALUE_IS_NOT_DELETE).compose(RxHelper.<Integer>rxSchedulerHelper());
    }
}
