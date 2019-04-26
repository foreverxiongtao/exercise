package com.example.meeting.ui.activity

import android.os.Bundle
import android.view.View
import com.example.library.base.BaseActivity
import com.example.library.base.BaseMVPCompatActivity
import com.example.library.base.BasePresenter
import com.example.library.utils.LogUtils
import com.example.library.utils.ToastUtils
import com.example.meeting.R
import com.example.meeting.constant.GlobalConstant
import com.example.meeting.contract.AbPersonAddContract
import com.example.meeting.db.AppDatabase
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

    override fun onGetNewestNumberFailure(message: String?) {

    }

    override fun hideWaitDialog() {
        showProgressDialog("")
    }

    override fun onGetNewestNumberSuccess(number: User) {
        LogUtils.d(number.toString())
    }

    override fun initPresenter(): BasePresenter<*, *> {
        return PersonAddPresenter()
    }

    override fun showWaitDialog(waitMsg: String?) {

    }

    override fun onSavePersonSuccess(user: User?) {
    }

    override fun onSavePersonFailure() {

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
        user.isDelete = GlobalConstant.VALUE_IS_NOT_DELETE
        val id = AppDatabase.getInstance().userDao().insertUsers(user)
        if (id > 0) {
            ToastUtils.showShort(getString(R.string.str_insert_success))
        } else {
            ToastUtils.showShort(getString(R.string.str_insert_failure))
        }
    }
}
