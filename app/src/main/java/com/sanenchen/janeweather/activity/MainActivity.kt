package com.sanenchen.janeweather.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sanenchen.janeweather.R
import com.sanenchen.janeweather.apiKey.ApiKey
import com.sanenchen.janeweather.utils.GetWeatherDataUtils
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
        getWeatherData() // 获取数据
    }

    /**
     * 初始化和风天气SDK
     * 更改ApiKey信息也在这里
     */
    private fun initHeWeather() {
        HeConfig.init(ApiKey.publicKey, ApiKey.appKey)
        HeConfig.switchToDevService()
    }

    /**
     * 获取天气数据
     */
    private fun getWeatherData() {
        GetWeatherDataUtils().getWeatherNow(this) // 获取现在的数据
        GetWeatherDataUtils().getWeather15D(this) // 获取15天的预报
    }
}