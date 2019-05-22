package com.example.meeting.model.entity;

import android.arch.persistence.room.*;
import android.support.annotation.NonNull;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/27 下午1:20
 * desc   : meeting history entity
 * version: 1.0
 */
@Entity(tableName = "t_meeting_history", foreignKeys = @ForeignKey(entity = User.class, parentColumns = "uid", childColumns = "u_id"))
public class MeetingHistory {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "u_id")
    @NonNull
    private int userId;


    @ColumnInfo(name = "host_time")
    @NonNull
    private long hostTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "MeetingHistory{" +
                "id=" + id +
                ", userId=" + userId +
                ", createTime=" + hostTime +
                '}';
    }


    @Embedded
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public long getHostTime() {
        return hostTime;
    }

    public void setHostTime(long hostTime) {
        this.hostTime = hostTime;
    }


}
