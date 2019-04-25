package com.example.meeting.adapter;

import android.support.v4.app.FragmentManager;
import com.example.library.base.BaseFragmentPageAdapter;
import com.example.meeting.MeetingApplication;
import com.example.meeting.R;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/25 下午10:32
 * desc   :主页面数据适配器
 * version: 1.0
 */

public class MainMeetingPageAdapter extends BaseFragmentPageAdapter {

    String[] strTitle = new String[]
            {MeetingApplication.getContext().getResources().getString(R.string.str_tab_main),
                    MeetingApplication.getContext().getResources().getString(R.string.str_tab_history),
                    MeetingApplication.getContext().getResources().getString(R.string.str_tab_management)};

    public MainMeetingPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return strTitle[position];
    }
}
