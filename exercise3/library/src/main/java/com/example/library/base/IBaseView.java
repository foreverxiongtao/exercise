package com.example.library.base;

import android.support.annotation.NonNull;

/**
 * Created by FKQ on 2017/12/19  11:13
 * *
 * Email: Hacken_F@163.com
 * *
 * Dec:
 * *
 */

public interface IBaseView {

    /**
     * 初始化presenter
     * <p>
     * 此方法返回的presenter对象不可为空
     */
    @NonNull
    BasePresenter initPresenter();

    /**
     * 显示等待dialog
     *
     * @param waitMsg 等待消息字符串
     */
    void showWaitDialog(String waitMsg);

    /**
     * 隐藏等待dialog
     */
    void hideWaitDialog();


}
