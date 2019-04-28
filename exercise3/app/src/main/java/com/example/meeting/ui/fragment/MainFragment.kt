package com.example.meeting.ui.fragment

import android.os.Bundle
import android.view.View
import com.example.library.base.BasePresenter
import com.example.library.base.fragment.BaseMVPCompatFragment
import com.example.library.utils.LogUtils
import com.example.meeting.R
import com.example.meeting.constant.GlobalConstant
import com.example.meeting.contract.AbMeetingNoticeContract
import com.example.meeting.manager.SPDataManager
import com.example.meeting.model.MeetingNoticeModel
import com.example.meeting.model.entity.MeetingHistory
import com.example.meeting.model.entity.NotifyChangedEvent
import com.example.meeting.model.entity.User
import com.example.meeting.presenter.MeetingNoticePresenter
import kotlinx.android.synthetic.main.fragment_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *    author : desperado
 *    e-mail : foreverxiongtao@sina.com
 *    date   : 2019/4/25 下午11:14
 *    desc   :主页面
 *    version: 1.0
 */
class MainFragment : BaseMVPCompatFragment<MeetingNoticePresenter, MeetingNoticeModel>(),
    AbMeetingNoticeContract.IMeetingNoticeView {


    override fun getYesterdayAndTodayMeetinFaiure() {
        tv_tomorrow_name.text = resources.getString(R.string.str_no_host)
    }

    override fun getYesterdayAndTodayMeeting(meetings: MutableList<MeetingHistory>) {
        LogUtils.d("" + meetings.size)
        if (!meetings.isNullOrEmpty()) {
            meetings.forEachIndexed { index, meetingHistory ->
                when (index) {
                    GlobalConstant.VALUE_INDEX_TODAY -> {
                        tv_today_name.text = meetingHistory.user.name
                    }
                    GlobalConstant.VALUE_INDEX_YESTERDAY -> {
                        tv_yesterday_name.text = meetingHistory.user.name
                    }
                }
            }
        }
    }


    override fun getTomorrowMeeting(user: User) {
        when {
            !user.name.isNullOrEmpty() -> tv_tomorrow_name.text = user.name
        }

    }

    override fun getTomorrowMeetingFailure() {
        tv_yesterday_name.text = resources.getString(R.string.str_no_host)
        tv_today_name.text = resources.getString(R.string.str_no_host)
    }


    override fun initData() {
        EventBus.getDefault().register(this)
        super.initData()
        val lastMeetingHostId = SPDataManager.getLastMeetingHostId()
        when {
            SPDataManager.SPDataConstant.VALUE_LAST_MEETING_HOST_ID_DEFAULT != lastMeetingHostId -> {
                mPresenter.getYestodayAndTodayInfo()
                mPresenter.getTomorrowMeetingInfo(lastMeetingHostId)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun notifyEventChanged(event: NotifyChangedEvent) {
        if (event.state == NotifyChangedEvent.NotifyChangeEventConstant.OBJ_PERSON_ADD) {
            val lastMeetingHostId = SPDataManager.getLastMeetingHostId()
            when {
                SPDataManager.SPDataConstant.VALUE_LAST_MEETING_HOST_ID_DEFAULT != lastMeetingHostId -> {
                    mPresenter.getYestodayAndTodayInfo()
                    mPresenter.getTomorrowMeetingInfo(lastMeetingHostId)
                }
            }
        }
    }

    override fun initPresenter(): BasePresenter<*, *> {
        return MeetingNoticePresenter.newInstance()
    }

    companion object {
        fun newInstance(tag: Int): MainFragment {
            val args = Bundle()
            args.putInt(GlobalConstant.ARGUMENT_KEY_TAG, tag)
            val fragment = MainFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_main
    }

    override fun initUI(view: View?, savedInstanceState: Bundle?) {
    }

}