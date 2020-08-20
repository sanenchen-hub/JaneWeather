package com.sanenchen.janeweather.fragment

import android.app.Activity
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
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherDailyBean
import kotlinx.android.synthetic.main.fragment_three_day_forecast.view.*
import kotlinx.android.synthetic.main.item_three_day_forecast.view.*

/**
 * 3天预报-Fragment
 * @author: sanenchen
 **/
class ThreeDayForecastFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_three_day_forecast, container, false)
        viewThis = view
        activityThis = activity!!
        threeDayForecastToRecycler(false) // 从缓存中读取数据
        return view
    }

    /**
     * 将数据放入RecyclerView
     */
    companion object {
        lateinit var viewThis: View
        lateinit var activityThis: Activity
        var isShow = false
        fun threeDayForecastToRecycler(isShow: Boolean) {
            /*
            isShow: 判断是否展示未来14日的天气
             */
            this.isShow = isShow
            /*
            获取数据
             */
            val sharedPreferences = NowWeatherFragment.activityThis.getSharedPreferences(
                "WeatherCache",
                Context.MODE_PRIVATE
            )
            /*
            尝试获取未来3天天气
             */
            if (sharedPreferences.getString("weather15D", "") == "") { // 检测是否存在数据
                return
            }
            val weather15DBean: WeatherDailyBean = Gson().fromJson(
                sharedPreferences.getString("weather15D", ""),
                WeatherDailyBean::class.java
            )
            val list = arrayListOf<ThreeDayForecastData>()
            /*
            将数据信息放入Data中
             */
            if (isShow) {
                for (index in 1..14) {
                    val weatherForecast = weather15DBean.daily[index]
                    list.add(
                        ThreeDayForecastData(
                            weatherForecast.tempMax,
                            weatherForecast.tempMin,
                            weatherForecast.textDay,
                            weatherForecast.fxDate,
                            weatherForecast.iconDay
                        )
                    )
                }
            } else {
                for (index in 1..3) {
                    val weatherForecast = weather15DBean.daily[index]
                    list.add(
                        ThreeDayForecastData(
                            weatherForecast.tempMax,
                            weatherForecast.tempMin,
                            weatherForecast.textDay,
                            weatherForecast.fxDate,
                            weatherForecast.iconDay
                        )
                    )
                }
            }

            viewThis.recyclerViewThreeDayForecast.adapter =
                ThreeDayForecastAdapter(list, viewThis.context)
            viewThis.recyclerViewThreeDayForecast.layoutManager =
                LinearLayoutManager(viewThis.context)
        }
    }

    /**
     * RecyclerView的适配器
     */
    class ThreeDayForecastAdapter(
        private val data: List<ThreeDayForecastData>,
        private val context: Context
    ) :
        RecyclerView.Adapter<ThreeDayForecastAdapter.InnerHolder>() {
        override fun onCreateViewHolder(
            viewGroup: ViewGroup,
            viewType: Int
        ): InnerHolder {
            //加载View
            val itemView: View =
                LayoutInflater.from(context)
                    .inflate(R.layout.item_three_day_forecast, viewGroup, false)
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
            val imageViewCloudIcon = itemView.imageViewCloudIcon!!
            val textViewWeatherData = itemView.textViewWeatherData!!
            val textViewWeatherMaxMinTemp = itemView.textViewWeatherMaxMinTemp!!
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
                .into(innerHolder.imageViewCloudIcon)

            /*
            设置天气信息
             */
            val weatherText = when (position) {
                0 -> {
                    "明天"
                }
                1 -> {
                    "后天"
                }
                2 -> {
                    "大后天"
                }
                else -> {
                    ResolutionTimeUtils(data[position].fxDate).getFSMouth() +
                            "月" +
                            ResolutionTimeUtils(data[position].fxDate).getFSDay() + "日"
                }
            } + " • " + data[position].textDay
            innerHolder.textViewWeatherData.text = weatherText

            /*
            设置最高温度与最低温度
             */
            val weatherMaxMinTemp = data[position].maxTemp + "° / " + data[position].minTemp
            innerHolder.textViewWeatherMaxMinTemp.text = weatherMaxMinTemp
        }
    }

    /**
     * 数据类
     */
    data class ThreeDayForecastData(
        val maxTemp: String,
        val minTemp: String,
        val textDay: String,
        val fxDate: String,
        val icon: String
    )
}