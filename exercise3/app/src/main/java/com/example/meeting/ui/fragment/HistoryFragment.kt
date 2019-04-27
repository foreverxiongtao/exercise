package com.example.meeting.ui.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.library.base.BasePresenter
import com.example.library.base.fragment.BaseMVPCompatFragment
import com.example.meeting.R
import com.example.meeting.adapter.MeetingHistoryAdapter
import com.example.meeting.constant.GlobalConstant
import com.example.meeting.contract.AbMeetingHistoryContract
import com.example.meeting.manager.MeetingManager
import com.example.meeting.model.MeetingHistoryModel
import com.example.meeting.model.entity.MeetingHistory
import com.example.meeting.model.entity.NotifyChangedEvent
import com.example.meeting.presenter.MeetingHistoryPresenter
import kotlinx.android.synthetic.main.fragment_history.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *    author : desperado
 *    e-mail : foreverxiongtao@sina.com
 *    date   : 2019/4/25 下午11:14
 *    desc   :会议历史页面
 *    version: 1.0
 */
class HistoryFragment : BaseMVPCompatFragment<MeetingHistoryPresenter, MeetingHistoryModel>(),
    AbMeetingHistoryContract.IMeetingHistoryView, SwipeRefreshLayout.OnRefreshListener,
    BaseQuickAdapter.RequestLoadMoreListener {
    override fun getHistoryListEmpty() {

    }

    companion object {
        fun newInstance(tag: Int): HistoryFragment {
            val args = Bundle()
            args.putInt(GlobalConstant.ARGUMENT_KEY_TAG, tag)
            val fragment = HistoryFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_history
    }


    override fun moreMeetingHistoryFailure(message: String?) {
    }

    override fun moreMeetingHistorySuccess(list: MutableList<MeetingHistory>?) {
        srl_history_refresh.isRefreshing = false
        if (mMeetingHistoryAdapter!!.data.size === 0) {
            mMeetingHistoryAdapter!!.setNewData(list)
        } else {
            list?.let {
                mMeetingHistoryAdapter!!.addData(it)
            }
            mMeetingHistoryAdapter!!.loadMoreComplete()
        }
    }


    private var mMeetingHistoryAdapter: MeetingHistoryAdapter? = null

    override fun refreshMeetingHistorySuccess(list: MutableList<MeetingHistory>?) {
        srl_history_refresh.isRefreshing = false
        list?.let {
            mMeetingHistoryAdapter?.setNewData(it)
            if (it.count() < GlobalConstant.VALUE_PAGING_DEFAULT) {
                mMeetingHistoryAdapter!!.loadMoreEnd(true);
            }
        }
    }

    override fun refreshMeetingHistoryFailure(message: String?) {
        srl_history_refresh.isRefreshing = false
        mMeetingHistoryAdapter?.loadMoreFail()
    }

    override fun initPresenter(): BasePresenter<*, *> {
        return MeetingHistoryPresenter.newInstance()
    }

    override fun initUI(view: View?, savedInstanceState: Bundle?) {
        srl_history_refresh.setOnRefreshListener(this)
        mMeetingHistoryAdapter = MeetingHistoryAdapter(R.layout.item_list_history)
        rcv_history_list.adapter = mMeetingHistoryAdapter
        rcv_history_list.layoutManager = LinearLayoutManager(mActivity)
        mMeetingHistoryAdapter!!.setOnLoadMoreListener(this, rcv_history_list)
        tv_top_metting.setOnClickListener({
            MeetingManager.getInstance().publishMeeting()
        })
    }

    override fun initData() {
        EventBus.getDefault().register(this)
        super.initData()
        mPresenter.refreshMeetingHistoryCount()
        mPresenter.refreshMeetingHistoryList()
    }


    override fun onRefresh() {
        mMeetingHistoryAdapter?.setEnableLoadMore(false)
        mPresenter.refreshMeetingHistoryList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

    override fun onLoadMoreRequested() {
        mPresenter.loadMoreMeetingHistoryList()
    }

    override fun showNoMoreData() {
        srl_history_refresh.isRefreshing = false
        mMeetingHistoryAdapter!!.loadMoreEnd(false)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPersonChange(event: NotifyChangedEvent) {

    }

}