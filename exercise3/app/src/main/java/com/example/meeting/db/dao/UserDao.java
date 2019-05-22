package com.example.meeting.db.dao;

import android.arch.persistence.room.*;
import com.example.meeting.model.entity.User;
import io.reactivex.Maybe;
import io.reactivex.Observable;

import java.util.List;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/26 上午9:56
 * desc   : user dao
 * version: 1.0
 */
@Dao
public interface UserDao {

    /**
     * get all users which is not deleted(未删除)
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


    @Query("select count(`no`) from t_users where is_delete=:deleteStatus")
    Integer getPersonTotalCountDefault(int deleteStatus);


    /***
     * add user
     * @param user
     * @return
     */
    @Insert(onConflict = OnConflictStrategy.FAIL)
    Long insertUsers(User user);

    /**
     * Get the latest users
     *
     * @return
     */
    @Query("select * from t_users  order by `no` desc limit 1 ")
    Maybe<User> getNewestNumber();


    /**
     * Get the latest users
     *
     * @return
     */
    @Query("select * from t_users  order by `no` desc limit 1 ")
    User getNewestNumber2();

    /***
     * modify user info
     * @param user
     * @return
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateUser(User user);


    /**
     * Reset skip status for all users
     *
     * @return
     */
    @Query("update t_users set  is_skip = :skipStauts where uid>:uid")
    void resetSkipStatus(int skipStauts, int uid);


    /**
     * Get the latest user who can host the meeting
     *
     * @return
     */
    @Query("select * from t_users where is_delete =:deleteStatus and uid>:uid and is_skip =:notSkip order by `no` limit 1 ")
    Maybe<User> getAvaiableUser(int deleteStatus, int uid, int notSkip);


    /**
     * User skip state modification for the specified interval segment
     *
     * @return
     */
    @Query("update t_users set  is_skip = :skipStauts where is_skip != :skipStauts and  uid > :startUid and uid<:endUid ")
    void resetSkipStatus(int skipStauts, int startUid, int endUid);


    /**
     * Get the latest user who can host the meeting
     *
     * @return
     */
    @Query("select * from t_users where is_delete =:deleteStatus and uid>:uid and is_skip =:notSkip order by `no` limit 1 ")
    User getAvaiableUserByUid(int deleteStatus, int uid, int notSkip);


    /**
     * Get current user information based on user number
     *
     * @return
     */
    @Query("select * from t_users  where `no`= :no ")
    User getUserByNo(int no);


    /**
     * Obtain the information of the first person who cannot meet the main meeting
     *
     * @return
     */
    @Query("select * from t_users where is_delete =:deleteStatus and uid>:uid and is_skip =:notSkip order by `no` limit 1 ")
    User getFirstUnavaiableUser(int deleteStatus, int uid, int notSkip);

}
