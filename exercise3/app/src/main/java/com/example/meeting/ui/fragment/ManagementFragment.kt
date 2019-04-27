package com.example.meeting.ui.fragment

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.library.base.BasePresenter
import com.example.library.base.fragment.BaseMVPCompatFragment
import com.example.library.utils.ActivityUtils
import com.example.meeting.R
import com.example.meeting.adapter.ManagementAdapter
import com.example.meeting.constant.GlobalConstant
import com.example.meeting.contract.AbPersonManagementContract
import com.example.meeting.model.entity.NotifyChangedEvent
import com.example.meeting.model.entity.User
import com.example.meeting.presenter.PersonManagementPresenter
import com.example.meeting.ui.activity.PersonAddActivity
import kotlinx.android.synthetic.main.fragment_management.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 *    author : desperado
 *    e-mail : foreverxiongtao@sina.com
 *    date   : 2019/4/25 下午11:13
 *    desc   :人员管理页面
 *    version: 1.0
 */
class ManagementFragment :
    BaseMVPCompatFragment<AbPersonManagementContract.AbPersonManagementPresenter, AbPersonManagementContract.IPersonManagementModel>(),
    View.OnClickListener, AbPersonManagementContract.IPersonalManagementView, SwipeRefreshLayout.OnRefreshListener,
    BaseQuickAdapter.RequestLoadMoreListener {

    override fun moreUserListFailure(message: String?) {
    }

    override fun moreUserListSuccess(list: MutableList<User>?) {
        srl_management_refresh.isRefreshing = false
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
        srl_management_refresh.isRefreshing = false
        list?.let { mManagementAdapter?.setNewData(it) }
    }

    override fun refreshUserListFailure(message: String?) {
        srl_management_refresh.isRefreshing = false
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

    companion object {
        fun newInstance(tag: Int): ManagementFragment {
            val args = Bundle()
            args.putInt(GlobalConstant.ARGUMENT_KEY_TAG, tag)
            val fragment = ManagementFragment()
            fragment.arguments = args
            return fragment
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPersonChange(event: NotifyChangedEvent) {

    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_management
    }

    override fun initUI(view: View?, savedInstanceState: Bundle?) {
        fab_management_add.setOnClickListener(this)
        srl_management_refresh.setOnRefreshListener(this)
        mManagementAdapter = ManagementAdapter(R.layout.item_list_management)
        rcv_management_list.adapter = mManagementAdapter
        rcv_management_list.layoutManager = LinearLayoutManager(mActivity)
        mManagementAdapter!!.setOnLoadMoreListener(this, rcv_management_list)
        mManagementAdapter!!.setOnItemClickListener { adapter, view, position ->
            if (position > 0) {
                val user = mManagementAdapter!!.getItem(position)
                user?.let { showUserOperateDialog(it, position) }
            }
        }
    }


    /**
     * 弹出人员操作对话框
     */
    private fun showUserOperateDialog(user: User, position: Int) {
        var builder = AlertDialog.Builder(context!!)
        builder.setIcon(R.mipmap.ic_launcher)
        builder.setTitle(R.string.str_operate)
        val items = arrayOf(getString(R.string.str_delete), getString(R.string.str_skip))
        builder.setItems(items,
            DialogInterface.OnClickListener { dialogInterface, i ->
                when (i) {
                    //修改用户的删除状态
                    GlobalConstant.VALUE_OPERATE_DELETE -> mPresenter.deletePerson(user, position)
                    GlobalConstant.VALUE_OPERATE_SKIP -> {

                    }

                }
            })
        builder.setCancelable(true)
        val dialog = builder.create()
        dialog.show()
    }

    override fun initData() {
        EventBus.getDefault().register(this)
        super.initData()
        mPresenter.refreshUserTotalCount()
        mPresenter.refreshUserList()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_management_add -> {
                ActivityUtils.startActivity(Intent(mActivity, PersonAddActivity::class.java))
            }
        }
    }

    override fun onRefresh() {
        mManagementAdapter?.setEnableLoadMore(false)
        mPresenter.refreshUserList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

    override fun onLoadMoreRequested() {
        mPresenter.loadMoreUserList()
    }

    override fun showNoMoreData() {
        srl_management_refresh.isRefreshing = false
        mManagementAdapter!!.loadMoreEnd(false)
    }


}