package com.sanenchen.janeweather.fragment

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.sanenchen.janeweather.R
import interfaces.heweather.com.interfacesmodule.bean.MinutelyBean
import kotlinx.android.synthetic.main.fragment_minutely_rain.view.*

/**
 * 分钟级降水预测-Fragment
 * @author: sanenchen
 */
class MinutelyRainFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_minutely_rain, container, false)
        viewThis = view
        activityThis = activity!!
        setSummary() // 尝试从缓存中获取数据
        return view
    }

    /**
     * 设置降水预测信息
     */
    companion object {
        lateinit var viewThis: View
        lateinit var activityThis: Activity
        /**
         * 设置总结信息
         */
        fun setSummary() {
            /**
             * 从缓存获取信息
             */
            val sharedPreferences = activityThis.getSharedPreferences("WeatherCache", MODE_PRIVATE)
            if (sharedPreferences.getString("weatherRain", "") == "") {
                return
            }
            val minutelyBean: MinutelyBean = Gson().fromJson(
                sharedPreferences.getString("weatherRain", ""),
                MinutelyBean::class.java
            )

            /**
             * 设置信息
             */
            viewThis.textViewSummary.text = minutelyBean.summary
        }
    }
}