package com.sanenchen.janeweather.utils

/**
 * 解析时间
 * @author: sanenchen
 */
class ResolutionTimeUtils(time: String) {
    private val array: CharArray = time.toCharArray()

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
}