package com.akadev.hyeonmin.eq_sys_android.util

import android.location.Location
import android.util.Base64
import org.json.JSONObject

object Singleton {
    var activityOn = false

    var jwtToken = ""
    var memberInfo: Map<String, String>? = null
    var earthquakeInfo: Map<String, String>? = null
    var fcmRefreshed = false
    var myLoc: Location? = null

    var getChatsBeforeLocked = true

    fun loginResult(jt: String) {
        jwtToken = jt

        val jo = JSONObject(String(Base64.decode(jwtToken.split(".")[1], Base64.DEFAULT), Charsets.UTF_8))
        var mi = HashMap<String, String>()
        for (key in jo.keys()) {
            mi[key] = jo.get(key).toString()
        }
        memberInfo = mi
    }

    fun refreshJwtToken(response: String) {
        val jo = JSONObject(response)
        loginResult(jo["jwtToken"] as String)
    }

    fun earthquakeGetListResult(eqList: List<Map<String, String>>) {
        var ei: Map<String, String>? = null
        eqList.map {
            if (it["eq_active"]!!.equals("1"))
                ei = it
        }
        earthquakeInfo = ei
    }

}