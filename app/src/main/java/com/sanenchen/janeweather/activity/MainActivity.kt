package com.sanenchen.janeweather.activity

import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.sanenchen.janeweather.R
import com.sanenchen.janeweather.apiKey.ApiKey
import com.sanenchen.janeweather.fragment.ThreeDayForecastFragment
import com.sanenchen.janeweather.utils.GetWeatherDataUtils
import interfaces.heweather.com.interfacesmodule.view.HeConfig
import kotlinx.android.synthetic.main.activity_main.*

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
        listenView() // 监听事件
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

    /**
     * 监听事件
     */
    private fun listenView() {
        /*
        监听『展开未来14日天气』或『收起未来14日天气』
         */
        buttonShow14DaysWeather.setOnClickListener {
            if (buttonShow14DaysWeather.text == getString(R.string.show15DaysWeather)) {
                buttonShow14DaysWeather.text = getString(R.string.close15DaysWeather)
                ThreeDayForecastFragment.threeDayForecastToRecycler(true)
            } else {
                buttonShow14DaysWeather.text = getString(R.string.show15DaysWeather)
                ThreeDayForecastFragment.threeDayForecastToRecycler(false)
            }
        }

        /*
        监听下滑刷新
         */
        refreshMainLayout.setOnRefreshListener {
            getWeatherData()
            Thread(Runnable {
                Thread.sleep(1000)
                runOnUiThread {
                    refreshMainLayout.isRefreshing = false
                    Snackbar.make(refreshMainLayout, "刷新成功", Snackbar.LENGTH_SHORT).show()
                }
            }).start()
        }
    }
}