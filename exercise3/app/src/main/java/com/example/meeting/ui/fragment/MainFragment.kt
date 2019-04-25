package com.example.meeting.ui.fragment

import android.os.Bundle
import android.view.View
import com.example.library.base.BaseCompatFragment
import com.example.meeting.R
import com.example.meeting.constant.GlobalConstant

/**
 *    author : desperado
 *    e-mail : foreverxiongtao@sina.com
 *    date   : 2019/4/25 下午11:14
 *    desc   :主页面
 *    version: 1.0
 */
class MainFragment : BaseCompatFragment() {
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