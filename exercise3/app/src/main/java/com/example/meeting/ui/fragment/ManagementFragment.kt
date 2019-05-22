package com.example.meeting.ui.fragment

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.library.base.BasePresenter
import com.example.library.base.fragment.BaseCompatFragment
import com.example.library.base.fragment.BaseMVPCompatFragment
import com.example.library.utils.ActivityUtils
import com.example.library.utils.ToastUtils
import com.example.meeting.R
import com.example.meeting.adapter.ManagementAdapter
import com.example.meeting.constant.GlobalConstant
import com.example.meeting.contract.AbPersonManagementContract
import com.example.meeting.model.entity.NotifyChangedEvent
import com.example.meeting.model.entity.User
import com.example.meeting.presenter.PersonManagementPresenter
import com.example.meeting.ui.activity.PersonAddActivity
import com.example.meeting.ui.activity.PersonListActivity
import kotlinx.android.synthetic.main.fragment_management.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 *    author : desperado
 *    e-mail : foreverxiongtao@sina.com
 *    date   : 2019/4/25 下午11:13
 *    desc   :user management fragment
 *    version: 1.0
 */
class ManagementFragment : BaseCompatFragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cl_person_add -> {
                ActivityUtils.startActivity(Intent(mActivity,PersonAddActivity::class.java))
            }
            R.id.cl_person_list -> {
                ActivityUtils.startActivity(Intent(mActivity,PersonListActivity::class.java))
            }
        }
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

    override fun getLayoutId(): Int {
        return R.layout.fragment_management
    }

    override fun initUI(view: View?, savedInstanceState: Bundle?) {
        cl_person_add.setOnClickListener(this)
        cl_person_list.setOnClickListener(this)
    }
}