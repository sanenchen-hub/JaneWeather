package com.sanenchen.janeweather.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.sanenchen.janeweather.R
import com.sanenchen.janeweather.utils.ResolutionTimeUtils
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherHourlyBean
import kotlinx.android.synthetic.main.fragment_hourly_forecast.view.*
import kotlinx.android.synthetic.main.item_hourly_forecast.view.*

/**
 * 小时级预报-24小时-Fragment
 * @author: sanenchen
 */
class HourlyForecastFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_hourly_forecast, container, false)
        viewThis = view
        twentyFourForecastToRecycler()
        return view
    }

    /**
     * 解析数据放入RecyclerView
     */
    companion object {
        lateinit var viewThis: View
        fun twentyFourForecastToRecycler() {
            /*
            获取数据
             */
            val sharedPreferences = NowWeatherFragment.activityThis.getSharedPreferences(
                "WeatherCache",
                Context.MODE_PRIVATE
            )
            /*
            尝试获取24小时天气
             */
            if (sharedPreferences.getString("weather24H", "") == "") { // 检测是否存在数据
                return
            }
            val weatherHourlyBean: WeatherHourlyBean = Gson().fromJson(
                sharedPreferences.getString("weather24H", ""),
                WeatherHourlyBean::class.java
            )
            val list = arrayListOf<TwentyFourHourForecastData>()
            /*
            将数据信息放入Data中
             */
            for (index in 0..23) {
                val weatherForecast = weatherHourlyBean.hourly[index]
                list.add(
                    TwentyFourHourForecastData(
                        weatherForecast.temp,
                        weatherForecast.fxTime,
                        weatherForecast.icon
                    )
                )
            }

            viewThis.recyclerViewHourlyForecast.adapter =
                TwentyFourHourForecastAdapter(list, viewThis.context)
            /*
            RecyclerView布局
             */
            val layoutManager = LinearLayoutManager(viewThis.context)
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            viewThis.recyclerViewHourlyForecast.layoutManager = layoutManager

        }
    }

    /**
     * RecyclerView的适配器
     */
    class TwentyFourHourForecastAdapter(
        private val data: List<TwentyFourHourForecastData>,
        private val context: Context
    ) :
        RecyclerView.Adapter<TwentyFourHourForecastAdapter.InnerHolder>() {
        override fun onCreateViewHolder(
            viewGroup: ViewGroup,
            viewType: Int
        ): InnerHolder {
            //加载View
            val itemView: View =
                LayoutInflater.from(context)
                    .inflate(R.layout.item_hourly_forecast, viewGroup, false)
            return InnerHolder(itemView)
        }

        /**
         * 得到总条数
         */
        override fun getItemCount(): Int = data.size

        /**
         * 控件注册
         */
        class InnerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textView24HourlyTime = itemView.textView24HourlyTime!!
            val imageView24HourlyIcon = itemView.imageView24HourlyIcon!!
            val textView24HourlyTemp = itemView.textView24HourlyTemp!!
        }

        /**
         * 绑定数据，View和数据绑定
         */
        override fun onBindViewHolder(innerHolder: InnerHolder, position: Int) {
            /*
            设置天气图标
             */
            Glide.with(NowWeatherFragment.viewThis)
                .load("https://a.hecdn.net/img/plugin/190516/icon/c/" + data[position].icon + "d.png")
                .into(innerHolder.imageView24HourlyIcon)

            /*
            设置时间
             */
            val weatherTime = ResolutionTimeUtils(data[position].fxTime).getHourlyTime()
            innerHolder.textView24HourlyTime.text = weatherTime
            /*
            设置温度
             */
            val weatherTemp = data[position].Temp + "°"
            innerHolder.textView24HourlyTemp.text = weatherTemp
        }
    }

    /**
     * 数据类
     */
    data class TwentyFourHourForecastData(
        val Temp: String,
        val fxTime: String,
        val icon: String
    )
}