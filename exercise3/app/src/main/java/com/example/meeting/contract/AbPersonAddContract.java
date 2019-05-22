package com.example.meeting.contract;

import com.example.library.base.BasePresenter;
import com.example.library.base.IBaseModel;
import com.example.library.base.IBaseView;
import com.example.meeting.model.entity.User;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/26 下午12:28
 * desc   :person add contract
 * version: 1.0
 */
public interface AbPersonAddContract {

    abstract class AbPersonAddPresenter extends BasePresenter<IPersonAddModel, IPersonalAddView> {

        public abstract void getNewestNumber();

        public abstract void saveUser(User user);

    }

    interface IPersonAddModel extends IBaseModel {

        Maybe<User> getNewestNumber();

        Observable<Long> saveUser(User user);
    }

    interface IPersonalAddView extends IBaseView {

        void onSavePersonSuccess(User user);

        void onSavePersonFailure();

    }
}
