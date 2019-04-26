package com.example.meeting.model;

import com.example.library.base.BaseModel;
import com.example.library.helper.RxHelper;
import com.example.meeting.contract.AbPersonAddContract;
import com.example.meeting.db.AppDatabase;
import com.example.meeting.model.entity.User;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/26 下午12:37
 * desc   :人员新增model层
 * version: 1.0
 */
public class PersonAddModel extends BaseModel implements AbPersonAddContract.IPersonAddModel {
    @Override
    public Maybe<User> getNewestNumber() {
        return AppDatabase.getInstance().userDao().getNewestNumber().compose(RxHelper.<User>rxSchedulerHelper());
    }
}
