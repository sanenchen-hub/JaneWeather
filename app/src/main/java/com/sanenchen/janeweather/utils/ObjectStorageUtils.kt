package com.sanenchen.janeweather.utils

import android.app.Activity
import android.content.Context
import com.google.gson.Gson
import interfaces.heweather.com.interfacesmodule.bean.MinutelyBean
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherDailyBean
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherHourlyBean
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherNowBean

/**
 * 天气数据存入缓存
 * @author: sanenchen
 */
class ObjectStorageUtils {
    /**
     * 存入实时天气的Cache
     */
    fun storageWeatherNowCache(weatherNowBean: WeatherNowBean, activity: Activity) {
        val editor = activity.getSharedPreferences("WeatherCache", Context.MODE_PRIVATE).edit()
        val jsonObject = Gson().toJson(weatherNowBean).toString()
        editor.putString("weatherNow", jsonObject)
        editor.apply()
    }

    /**
     * 存入15日天气的Cache
     */
    fun storageWeather15DCache(weatherDailyBean: WeatherDailyBean, activity: Activity) {
        val editor = activity.getSharedPreferences("WeatherCache", Context.MODE_PRIVATE).edit()
        val jsonObject = Gson().toJson(weatherDailyBean).toString()
        editor.putString("weather15D", jsonObject)
        editor.apply()
    }

    /**
     * 存入24小时天气数据
     */
    fun storageWeather24HourlyCache(weatherHourlyBean: WeatherHourlyBean, activity: Activity) {
        val editor = activity.getSharedPreferences("WeatherCache", Context.MODE_PRIVATE).edit()
        val jsonObject = Gson().toJson(weatherHourlyBean).toString()
        editor.putString("weather24H", jsonObject)
        editor.apply()
    }

    /**
     * 存入分钟降雨数据
     */
    fun storageWeatherRainCache(minutelyBean: MinutelyBean, activity: Activity) {
        val editor = activity.getSharedPreferences("WeatherCache", Context.MODE_PRIVATE).edit()
        val jsonObject = Gson().toJson(minutelyBean).toString()
        editor.putString("weatherRain", jsonObject)
        editor.apply()
    }
}