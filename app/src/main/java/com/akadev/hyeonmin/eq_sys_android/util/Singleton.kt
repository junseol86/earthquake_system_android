package com.akadev.hyeonmin.eq_sys_android.util

import android.util.Base64
import org.json.JSONObject

object Singleton {
    var jwtToken = ""
    var memberInfo: Map<String, String>? = null
    var earthquakeInfo: Map<String, String>? = null

    fun loginResult(jt: String) {
        jwtToken = jt

        val jo = JSONObject(String(Base64.decode(jwtToken.split(".")[1], Base64.DEFAULT), Charsets.UTF_8))
        var mi = HashMap<String, String>()
        for (key in jo.keys()) {
            mi[key] = jo.get(key).toString()
        }
        memberInfo = mi
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