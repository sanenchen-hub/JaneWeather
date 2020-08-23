package com.sanenchen.janeweather.utils

import android.app.Activity
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.sanenchen.janeweather.apiKey.ContentUtils
import com.sanenchen.janeweather.fragment.*
import interfaces.heweather.com.interfacesmodule.bean.MinutelyBean
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherDailyBean
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherHourlyBean
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherNowBean
import interfaces.heweather.com.interfacesmodule.view.HeWeather
import kotlinx.android.synthetic.main.activity_main.*

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
            ContentUtils.place,
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
                    OtherThingsFragment.putOtherThingsToView() // 加载其他信息进控件
                }
            })
    }

    /**
     * 获取15日天气
     */
    fun getWeather15D(activity: Activity) {
        HeWeather.getWeather15D(
            activity,
            ContentUtils.place,
            object : HeWeather.OnResultWeatherDailyListener {
                override fun onError(error: Throwable) {
                    Log.e("HeWeatherSDKERROR", error.toString())
                }

                override fun onSuccess(weatherBean: WeatherDailyBean) {
                    ObjectStorageUtils().storageWeather15DCache(weatherBean, activity)
                    /*
                    加载进控件
                    */
                    ThreeDayForecastFragment.threeDayForecastToRecycler(ThreeDayForecastFragment.isShow)
                    OtherThingsFragment.putOtherThingsToView() // 加载其他信息进控件
                }
            })
    }

    /**
     * 获取小时级天气
     */
    fun getWeather24H(activity: Activity) {
        HeWeather.getWeather24Hourly(
            activity,
            ContentUtils.place,
            object : HeWeather.OnResultWeatherHourlyListener {
                override fun onError(error: Throwable) {
                    Log.e("HeWeatherSDKERROR", error.toString())
                    /*
                    错误返回
                     */
                    activity.refreshMainLayout.isRefreshing = false
                    Snackbar.make(activity.refreshMainLayout, "数据加载失败：$error", Snackbar.LENGTH_SHORT).show()
                }

                override fun onSuccess(weatherBean: WeatherHourlyBean) {
                    ObjectStorageUtils().storageWeather24HourlyCache(weatherBean, activity)
                    /*
                    加载进控件
                    */
                    HourlyForecastFragment.twentyFourForecastToRecycler()
                    OtherThingsFragment.putOtherThingsToView() // 加载其他信息进控件
                }
            })
    }

    /**
     * 获取分钟级降雨信息
     */
    fun getWeatherRain(activity: Activity) {
        HeWeather.getMinuteLy(
            activity,
            ContentUtils.place,
            object : HeWeather.OnResultMinutelyListener {
                override fun onError(error: Throwable) {
                    Log.e("HeWeatherSDKERROR", error.toString())
                    /*
                    错误返回
                     */
                    activity.refreshMainLayout.isRefreshing = false
                    Snackbar.make(activity.refreshMainLayout, "数据加载失败：$error", Snackbar.LENGTH_SHORT).show()
                }

                override fun onSuccess(minutelyBean: MinutelyBean) {
                    ObjectStorageUtils().storageWeatherRainCache(minutelyBean, activity)
                    /*
                    加载进控件
                    */
                    MinutelyRainFragment.setSummary() // 加载信息到控件
                    /*
                    Log打印
                     */
                    Log.i("ResultJson", Gson().toJson(minutelyBean).toString())
                    /*
                    结束返回
                     */
                    activity.refreshMainLayout.isRefreshing = false
                    Snackbar.make(activity.refreshMainLayout, "天气数据刷新成功", Snackbar.LENGTH_SHORT).show()
                }
            })
    }
}