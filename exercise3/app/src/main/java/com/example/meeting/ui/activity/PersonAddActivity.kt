package com.example.meeting.ui.activity

import android.os.Bundle
import android.view.View
import com.example.library.base.activity.BaseMVPCompatActivity
import com.example.library.base.BasePresenter
import com.example.library.utils.ToastUtils
import com.example.meeting.R
import com.example.meeting.constant.GlobalConstant
import com.example.meeting.contract.AbPersonAddContract
import com.example.meeting.manager.SPDataManager
import com.example.meeting.model.entity.NotifyChangedEvent
import com.example.meeting.model.entity.User
import com.example.meeting.presenter.PersonAddPresenter
import kotlinx.android.synthetic.main.activity_person_add.*
import org.greenrobot.eventbus.EventBus

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
//    }

    //    override fun onGetNewestNumberEmpty() {
//        var numStr = GlobalConstant.getNumStr(1)
//        et_person_add_num.setText("$numStr")
//    }
//
//    override fun onGetNewestNumberFailure(message: String?) {
//
//    override fun onGetNewestNumberSuccess(user: User) {
//        var numStr = GlobalConstant.getNumStr(user.no + 1)
//        et_person_add_num.setText("$numStr")
//    }

    override fun hideWaitDialog() {
        hideProgressDialog()
    }

    override fun initPresenter(): BasePresenter<*, *> {
        return PersonAddPresenter.newInstance()
    }

    override fun showWaitDialog(waitMsg: String?) {
        showProgressDialog("")
    }

    override fun onSavePersonSuccess(user: User) {
        ToastUtils.showShort(getString(R.string.str_insert_success))
        SPDataManager.saveNewestUserId(user.no)  //保存最新一个人员的id
        if (user.no < GlobalConstant.VALUE_PAGING_DEFAULT) {  //新增人员未满一页
            val notifyChangedEvent = NotifyChangedEvent(NotifyChangedEvent.NotifyChangeEventConstant.OBJ_PERSON_ADD)
            notifyChangedEvent.user = user
            EventBus.getDefault().post(notifyChangedEvent)
        }
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
//        mPresenter.getNewestNumber()

        //自动获取最新的员工编号
        val newestUserId = SPDataManager.getNewestUserId()
        when {
            newestUserId == SPDataManager.SPDataConstant.VALUE_LAST_MEETING_HOST_ID_DEFAULT -> {   //第一次新增
                var numStr = GlobalConstant.getNumStr(1)
                et_person_add_num.setText("$numStr")
            }
            else -> {

                var numStr = GlobalConstant.getNumStr(newestUserId + 1)
                et_person_add_num.setText("$numStr")
            }
        }
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
        user.createTime = System.currentTimeMillis() //设置当前的创建
        user.name = name
        val text = et_person_add_num.text.toString().substring(GlobalConstant.PRE_USER_NUMBER.length)
        user.no = text.toInt()
        user.isSkip = GlobalConstant.VALUE_IS_NOT_SKIP   //默认是不跳过
        user.isDelete = GlobalConstant.VALUE_IS_NOT_DELETE //默认是不删除
        mPresenter.saveUser(user)
    }
}
