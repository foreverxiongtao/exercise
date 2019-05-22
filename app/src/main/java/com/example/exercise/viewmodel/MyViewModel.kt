package com.example.exercise.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData

class MyViewModel(application: Application) : AndroidViewModel(application) {

    private val liveData: MutableLiveData<String> = MutableLiveData();

    public fun setData(data: String) {
        liveData.value = data
    }

    fun getData(): String {
        return liveData.value!!
    }

    public fun getLiveData(): MutableLiveData<String> {
        return liveData
    }

}