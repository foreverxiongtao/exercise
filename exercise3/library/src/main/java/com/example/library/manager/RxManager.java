package com.example.library.manager;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
/**
 *    author : desperado
 *    e-mail : foreverxiongtao@sina.com
 *    date   : 2019/4/26 下午12:09
 *    desc   : 用于管理Rxjava 注册订阅和取消订阅
 *    version: 1.0
 */
public class RxManager {

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();// 管理订阅者者

    public void register(Disposable d) {
        mCompositeDisposable.add(d);
    }

    public void unSubscribe() {
        mCompositeDisposable.dispose();// 取消订阅
    }

}
