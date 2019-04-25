package com.example.meeting;

import android.app.Application;
import com.example.library.base.BaseApplication;
import com.example.library.utils.CoreUtils;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/25 下午5:07
 * desc   : application
 * version: 1.0
 */
public class MeetingApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        CoreUtils.init(this);
    }
}
