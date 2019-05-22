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
 * desc   : meeting history
 * version: 1.0
 */
@Dao
public interface MeetingHistoryDao {

    /***
     * add user
     * @param meetingHistory
     * @return
     */
    @Insert(onConflict = OnConflictStrategy.FAIL)
    Long insertUsers(MeetingHistory meetingHistory);


    /***
     * page to get meeting history
     * @param offset
     * @param defaultPaging
     * @return
     */
    @Query("select * from t_meeting_history as t inner join t_users as u on t.u_id = u.uid order by t.host_time desc limit:offset,:defaultPaging")
    Maybe<List<MeetingHistory>> getMeetingHistory(int offset, int defaultPaging);


    /***
     * get history total count
     * @return
     */
    @Query("select count(`id`) from t_meeting_history")
    Maybe<Integer> getHistoryTotalCount();


    /***
     * Get records of yesterday and today's meetings
     * @param offset
     * @return
     */
    @Query("select * from t_meeting_history as t inner join t_users as u on t.u_id = u.uid order by t.host_time desc limit:offset")
    Maybe<List<MeetingHistory>> getYesterdayAndTodayMeeting(int offset);

    /***
     * Get the total number of meeting minutes
     * @return
     */
    @Query("delete  from t_meeting_history where id  =(select id  from t_meeting_history order by create_time  limit 1)")
    void deleteOldestHistory();
}
