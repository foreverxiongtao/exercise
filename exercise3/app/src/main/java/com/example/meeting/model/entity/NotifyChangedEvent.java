package com.example.meeting.model.entity;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/26 下午11:48
 * desc   : notification change event entity
 * version: 1.0
 */

public class NotifyChangedEvent {


    private final int notifyObj;

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public NotifyChangedEvent(int notifyObj) {
        this.notifyObj = notifyObj;
    }

    public int getState() {
        return notifyObj;
    }

    public static class NotifyChangeEventConstant {
        public static final int OBJ_PERSON_ADD = 1;
        public static final int OBJ_DELETE_HISTORY = 2;
        public static final int OBJ_PERSON_TOTAL_COUNT = 2;
    }
}
