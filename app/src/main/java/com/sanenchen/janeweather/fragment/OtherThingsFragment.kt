package com.sanenchen.janeweather.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.sanenchen.janeweather.R
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherDailyBean
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherNowBean
import kotlinx.android.synthetic.main.fragment_other_things.view.*

/**
 * 其他信息-Fragment
 * @author: sanenchen
 */
class OtherThingsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_other_things, container, false)
        viewThis = view
        putOtherThingsToView() // 从缓存中尝试获取数据
        return view
    }

    /**
     * 将数据解析并放入view中
     */
    companion object{
        lateinit var viewThis: View
        fun putOtherThingsToView() {
            /*
            从缓存获取数据
             */
            val sharedPreferences = NowWeatherFragment.activityThis.getSharedPreferences(
                "WeatherCache",
                Context.MODE_PRIVATE
            )
            /*
            尝试获取今天的预报
             */
            if (sharedPreferences.getString("weather15D", "") == "") { // 检测是否存在数据
                return
            }
            val weather15DBean: WeatherDailyBean = Gson().fromJson(
                sharedPreferences.getString("weather15D", ""),
                WeatherDailyBean::class.java
            )
            val weatherToday = weather15DBean.daily[0]
            /*
            获取日出时间
             */
            viewThis.textViewOtherSunriseTime.text = weatherToday.sunrise
            /*
            获取日落时间
             */
            viewThis.textViewOtherSunsetTime.text = weatherToday.sunset

            /*
            尝试获取实时数据
             */
            if (sharedPreferences.getString("weatherNow", "") == "") { // 检测是否存在数据
                return
            }
            val weatherNowBean: WeatherNowBean = Gson().fromJson(
                sharedPreferences.getString("weatherNow", ""),
                WeatherNowBean::class.java
            )
            /*
            获取湿度
             */
            viewThis.textViewOtherHumidity.text = weatherNowBean.now.humidity
            /*
            获取降水量
             */
            viewThis.textViewOtherPrecip.text = weatherNowBean.now.precip
            /*
            获取气压
             */
            viewThis.textViewOtherPressure.text = weatherNowBean.now.pressure
            /*
            获取能见度
             */
            viewThis.textViewOtherVis.text = weatherNowBean.now.vis
        }
    }
}