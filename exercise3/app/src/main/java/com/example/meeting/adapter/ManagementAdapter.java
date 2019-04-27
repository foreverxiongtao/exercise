package com.example.meeting.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.library.utils.TimeUtils;
import com.example.meeting.R;
import com.example.meeting.constant.GlobalConstant;
import com.example.meeting.model.entity.User;

import java.util.List;

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
            tv_management_name.setText(item.getName());
        }
        TextView tv_management_no = helper.getView(R.id.tv_management_no);
        tv_management_no.setText(GlobalConstant.getNumStr(item.getNo()));

        TextView tv_management_date = helper.getView(R.id.tv_management_date);
        tv_management_date.setText(TimeUtils.millis2String(item.getCreateTime()));

        TextView tv_management_skip = helper.getView(R.id.tv_management_skip);
        if (item.getIsSkip() == GlobalConstant.VALUE_IS_SKIP) {
            tv_management_skip.setVisibility(View.VISIBLE);
        } else {
            tv_management_skip.setVisibility(View.GONE);
        }
    }
}
