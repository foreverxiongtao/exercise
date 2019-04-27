package com.example.meeting.model.entity;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/26 下午11:48
 * desc   : 事件通知基类
 * version: 1.0
 */

public class NotifyChangedEvent {


    private final int notifyObj;

    public NotifyChangedEvent(int notifyObj) {
        this.notifyObj = notifyObj;
    }

    public int getState() {
        return notifyObj;
    }

    static class NotifyChangeEventConstant {
        private static final int OBJ_PERSON_ADD = 1;
    }
}
