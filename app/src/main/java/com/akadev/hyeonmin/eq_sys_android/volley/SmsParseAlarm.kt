package com.akadev.hyeonmin.eq_sys_android.volley

import android.content.Context
import com.akadev.hyeonmin.eq_sys_android.util.Const
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class SmsParseAlarm(val ctx: Context) {
    private val queue = Volley.newRequestQueue(ctx)

    fun sendReport(str: String) {
        val request: StringRequest = object: StringRequest(Request.Method.POST,
                Const.apiUrl + "structure/smsParseAlarm",
                Response.Listener { response ->
                },
                Response.ErrorListener {
                    println(it)
                }) {
            override fun getBodyContentType(): String {
                return "application/x-www-form-urlencoded; charset=UTF-8"
            }
            override fun getParams(): MutableMap<String, String> {
                var map = HashMap<String, String>()
                map.put("sms", str)
                return map
            }
        }
        queue.add(request)
    }
}