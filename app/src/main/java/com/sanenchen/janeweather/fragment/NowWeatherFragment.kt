package com.sanenchen.janeweather.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sanenchen.janeweather.R
import com.sanenchen.janeweather.setDataToView.PutWeatherToView
import com.sanenchen.janeweather.utils.GetWeatherDataUtils
/**
 * 实况天气-碎片
 * @author: sanenchen
 */
class NowWeatherFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_now_weather, container)
        PutWeatherToView(view, activity!!).analyzeNowWeather()// 从缓存中获取数据
        GetWeatherDataUtils().geWeatherNow(view, activity!!)
        return view
    }
}
