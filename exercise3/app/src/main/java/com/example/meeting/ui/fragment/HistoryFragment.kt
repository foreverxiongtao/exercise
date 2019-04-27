package com.example.meeting.ui.fragment

import android.os.Bundle
import android.view.View
import com.example.library.base.fragment.BaseCompatFragment
import com.example.meeting.R
import com.example.meeting.constant.GlobalConstant

/**
 *    author : desperado
 *    e-mail : foreverxiongtao@sina.com
 *    date   : 2019/4/25 下午11:14
 *    desc   :会议历史页面
 *    version: 1.0
 */
class HistoryFragment : BaseCompatFragment() {
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

    override fun initUI(view: View?, savedInstanceState: Bundle?) {
    }

}