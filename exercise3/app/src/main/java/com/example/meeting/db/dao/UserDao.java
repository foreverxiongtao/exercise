package com.example.meeting.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.example.meeting.model.entity.User;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

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

    @Query("select * from t_users")
    Flowable<List<User>> getUsers();

    @Insert(onConflict = OnConflictStrategy.FAIL)
    long insertUsers(User user);

    @Query("select * from t_users  order by create_time limit 1 ")
    Maybe<User> getNewestNumber();
}
