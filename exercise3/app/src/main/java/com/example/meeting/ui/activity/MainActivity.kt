package com.example.meeting.ui.activity

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.library.base.activity.BaseActivity
import com.example.meeting.R
import com.example.meeting.adapter.MainMeetingPageAdapter
import com.example.meeting.constant.GlobalConstant
import com.example.meeting.ui.fragment.HistoryFragment
import com.example.meeting.ui.fragment.MainFragment
import com.example.meeting.ui.fragment.ManagementFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

/**
 *    author : desperado
 *    e-mail : foreverxiongtao@sina.com
 *    date   : 2019/4/25 下午9:41
 *    desc   :main activity
 *    version: 1.0
 */
class MainActivity : BaseActivity() {
    override fun initView(savedInstanceState: Bundle?) {
        initTab()
    }


    override fun initData() {
        super.initData()
    }


    /***
     * 初始化tab
     */
    private fun initTab() {
        tl_main_tab.addTab(
            tl_main_tab.newTab().setCustomView(
                getCustomTabItem(
                    getString(R.string.str_tab_main),
                    R.drawable.item_tab_main_selector
                )
            )
        )
        tl_main_tab.addTab(
            tl_main_tab.newTab().setCustomView(
                getCustomTabItem(
                    getString(R.string.str_tab_history),
                    R.drawable.item_tab_history_selector
                )
            )
        )
        tl_main_tab.addTab(
            tl_main_tab.newTab().setCustomView(
                getCustomTabItem(
                    getString(R.string.str_tab_management),
                    R.drawable.item_tab_management_selector
                )
            )
        )
        //viewpage  and  tablayout bind
        vp_main_tab.addOnPageChangeListener(object : TabLayout.TabLayoutOnPageChangeListener(tl_main_tab) {

        })

        tl_main_tab.addOnTabSelectedListener(object : TabLayout.ViewPagerOnTabSelectedListener(vp_main_tab) {

        })
        //set  fragment data
        val vpPageAdapter = MainMeetingPageAdapter(supportFragmentManager)
        val mainFragment = MainFragment.newInstance(GlobalConstant.ARGUMENT_CODE_MAIN)
        val historyFragment = HistoryFragment.newInstance(GlobalConstant.ARGUMENT_CODE_HISTORY)
        val manageFragment = ManagementFragment.newInstance(GlobalConstant.ARGUMENT_CODE_MANAGEMENT)
        val fragments = ArrayList<Fragment>()
        fragments.add(mainFragment)
        fragments.add(historyFragment)
        fragments.add(manageFragment)
        vpPageAdapter.setFragments(fragments)
        vp_main_tab.adapter = vpPageAdapter

    }


    /***
     * set custom tab item
     * @param name  tab name
     * @param iconID tab icon
     */
    private fun getCustomTabItem(name: String, iconID: Int): View {
        val newtab = LayoutInflater.from(this).inflate(R.layout.item_custom_tab, null)
        val tv = newtab.findViewById(R.id.tabtext) as TextView
        tv.text = name
        val im = newtab.findViewById(R.id.tabicon) as ImageView
        im.setImageResource(iconID)
        return newtab
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }
}
