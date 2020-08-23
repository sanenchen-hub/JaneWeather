package com.sanenchen.janeweather.utils

import android.content.Context
import android.util.Log
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.sanenchen.janeweather.activity.MainActivity

/**
 * 定位--当前所在的位置
 * @author: CSDN on the Intent
 */
class LocationUtils constructor(context: Context) {
    private var locationClient: AMapLocationClient? = null
    private var locationOption: AMapLocationClientOption? = null
    private fun initLocation(context: Context?) {
        //初始化client
        locationClient = AMapLocationClient(context)
        locationOption = AMapLocationClientOption()
        locationOption!!.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        locationOption = defaultOption
        //设置定位参数
        locationClient!!.setLocationOption(locationOption)
        // 设置定位监听
        locationClient!!.setLocationListener(locationListener)
        startLocation()
    }

    /**
     * 开始定位
     */
    private fun startLocation() {
        // 设置定位参数
        locationClient!!.setLocationOption(locationOption)
        // 启动定位
        locationClient!!.startLocation()
    }

    private val locationListener = AMapLocationListener { location ->
        if (location != null) {
            if (location.errorCode == 0) {
                Log.d("Sophie", " --- 经纬度 --- " + location.longitude + location.latitude)

                val locationData =
                    location.longitude.toString() + "," + location.latitude.toString()
                MainActivity.initHeWeather(locationData, location.district + "-" + location.street)
                stopLocation()
                destroyLocation()
            } else {
                Log.d("Sophie", " --- 定位失败 --- ")
                Log.d("Sophie", "错误码:" + location.errorCode)
                Log.d("Sophie", "错误信息:" + location.errorInfo)
                Log.d("Sophie", "错误描述:" + location.locationDetail)
            }
        }
    }

    /**
     * 定位参数
     */
    private val defaultOption: AMapLocationClientOption
        get() {
            val mOption = AMapLocationClientOption()
            mOption.locationMode =
                AMapLocationClientOption.AMapLocationMode.Hight_Accuracy //可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
            mOption.isGpsFirst = false //可选，设置是否gps优先，只在高精度模式下有效。默认关闭
            mOption.httpTimeOut = 30000 //可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
            mOption.isNeedAddress = true //可选，设置是否返回逆地理地址信息。默认是true
            mOption.isOnceLocation = true //可选，设置是否单次定位。默认是false
            mOption.isOnceLocationLatest =
                false //可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
            AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP) //可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
            mOption.isSensorEnable = false //可选，设置是否使用传感器。默认是false
            mOption.isWifiScan =
                true //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
            mOption.isLocationCacheEnable = true //可选，设置是否使用缓存定位，默认为true
            return mOption
        }

    /**
     * 停止定位
     */
    private fun stopLocation() {
        // 停止定位
        locationClient!!.stopLocation()
    }

    /**
     * 销毁定位
     */
    private fun destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient!!.onDestroy()
            locationClient = null
            locationOption = null
        }
    }

    companion object {
        private var instance: LocationUtils? = null
        fun getInstance(context: Context): LocationUtils? {
            if (instance == null) {
                instance = LocationUtils(context)
            }
            return instance
        }
    }

    init {
        initLocation(context)
    }
}