package com.example.exercise

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.exercise.presenter.BasePresenter
import com.example.exercise.viewmodel.MyViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mPresenter: BasePresenter = BasePresenter()
    private var viewModel: MyViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycle.addObserver(mPresenter)
        initData()
    }


    private fun initData() {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(MyViewModel::class.java)
        val observer = Observer<String> {
            tv_main_text.text = it!!
        }

        viewModel!!.getLiveData().observe(this, observer)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(mPresenter)
    }

    fun Click(view: View) {
        Log.d("Click", "Click")
        viewModel?.setData("usb")
    }
}
