package com.akadev.hyeonmin.eq_sys_android.util

import com.akadev.hyeonmin.eq_sys_android.R
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object Tool {

    fun jaToAL(ja: JSONArray): ArrayList<Map<String, String>> {

        var result = ArrayList<Map<String, String>>()

        for (i in 0 until ja.length()) {
            val jo = ja[i] as JSONObject
            var map = HashMap<String, String>()
            for (key in jo.keys()) {
                map[key] = jo[key.toString()].toString()
            }
            result.add(map)
        }

        return result
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }
    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }
    fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist *= 60.0 * 1.1515 * 1.609344
        return dist
    }

    fun isReportedToday(dateStr: String?): Boolean {
        if (dateStr == null || dateStr == "null")
            return false

        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.KOREA)
        format.timeZone = TimeZone.getTimeZone("ASIA/SEOUL")
        val givenDate = format.parse(dateStr)
        val today = Date()
        val diff = TimeUnit.DAYS.convert(today.time - givenDate.time, TimeUnit.MILLISECONDS)
        return (diff < 1)
    }

}