package com.sanenchen.janeweather.utils

import android.app.Activity
import android.content.Context
import com.google.gson.Gson
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherNowBean

/**
 * 天气数据存入缓存
 * @author: sanenchen
 */
class ObjectStorageUtils {
    fun storageWeatherNowCache(weatherNowBean: WeatherNowBean, activity: Activity) {
        val editor = activity.getSharedPreferences("WeatherCache", Context.MODE_PRIVATE).edit()
        val jsonObject = Gson().toJson(weatherNowBean).toString()
        editor.putString("weatherNow", jsonObject)
        editor.apply()
    }
}