package com.example.meeting.model;

import android.util.Log;
import com.example.library.base.BaseModel;
import com.example.library.helper.RxHelper;
import com.example.meeting.contract.AbPersonAddContract;
import com.example.meeting.db.AppDatabase;
import com.example.meeting.model.entity.User;
import io.reactivex.*;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/26 下午12:37
 * desc   :person add model
 * version: 1.0
 */
public class PersonAddModel extends BaseModel implements AbPersonAddContract.IPersonAddModel {
    @Override
    public Maybe<User> getNewestNumber() {
        return AppDatabase.getInstance().userDao().getNewestNumber().compose(RxHelper.<User>rxSchedulerHelper());
    }

    @Override
    public Observable<Long> saveUser(final User user) {
        Observable<Long> observable = Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> e) throws Exception {
                Long id = AppDatabase.getInstance().userDao().insertUsers(user);
                if (id > 0) {
                    e.onNext(id);
                } else {
                    e.onError(new Throwable());
                }
            }
        }).compose(RxHelper.<Long>rxObservaleSchedulerHelper());
        return observable;
    }
}
