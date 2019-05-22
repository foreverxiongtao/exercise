package com.example.exercise.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.util.Log

/**
 *    author : desperado
 *    e-mail : foreverxiongtao@sina.com
 *    date   : 2019/5/7 下午4:58
 *    desc   :  LifeCycleObserver创建
 *    version: 1.0
 */
class BasePresenter : LifecycleObserver {
    private val TAG: String = BasePresenter::class.java.simpleName

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        Log.d(TAG, "onCreate")
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        Log.i(TAG, "onStart()");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        Log.i(TAG, "onResume()");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        Log.i(TAG, "onPause()");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        Log.i(TAG, "onStop()");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        Log.d(TAG, "onDestroy");
    }
}