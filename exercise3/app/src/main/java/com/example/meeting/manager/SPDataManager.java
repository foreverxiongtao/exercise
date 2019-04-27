package com.example.meeting.manager;

import com.example.library.utils.SPUtils;

import java.util.Map;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/25 下午5:23
 * desc   :  本地数据管理工具
 * version: 1.0
 */

public class SPDataManager {


    public static class SPDataConstant {
        public static final String KEY_LAST_MEETING_HOST_ID = "key_last_meeting_host_id";
        public static final String KEY_NEWEST_USER_ID = "key_newest_user_id";
        public static final int VALUE_LAST_MEETING_HOST_ID_DEFAULT = -1;
    }


    private static final SPUtils SP_UTILS = SPUtils.getInstance("inflluu");

    public static void clear() {
        SP_UTILS.clear();
    }

    public static String sp2String() {
        StringBuilder sb = new StringBuilder();
        Map<String, ?> map = SP_UTILS.getAll();
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            sb.append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append("\n");
        }
        return sb.toString();
    }


    /***
     * 保存最近主持会议人员的id
     * @param userId
     */
    public static void saveLastMeetingHostId(int userId) {
        SP_UTILS.put(SPDataConstant.KEY_LAST_MEETING_HOST_ID, userId);
    }

    /***
     * 获取上一个主持会议人员的id
     * @return
     */
    public static int getLastMeetingHostId() {
        return SP_UTILS.getInt(SPDataConstant.KEY_LAST_MEETING_HOST_ID, SPDataConstant.VALUE_LAST_MEETING_HOST_ID_DEFAULT);
    }


    /***
     * 保存最近一个创建人员的id
     * @param userId
     */
    public static void saveNewestUserId(int userId) {
        SP_UTILS.put(SPDataConstant.KEY_NEWEST_USER_ID, userId);
    }

    /***
     * 获取最近一个创建人员的id
     * @return
     */
    public static int getNewestUserId() {
        return SP_UTILS.getInt(SPDataConstant.KEY_NEWEST_USER_ID, SPDataConstant.VALUE_LAST_MEETING_HOST_ID_DEFAULT);
    }


}
