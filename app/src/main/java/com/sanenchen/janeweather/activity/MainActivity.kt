package com.sanenchen.janeweather.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sanenchen.janeweather.R
import com.sanenchen.janeweather.apiKey.ApiKey
import interfaces.heweather.com.interfacesmodule.view.HeConfig

/**
 * 天气主界面
 * @author: sanenchen
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initHeWeather() // 初始化SDK
    }

    /**
     * 初始化和风天气SDK
     * 更改ApiKey信息也在这里
     */
    private fun initHeWeather() {
        HeConfig.init(ApiKey.publicKey, ApiKey.appKey)
        HeConfig.switchToDevService()
    }
}