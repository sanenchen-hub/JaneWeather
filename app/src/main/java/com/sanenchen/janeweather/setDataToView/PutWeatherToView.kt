package com.sanenchen.janeweather.setDataToView

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.google.gson.Gson
import com.sanenchen.janeweather.utils.ResolutionTimeUtils
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherNowBean
import kotlinx.android.synthetic.main.fragment_now_weather.view.*

/**
 * 所有的天气数据都会在这里设置到View
 * @author: sanenchen
 */
class PutWeatherToView(private val viewThis: View, private val activity: Activity) {

    /**
     * 解析现在的天气数据并放入控件中
     */
    fun analyzeNowWeather() {
        val sharedPreferences = activity.getSharedPreferences(
            "WeatherCache",
            Context.MODE_PRIVATE
        )
        if (sharedPreferences.getString("weatherNow", "") == "") {
            return
        }
        val weatherNowBean: WeatherNowBean = Gson().fromJson(
            sharedPreferences.getString("weatherNow", ""),
            WeatherNowBean::class.java
        )
        viewThis.textViewMainWeather.text = weatherNowBean.now.text// 天气
        /*
        当前温度
         */
        val temp = weatherNowBean.now.temp + "°"
        viewThis.textViewMainTemp.text = temp
        /*
        图片图标
         */
        val resID = viewThis.resources.getIdentifier(
            "icon" + weatherNowBean.now.icon,
            "drawable", "com.sanenchen.janeweather"
        )
        viewThis.imageViewMainImage.setImageDrawable(
            ResourcesCompat.getDrawable(
                viewThis.resources,
                resID,
                null
            )
        )
        /*
        观测时间
         */
        val obsTime = weatherNowBean.now.obsTime
        if (obsTime != null) {
            val rT = ResolutionTimeUtils(obsTime)
            val updateData =
                rT.getYear() + "/" + rT.getMouth() + "/" + rT.getDay() + "/" + rT.getHour() + ":" + rT.getMinute()
            viewThis.textViewMainTime.text = updateData
        }
    }
}