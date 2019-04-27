package com.example.meeting.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.library.utils.TimeUtils;
import com.example.meeting.R;
import com.example.meeting.model.entity.MeetingHistory;
import com.example.meeting.model.entity.User;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/27 下午4:13
 * desc   : 历史记录数据源
 * version: 1.0
 */
public class MeetingHistoryAdapter extends BaseCompatAdapter<MeetingHistory, BaseViewHolder> {


    public MeetingHistoryAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, MeetingHistory item) {
        TextView tv_history_name_desc = helper.getView(R.id.tv_history_name_desc);
        TextView tv_history_name = helper.getView(R.id.tv_history_name);
        TextView tv_history_date = helper.getView(R.id.tv_history_date);
        User user = item.getUser();
        if (user != null) {
            if (!TextUtils.isEmpty(user.getName())) {
                tv_history_name.setText(user.getName());
                tv_history_name.setVisibility(View.VISIBLE);
                tv_history_name_desc.setVisibility(View.VISIBLE);
                tv_history_date.setVisibility(View.VISIBLE);
                tv_history_date.setText(TimeUtils.millis2StringByCustomTime(item.getHostTime()));
            } else {
                tv_history_name_desc.setVisibility(View.GONE);
                tv_history_name.setVisibility(View.GONE);
                tv_history_date.setVisibility(View.GONE);
            }

        }
    }
}
