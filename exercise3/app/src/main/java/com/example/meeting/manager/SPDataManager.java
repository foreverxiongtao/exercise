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

}
