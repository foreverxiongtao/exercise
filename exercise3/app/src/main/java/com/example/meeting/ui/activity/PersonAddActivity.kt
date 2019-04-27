package com.example.meeting.ui.activity

import android.os.Bundle
import android.view.View
import com.example.library.base.activity.BaseMVPCompatActivity
import com.example.library.base.BasePresenter
import com.example.library.utils.ToastUtils
import com.example.meeting.R
import com.example.meeting.constant.GlobalConstant
import com.example.meeting.contract.AbPersonAddContract
import com.example.meeting.model.entity.User
import com.example.meeting.presenter.PersonAddPresenter
import kotlinx.android.synthetic.main.activity_person_add.*

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/25 下午11:28
 * desc   :人员新增页面
 * version: 1.0
 */
class PersonAddActivity :
    BaseMVPCompatActivity<AbPersonAddContract.AbPersonAddPresenter, AbPersonAddContract.IPersonAddModel>(),
    View.OnClickListener, AbPersonAddContract.IPersonalAddView {
    override fun onGetNewestNumberEmpty() {
        var numStr = GlobalConstant.getNumStr(1)
        et_person_add_num.setText("$numStr")
    }

    override fun onGetNewestNumberFailure(message: String?) {

    }

    override fun hideWaitDialog() {
        hideProgressDialog()
    }

    override fun onGetNewestNumberSuccess(user: User) {
        var numStr = GlobalConstant.getNumStr(user.no + 1)
        et_person_add_num.setText("$numStr")
    }

    override fun initPresenter(): BasePresenter<*, *> {
        return PersonAddPresenter.newInstance()
    }

    override fun showWaitDialog(waitMsg: String?) {
        showProgressDialog("")
    }

    override fun onSavePersonSuccess(user: User?) {
        ToastUtils.showShort(getString(R.string.str_insert_success))
        finish()
    }

    override fun onSavePersonFailure() {
        ToastUtils.showShort(getString(R.string.str_insert_failure))
    }


    override fun initView(savedInstanceState: Bundle?) {
        initTitleBar(tl_person_add_title)
        btn_person_add_save.setOnClickListener(this)
    }


    override fun initData() {
        super.initData()
        //自动获取最新的员工编号
        mPresenter.getNewestNumber()
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
        val num = et_person_add_num.text.toString()
        if (num.isNullOrEmpty()) {
            ToastUtils.showShort(R.string.str_num_empty)
            return
        }
        val name = et_person_add_name.text.toString()
        if (name.isNullOrEmpty()) {
            ToastUtils.showShort(R.string.str_name_empty)
            return
        }
        val user = User()
        user.createTime = System.currentTimeMillis()
        user.name = name
        val text = et_person_add_num.text.toString().substring(2)
        user.no = text.toInt()
        user.isDelete = GlobalConstant.VALUE_IS_NOT_DELETE
        mPresenter.saveUser(user)
    }
}
