package com.example.firstproject

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
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
        val intent = Intent(this, MyService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val request_code = intent?.getIntExtra(MyService.KEY_REQUEST_CODE, MyService.REQUEST_CODE_NORMAL)
        when (request_code) {
            MyService.REQUEST_CODE_PREVIOUSE_SONG -> Toast.makeText(this, resources.getString(R.string.str_notify_previouse), Toast.LENGTH_SHORT).show()
            MyService.REQUEST_CODE_PAUSE_SONG -> Toast.makeText(this, resources.getString(R.string.str_notify_start), Toast.LENGTH_SHORT).show()
            MyService.REQUEST_CODE_NEXT_SONG -> Toast.makeText(this, resources.getString(R.string.str_notify_next), Toast.LENGTH_SHORT).show()
        }

    }
}