package com.example.meeting.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.library.base.BaseCompatFragment
import com.example.library.utils.ActivityUtils
import com.example.meeting.R
import com.example.meeting.constant.GlobalConstant
import com.example.meeting.ui.activity.PersonAddActivity
import kotlinx.android.synthetic.main.fragment_management.*

/**
 *    author : desperado
 *    e-mail : foreverxiongtao@sina.com
 *    date   : 2019/4/25 下午11:13
 *    desc   :人员管理页面
 *    version: 1.0
 */
class ManagementFragment : BaseCompatFragment(), View.OnClickListener {

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
        fab_management_add.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_management_add -> {
                ActivityUtils.startActivity(Intent(mActivity, PersonAddActivity::class.java))
            }
        }
    }

}