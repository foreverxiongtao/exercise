package com.example.meeting.ui.activity

import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.library.base.BasePresenter
import com.example.library.base.activity.BaseMVPCompatActivity
import com.example.library.utils.ToastUtils
import com.example.meeting.R
import com.example.meeting.adapter.ManagementAdapter
import com.example.meeting.constant.GlobalConstant
import com.example.meeting.contract.AbPersonManagementContract
import com.example.meeting.model.PersonManagementModel
import com.example.meeting.model.entity.User
import com.example.meeting.presenter.PersonManagementPresenter
import kotlinx.android.synthetic.main.activity_person_list.*

class PersonListActivity : BaseMVPCompatActivity<PersonManagementPresenter, PersonManagementModel>(),
    AbPersonManagementContract.IPersonalManagementView,
    SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    override fun initView(savedInstanceState: Bundle?) {
        initTitleBar(tl_person_list_title)

        srl_person_list_refresh.setOnRefreshListener(this)
        mManagementAdapter = ManagementAdapter(R.layout.item_list_management)
        rcv_person_list.adapter = mManagementAdapter
        rcv_person_list.layoutManager = LinearLayoutManager(this)
        mManagementAdapter!!.setOnLoadMoreListener(this, rcv_person_list)
        mManagementAdapter!!.setOnItemClickListener { adapter, view, position ->
            if (position >= 0) {
                val user = mManagementAdapter!!.getItem(position)
                user?.let { showUserOperateDialog(it, position) }
            }
        }
    }


    override fun moreUserListFailure(message: String?) {
    }

    override fun moreUserListSuccess(list: MutableList<User>?) {
        srl_person_list_refresh.isRefreshing = false
        if (mManagementAdapter!!.data.size === 0) {
            mManagementAdapter!!.setNewData(list)
        } else {
            list?.let {
                mManagementAdapter!!.addData(it)
            }
            mManagementAdapter!!.loadMoreComplete()
        }
    }


    private var mManagementAdapter: ManagementAdapter? = null

    override fun refreshUserListSuccess(list: MutableList<User>?) {
        srl_person_list_refresh.isRefreshing = false
        list?.let {
            mManagementAdapter?.setNewData(it)
            when {
                it.size < GlobalConstant.VALUE_PAGING_DEFAULT -> mManagementAdapter!!.loadMoreEnd(true)
            }
        }
    }

    override fun refreshUserListFailure(message: String?) {
        srl_person_list_refresh.isRefreshing = false
        mManagementAdapter?.loadMoreFail()
    }

    override fun getUserListrEmpty() {
    }

    override fun onDeletePersonSuccess(position: Int) {
        mManagementAdapter!!.remove(position)
    }

    override fun onDeletePersonFailure() {
    }

    override fun initPresenter(): BasePresenter<*, *> {
        return PersonManagementPresenter.newInstance()
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_person_list
    }


    /**
     * 弹出人员操作对话框
     */
    private fun showUserOperateDialog(user: User, position: Int) {
        var builder = AlertDialog.Builder(this)
        builder.setIcon(R.drawable.ic_operate)
        builder.setTitle(R.string.str_operate)
        val items = arrayOf(getString(R.string.str_delete), getString(R.string.str_skip))
        builder.setItems(items,
            DialogInterface.OnClickListener { dialogInterface, i ->
                when (i) {
                    //修改用户的删除状态
                    GlobalConstant.VALUE_OPERATE_DELETE -> mPresenter.deletePerson(user, position)
                    GlobalConstant.VALUE_OPERATE_SKIP -> {
                        if (user.isSkip == GlobalConstant.VALUE_IS_NOT_SKIP) {
                            mPresenter.skipMeeting(user, position)
                        } else {
                            ToastUtils.showLong(getString(R.string.str_meeting_already_skip))
                        }
                    }
                }
            })
        builder.setCancelable(true)
        val dialog = builder.create()
        dialog.show()
    }

    override fun initData() {
        super.initData()
        mPresenter.refreshUserTotalCount()
        mPresenter.refreshUserList()
    }

    override fun onRefresh() {
        mManagementAdapter?.setEnableLoadMore(false)
        mPresenter.refreshUserList()
    }

    override fun onLoadMoreRequested() {
        mPresenter.loadMoreUserList()
    }

    override fun showNoMoreData() {
        srl_person_list_refresh.isRefreshing = false
        mManagementAdapter!!.loadMoreEnd(false)
    }

    override fun onSkipMeetingSuccess(user: User, position: Int) {
        mManagementAdapter!!.setData(position, user)
    }

    override fun onSkipMeetingFailure() {
        ToastUtils.showShort(getString(R.string.str_skip_meeting_failure))
    }

}