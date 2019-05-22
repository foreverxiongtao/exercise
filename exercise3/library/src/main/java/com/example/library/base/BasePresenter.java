package com.example.library.base;

import android.support.annotation.NonNull;
import com.example.library.manager.RxManager;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/25 下午11:28
 * desc   : BasePresenter
 * version: 1.0
 */

public abstract class BasePresenter<M, V> {

    public M mModel;
    public V mView;
    protected RxManager mRxManager = new RxManager();

    /**
     * Returns the Model reference that the presenter wants to hold
     *
     * @return Model reference held by presenter
     */
    public abstract M getModel();

    /**
     * 绑定mModel和mView的引用
     *
     * @param m model
     * @param v view
     */
    public void attachMV(@NonNull M m, @NonNull V v) {
        this.mModel = m;
        this.mView = v;
        this.onStart();
    }

    /**
     * Untie mModel and mView
     */
    public void detachMV() {
        mRxManager.unSubscribe();
        mView = null;
        mModel = null;
    }

    /**
     * mView and mModel bindings are completed immediately
     * <p>
     * Implement the logic after the class implementation is complete, such as data initialization, interface initialization, update, etc.
     */
    public abstract void onStart();
}
