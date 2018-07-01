package com.akadev.hyeonmin.eq_sys_android.volley.extension

import com.akadev.hyeonmin.eq_sys_android.activity.extension.ActivityCommon
import com.android.volley.Response
import com.android.volley.VolleyError
import org.json.JSONObject
import java.io.UnsupportedEncodingException


class ErrorDialogListener(val activityCommon: ActivityCommon): Response.ErrorListener {
    override fun onErrorResponse(error: VolleyError?) {
        if (error!!.networkResponse.data != null) {
            try {
                var body = String(error.networkResponse.data, Charsets.UTF_8)
                activityCommon.alert(JSONObject(body)["result"] as String)
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
        }
    }

    operator fun invoke(function: () -> Int): ErrorDialogListener {
        return this
    }
}