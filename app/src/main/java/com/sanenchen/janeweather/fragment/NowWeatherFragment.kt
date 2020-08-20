package com.sanenchen.janeweather.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.sanenchen.janeweather.R
import com.sanenchen.janeweather.utils.GetWeatherDataUtils
import com.sanenchen.janeweather.utils.ResolutionTimeUtils
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherDailyBean
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherNowBean
import kotlinx.android.synthetic.main.fragment_now_weather.view.*

/**
 * 实况天气-Fragment
 * @author: sanenchen
 */
class NowWeatherFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_now_weather, container)
        viewThis = view
        activityThis = activity!!
        analyzeNowWeather() // 从缓存中获取数据
        return view
    }

    /**
     * 解析现在的天气数据并放入控件中
     */
    companion object AnalyzeNowWeather {
        lateinit var activityThis: Activity
        lateinit var viewThis: View
        fun analyzeNowWeather() {
            val sharedPreferences = activityThis.getSharedPreferences(
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
            图片图标 (从网络获取)
             */
            Glide.with(viewThis)
                .load("https://a.hecdn.net/img/plugin/190516/icon/c/" + weatherNowBean.now.icon + "d.png")
                .into(viewThis.imageViewMainImage)
            viewThis.imageViewMainImage.visibility = View.VISIBLE
            /*
            观测时间
             */
            val obsTime = weatherNowBean.now.obsTime
            if (obsTime != null) {
                val rT = ResolutionTimeUtils(obsTime)
                val updateData =
                    rT.getYear() + "/" + rT.getMouth() + "/" + rT.getDay() + " " + rT.getHour() + ":" + rT.getMinute()
                viewThis.textViewMainTime.text = updateData
            }
            /*
            尝试获取今日最高温度与最低温度
             */
            if (sharedPreferences.getString("weather15D", "") == "") { // 检测是否存在数据
                return
            }
            val weather15DBean: WeatherDailyBean = Gson().fromJson(
                sharedPreferences.getString("weather15D", ""),
                WeatherDailyBean::class.java
            )
            val today = weather15DBean.daily[0]
            val tempMax = today.tempMax + "°"
            val tempMin = today.tempMin + "°"
            val formatMaxMinTemp = "$tempMax / $tempMin"
            viewThis.textViewMMaxMinTemp.text = formatMaxMinTemp
        }
    }
}
