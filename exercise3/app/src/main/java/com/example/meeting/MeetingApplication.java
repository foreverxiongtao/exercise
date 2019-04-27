package com.example.meeting;

import com.example.library.base.BaseApplication;
import com.example.library.utils.CoreUtils;
import com.example.meeting.manager.MeetingManager;

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
        //检查当前的日期是否满足生成会议记录
        MeetingManager.getInstance().checkMeetingPublishAvaiablity();
    }
}
