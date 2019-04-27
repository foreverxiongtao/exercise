package com.example.meeting.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.example.meeting.model.entity.MeetingHistory;
import io.reactivex.Maybe;

import java.util.List;


/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/27 下午1:24
 * desc   :会议历史db层
 * version: 1.0
 */
@Dao
public interface MeetingHistoryDao {

    /***
     * 插入用户
     * @param meetingHistory
     * @return
     */
    @Insert(onConflict = OnConflictStrategy.FAIL)
    Long insertUsers(MeetingHistory meetingHistory);


    /***
     * 分页获取会议记录
     * @param offset
     * @param defaultPaging
     * @return
     */
    @Query("select * from t_meeting_history as t inner join t_users as u on t.u_id = u.uid order by t.host_time desc limit:offset,:defaultPaging")
    Maybe<List<MeetingHistory>> getMeetingHistory(int offset, int defaultPaging);


    /***
     * 获取会议记录总条数
     * @return
     */
    @Query("select count(`id`) from t_meeting_history")
    Maybe<Integer> getHistoryTotalCount();


    /***
     * 获取昨天和今天会议的记录
     * @param offset
     * @return
     */
    @Query("select * from t_meeting_history as t inner join t_users as u on t.u_id = u.uid order by t.host_time desc limit:offset")
    Maybe<List<MeetingHistory>> getYesterdayAndTodayMeeting(int offset);
}
