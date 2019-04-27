package com.example.meeting.db.dao;

import android.arch.persistence.room.*;
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

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateUser(User user);
}
