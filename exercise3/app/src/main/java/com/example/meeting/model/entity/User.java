package com.example.meeting.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/26 上午9:55
 * desc   : person  entity
 * version: 1.0
 */


@Entity(tableName = "t_users")
public class User {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid")
    private int id;

    @ColumnInfo(name = "no")
    @NonNull
    private int no;

    @ColumnInfo(name = "name")
    @NonNull
    private String name;

    @ColumnInfo(name = "create_time")
    @NonNull
    private long createTime;

    @ColumnInfo(name = "is_delete")
    @NonNull
    private int isDelete;  //delete status

    @ColumnInfo(name = "is_skip")
    @NonNull
    private int isSkip;  //skip status




    public int getIsSkip() {
        return isSkip;
    }

    public void setIsSkip(int isSkip) {
        this.isSkip = isSkip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String toString() {
        return "User{" +
                "no=" + no +
                ", name='" + name + '\'' +
                ", createTime=" + createTime +
                ", isDelete=" + isDelete +
                '}';
    }
}
