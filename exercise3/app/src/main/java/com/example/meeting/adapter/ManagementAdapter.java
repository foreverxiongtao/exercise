package com.example.meeting.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.library.utils.TimeUtils;
import com.example.meeting.R;
import com.example.meeting.constant.GlobalConstant;
import com.example.meeting.model.entity.User;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/27 上午12:12
 * desc   : 管理人员数据源
 * version: 1.0
 */
public class ManagementAdapter extends BaseCompatAdapter<User, BaseViewHolder> {


    public ManagementAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, User item) {
        TextView tv_management_name = helper.getView(R.id.tv_management_name);
        if (!TextUtils.isEmpty(item.getName())) {
            String name = String.format(mContext.getResources().getString(R.string.str_person_name_format), item.getName());
            tv_management_name.setText(name);
        }
        TextView tv_management_no = helper.getView(R.id.tv_management_no);
        String no = String.format(mContext.getResources().getString(R.string.str_person_no_format), GlobalConstant.getNumStr(item.getNo()));
        tv_management_no.setText(no);
        TextView tv_management_date = helper.getView(R.id.tv_management_date);
        String date = String.format(mContext.getResources().getString(R.string.str_person_create_format), TimeUtils.millis2StringByCustomTime(item.getCreateTime()));
        tv_management_date.setText(date);
        TextView tv_management_skip = helper.getView(R.id.tv_management_skip);
        if (item.getIsSkip() == GlobalConstant.VALUE_IS_SKIP) {
            tv_management_skip.setVisibility(View.VISIBLE);
        } else {
            tv_management_skip.setVisibility(View.GONE);
        }
    }
}
