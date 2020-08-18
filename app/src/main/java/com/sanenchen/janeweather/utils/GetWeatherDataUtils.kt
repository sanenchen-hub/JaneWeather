package com.sanenchen.janeweather.utils

import android.app.Activity
import android.util.Log
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherNowBean
import interfaces.heweather.com.interfacesmodule.view.HeWeather

class GetWeatherDataUtils {
    fun getWeatherNow(activity: Activity): WeatherNowBean {
        var weatherNowBean = WeatherNowBean()
        HeWeather.getWeatherNow(
            activity,
            "CN101191003",
            object : HeWeather.OnResultWeatherNowListener {
                override fun onError(error: Throwable) {
                    Log.e("HeWeatherSDKERROR", error.toString())
                }

                override fun onSuccess(weatherBean: WeatherNowBean) {
                    if (weatherBean.code == "200") {
                        weatherNowBean = weatherBean
                    }
                }
            })
        return weatherNowBean
    }
}