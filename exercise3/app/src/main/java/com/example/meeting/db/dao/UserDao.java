package com.example.meeting.db.dao;

import android.arch.persistence.room.*;
import android.database.Cursor;
import android.util.Log;
import com.example.meeting.model.entity.User;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

import java.util.List;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/26 上午9:56
 * desc   : 用户dao层
 * version: 1.0
 */
@Dao
public interface UserDao {

    /**
     * 获取所有的用户(未删除)
     *
     * @param deleteStatus
     * @param offset
     * @param defaultPaging
     * @return
     */
    @Query("select * from t_users where is_delete =:deleteStatus order by `no` limit:offset,:defaultPaging")
    Maybe<List<User>> getAllUsers(int deleteStatus, int offset, int defaultPaging);


    @Query("select count(`no`) from t_users where is_delete=:deleteStatus")
    Maybe<Integer> getPersonTotalCount(int deleteStatus);


    /***
     * 插入用户
     * @param user
     * @return
     */
    @Insert(onConflict = OnConflictStrategy.FAIL)
    Long insertUsers(User user);

    /**
     * 获取当前最新的用户
     *
     * @return
     */
    @Query("select * from t_users  order by `no` desc limit 1 ")
    Maybe<User> getNewestNumber();

    /***
     * 修改用户信息
     * @param user
     * @return
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateUser(User user);


    /**
     * 重置所有用户的跳过状态
     *
     * @return
     */
    @Query("update t_users set  is_skip = :skipStauts where uid>:uid")
    void resetSkipStatus(int skipStauts, int uid);


    /**
     * 获取能满足主持会议的最近一条用户
     *
     * @return
     */
    @Query("select * from t_users where is_delete =:deleteStatus and uid>:uid and is_skip =:notSkip order by `no` limit 1 ")
    Maybe<User> getAvaiableUser(int deleteStatus, int uid, int notSkip);


    /**
     * 将指定区间段的用户跳过状态修改
     *
     * @return
     */
    @Query("update t_users set  is_skip = :skipStauts where is_skip != :skipStauts and  uid between :startUid and :endUid ")
    void resetSkipStatus(int skipStauts, int startUid, int endUid);


    /**
     * 获取能满足主持会议的最近一条用户
     *
     * @return
     */
    @Query("select * from t_users where is_delete =:deleteStatus and uid>:uid and is_skip =:notSkip order by `no` limit 1 ")
    User getAvaiableUserByUid(int deleteStatus, int uid, int notSkip);


}
