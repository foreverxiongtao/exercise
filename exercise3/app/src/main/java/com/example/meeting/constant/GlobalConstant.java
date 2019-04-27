package com.example.meeting.constant;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/25 下午11:07
 * desc   :全局常量
 * version: 1.0
 */
public class GlobalConstant {

    public static final int ARGUMENT_CODE_MAIN = 1;
    public static final int ARGUMENT_CODE_HISTORY = 2;
    public static final int ARGUMENT_CODE_MANAGEMENT = 3;
    public static final String ARGUMENT_KEY_TAG = "argument_key_tag";
    public static final int VALUE_IS_DELETE = 1;
    public static final int VALUE_IS_NOT_DELETE = 2;
    public static final String PRE_USER_NUMBER = "xm";
    public static final int VALUE_NUMBER_LENGTH = 6;
    public static final int VALUE_PAGING_DEFAULT = 10;

    public static final int VALUE_OPERATE_DELETE = 0;
    public static final int VALUE_OPERATE_SKIP = 1;


    public static final int VALUE_IS_SKIP = 1;
    public static final int VALUE_IS_NOT_SKIP = 2;


    public static String getNumStr(int number) {
        String numPre = GlobalConstant.PRE_USER_NUMBER;
        String numStr = number + "";
        while (numStr.length() < GlobalConstant.VALUE_NUMBER_LENGTH) {
            numStr = "0".concat(numStr);
        }
        return numPre.concat(numStr);
    }
}
