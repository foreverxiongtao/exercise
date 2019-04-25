package com.example.meeting.ui.activity

import android.os.Bundle
import android.view.View
import com.example.library.base.BaseActivity
import com.example.library.utils.CoreUtils
import com.example.library.utils.ToastUtils
import com.example.meeting.R
import kotlinx.android.synthetic.main.activity_person_add.*

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/25 下午11:28
 * desc   :人员新增页面
 * version: 1.0
 */
class PersonAddActivity : BaseActivity(), View.OnClickListener {


    override fun initView(savedInstanceState: Bundle?) {
        initTitleBar(tl_person_add_title)
        btn_person_add_save.setOnClickListener(this)
    }


    override fun initData() {
        super.initData()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_person_add
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_person_add_save -> savePerson()
        }
    }


    /***
     * 保存人员到db
     */
    private fun savePerson() {
        val num = et_person_add_num.text
        num.isNullOrEmpty().let {
            ToastUtils.showShort(R.string.str_num_empty)
            return
        }

        val name = et_person_add_name.text
        name.isNullOrEmpty().let {
            ToastUtils.showShort(R.string.str_name_empty)
            return
        }

    }
}
