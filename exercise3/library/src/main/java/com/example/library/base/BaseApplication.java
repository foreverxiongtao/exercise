package com.example.library.base;

import android.app.Application;
import android.content.Context;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/25 下午5:47
 * desc   :application 基类
 * version: 1.0
 */
public class BaseApplication extends Application {

    protected static Context mContext;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}
