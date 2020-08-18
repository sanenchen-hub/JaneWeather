package com.sanenchen.janeweather.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.sanenchen.janeweather.R
import com.sanenchen.janeweather.utils.GetWeatherDataUtils
import com.sanenchen.janeweather.utils.ResolutionTimeUtils
import kotlinx.android.synthetic.main.fragment_now_weather.view.*

/**
 * 实况天气-碎片
 */
class NowWeatherFragment : Fragment() {
    /**
     * 全局View
     */
    private lateinit var viewThis: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_now_weather, container)
        this.viewThis = view
        analyzeWeather() // 从缓存中获取数据
        getWeatherNow() // 获取天气数据
        return view
    }

    /**
     * 获取现在天气数据
     */
    private fun getWeatherNow() {
        val weatherBean = GetWeatherDataUtils().getWeatherNow(activity!!)
        val editor = activity!!.getSharedPreferences(
            "weatherCache",
            Context.MODE_PRIVATE
        )!!.edit()
        if (editor != null) {
            editor.putString("weather", weatherBean.now.text)
            editor.putString("temp", weatherBean.now.temp)
            editor.putString("icon", weatherBean.now.icon)
            editor.putString("obsTime", weatherBean.now.obsTime)
            editor.apply()
            analyzeWeather()
        }
    }

    /**
     * 解析现在的天气数据并放入控件中
     */
    private fun analyzeWeather() {
        /**
         * 将缓存的数据放入控件中
         */
        val preferences =
            activity!!.getSharedPreferences("weatherCache", Context.MODE_PRIVATE) // 获取缓存里的数据
        viewThis.textViewMainWeather.text = preferences.getString("weather", "----") // 天气
        /*
        当前温度
         */
        val temp = preferences.getString("temp", "----") + "°"
        viewThis.textViewMainTemp.text = temp
        /*
        图片图标
         */
        val resID = resources.getIdentifier(
            "icon" + preferences.getString("icon", "100"),
            "drawable", "com.sanenchen.janeweather"
        )
        viewThis.imageViewMainImage.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                resID,
                null
            )
        )
        /*
        观测时间
         */
        val obsTime = preferences.getString("obsTime", "----")
        if (obsTime != null) {
            val rT = ResolutionTimeUtils(obsTime)
            val updateData =
                rT.getYear() + "/" + rT.getMouth() + "/" + rT.getDay() + "/" + rT.getHour() + ":" + rT.getMinute()
            viewThis.textViewMainTime.text = updateData
        }
    }
}