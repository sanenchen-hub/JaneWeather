package com.sanenchen.janeweather.utils

/**
 * 解析时间数据
 * @author: sanenchen
 */
class ResolutionTimeUtils(time: String) {
    private val array: CharArray = time.toCharArray()

    /**
     * 实时天气
     */
    fun getYear(): String {
        return array[0].toString() + array[1].toString() + array[2].toString() + array[3].toString()
    }

    fun getMouth(): String {
        return array[4].toString() + array[5].toString()
    }

    fun getDay(): String {
        return array[6].toString() + array[7].toString()
    }

    fun getHour(): String {
        return array[9].toString() + array[10].toString()
    }

    fun getMinute(): String {
        return array[11].toString() + array[12].toString()
    }

    /**
     * 15日预报天气
     */
    fun getFSMouth(): String {
        return array[5].toString() + array[6].toString()
    }

    fun getFSDay(): String {
        return array[8].toString() + array[9].toString()
    }

    /**
     * 小时级天气
     */
    fun getHourlyTime(): String {
        return array[11].toString() + array[12].toString() + array[13].toString() + array[14].toString() + array[15].toString()
    }
}