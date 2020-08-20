package com.sanenchen.janeweather.utils

import android.app.Activity
import android.util.Log
import com.sanenchen.janeweather.apiKey.ApiKey
import com.sanenchen.janeweather.fragment.NowWeatherFragment
import com.sanenchen.janeweather.fragment.ThreeDayForecastFragment
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherDailyBean
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherNowBean
import interfaces.heweather.com.interfacesmodule.view.HeWeather

/**
 * 从服务器获取天气信息
 * @author: sanenchen
 */
class GetWeatherDataUtils {

    /**
     * 获取实况天气数据
     */
    fun getWeatherNow(activity: Activity) {
        HeWeather.getWeatherNow(
            activity,
            ApiKey.place,
            object : HeWeather.OnResultWeatherNowListener {
                override fun onError(error: Throwable) {
                    Log.e("HeWeatherSDKERROR", error.toString())
                }

                override fun onSuccess(weatherBean: WeatherNowBean) {
                    ObjectStorageUtils().storageWeatherNowCache(weatherBean, activity)
                    /*
                    加载进控件
                     */
                    NowWeatherFragment.analyzeNowWeather()
                }
            })
    }

    /**
     * 获取15天天气
     */
    fun getWeather15D(activity: Activity) {
        HeWeather.getWeather15D(
            activity,
            ApiKey.place,
            object : HeWeather.OnResultWeatherDailyListener {
                override fun onError(error: Throwable) {
                    Log.e("HeWeatherSDKERROR", error.toString())
                }

                override fun onSuccess(weatherBean: WeatherDailyBean) {
                    ObjectStorageUtils().storageWeather15DCache(weatherBean, activity)
                    /*
                    加载进控件
                    */
                    NowWeatherFragment.analyzeNowWeather()
                    ThreeDayForecastFragment.threeDayForecastToRecycler(ThreeDayForecastFragment.isShow)
                }
            })
    }
}