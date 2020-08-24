package com.sanenchen.janeweather.activity

import android.Manifest
import android.app.AlertDialog
import android.os.Bundle
import com.amap.api.location.AMapLocationClient
import com.google.android.material.snackbar.Snackbar
import com.permissionx.guolindev.PermissionX
import com.sanenchen.janeweather.R
import com.sanenchen.janeweather.apiKey.ContentUtils
import com.sanenchen.janeweather.fragment.ThreeDayForecastFragment
import com.sanenchen.janeweather.utils.GetWeatherDataUtils
import com.sanenchen.janeweather.utils.LocationUtils
import interfaces.heweather.com.interfacesmodule.view.HeConfig
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*


/**
 * 天气主界面
 * @author: sanenchen
 */
class MainActivity : BaseActivity() {
    /*
    定位,确定要查询的位置
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activity = this
        setToolbar() // 设置Toolbar
        getPermission() // 获取权限
        listenView()
    }

    override fun onDestroy() {
        super.onDestroy()
        AMapLocationClient(this).onDestroy()
    }

    /**
     * 权限申请并调用GPS并加载数据
     */
    private fun getPermission() {
        PermissionX.init(this)
            .permissions(Manifest.permission.ACCESS_FINE_LOCATION)
            .onExplainRequestReason { scope, deniedList ->
                val message = "展示您当前定位的天气信息需要定位权限"
                val ok = "确定"
                scope.showRequestReasonDialog(deniedList, message, ok)
            }
            .onForwardToSettings { scope, deniedList ->
                val message = "您需要去设置当中同意定位权限"
                val ok = "确定"
                scope.showForwardToSettingsDialog(deniedList, message, ok)
            }
            .request { _, _, _ ->
                getGPS()
            }
    }

    private fun getGPS() {
        initHeWeather(null, null) // 尝试从缓存中获取位置数据
        LocationUtils.getInstance(this)
        Snackbar.make(buttonShow14DaysWeather, "获取GPS定位中", Snackbar.LENGTH_SHORT).show()
    }

    /**
     * 加载数据
     */
    companion object {
        lateinit var activity: BaseActivity

        /**
         * 初始化和风天气SDK
         * 更改ApiKey信息也在这里
         */
        fun initHeWeather(location: String?, city: String?) {
            val allRight: Boolean
            /**
             * 缓存并设置
             */
            if (city != null && location != null) {
                val sharedPreferences =
                    activity.getSharedPreferences("LocationData", MODE_PRIVATE).edit()
                sharedPreferences.putString("location", location)
                sharedPreferences.putString("city", city)
                sharedPreferences.apply()
                ContentUtils.place = location // 设置地区
                allRight = true

            } else {
                /**
                 * 从缓存读取并设置
                 */
                val locations = activity.getSharedPreferences("LocationData", MODE_PRIVATE)
                    .getString("location", "")
                if (locations != "") {
                    ContentUtils.place = locations!! // 设置地区
                    HeConfig.init(ContentUtils.publicKey, ContentUtils.appKey)
                    HeConfig.switchToDevService()
                    allRight = true
                } else {
                    allRight = false
                }
            }
            if (allRight) {
                HeConfig.init(ContentUtils.publicKey, ContentUtils.appKey)
                HeConfig.switchToDevService()
                setToolbar() // 设置标题
                getWeatherData() // 获取天气数据
            }
        }

        /**
         * 获取天气数据
         */
        private fun getWeatherData() {
            GetWeatherDataUtils().getWeatherNow(activity) // 获取现在的数据
            GetWeatherDataUtils().getWeather15D(activity) // 获取15天的预报
            GetWeatherDataUtils().getWeather24H(activity) // 获取24小时的天气数据
            GetWeatherDataUtils().getWeatherRain(activity) // 获取分钟级降水信息
        }

        /**
         * 设置Toolbar
         */
        fun setToolbar() {
            activity.setToolbar(
                activity.toolbar,
                "简天气-" + activity.getSharedPreferences("LocationData", MODE_PRIVATE)
                    .getString("city", "")
            )
        }
    }

    /**
     * 监听事件
     */
    private fun listenView() {
        /*
        监听『展开未来14日天气』或『收起未来14日天气』
         */
        buttonShow14DaysWeather.setOnClickListener {
            if (buttonShow14DaysWeather.text == getString(R.string.show15DaysWeather)) {
                buttonShow14DaysWeather.text = getString(R.string.close15DaysWeather)
                ThreeDayForecastFragment.threeDayForecastToRecycler(true)
            } else {
                buttonShow14DaysWeather.text = getString(R.string.show15DaysWeather)
                ThreeDayForecastFragment.threeDayForecastToRecycler(false)
            }
        }

        /*
        监听下滑刷新
         */
        refreshMainLayout.setOnRefreshListener {
            getWeatherData()
        }

        /*
        监听常见问题按钮
         */
        buttonQuestion.setOnClickListener {
            /*
            显示对话框
             */
            AlertDialog
                .Builder(this)
                .setTitle("常见问题")
                .setMessage(
                    "1.外边已经下雨了，为什么数据上还是阴天？\n" +
                            "这是一个好问题，实况数据的生成首先是收集全球各个站点的观测数据后再进行计算，" +
                            "因此会出现不可避免的延迟，因此在某些极端情况下会出现下雨了但数据还是阴天的情况。\n" +
                            "另一方面，城市级预报是覆盖整个城市，而降雨并非完全覆盖城市，有可能你所在的位置下雨，" +
                            "但城市内其他地方没有降雨。\n目前和风天气晴雨准确率已经达到85%，是国家最高标准。\n"
                )
                .setPositiveButton("好的", null)
                .create().show()
        }
    }
}