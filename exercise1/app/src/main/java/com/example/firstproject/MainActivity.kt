package com.example.firstproject

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.firstproject.service.MyService
import kotlinx.android.synthetic.main.activity_main.*

/**
 *    author : desperado
 *    e-mail : foreverxiongtao@sina.com
 *    date   : 2019/4/25 上午12:01
 *    desc   : 主页面
 *    version: 1.0
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_main_startService.setOnClickListener {
            startMusicService()
        }
    }

    /**
     * method:启动服务
     */
    private fun startMusicService() {
        val intent = Intent(MainActivity@this,MyService::class.java)
        startService(intent)
    }
}