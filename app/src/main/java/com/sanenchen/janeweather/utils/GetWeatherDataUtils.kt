package com.sanenchen.janeweather.utils

import android.app.Activity
import android.util.Log
import android.view.View
import com.sanenchen.janeweather.apiKey.ApiKey
import com.sanenchen.janeweather.setDataToView.PutWeatherToView
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
    fun geWeatherNow(view: View, activity: Activity) {
        HeWeather.getWeatherNow(
            activity,
            ApiKey.place,
            object : HeWeather.OnResultWeatherNowListener {
                override fun onError(error: Throwable) {
                    Log.e("HeWeatherSDKERROR", error.toString())
                }

                override fun onSuccess(weatherBean: WeatherNowBean) {
                    ObjectStorageUtils().storageWeatherNowCache(weatherBean, activity)
                    PutWeatherToView(view, activity).analyzeNowWeather()
                }
            })
    }
}