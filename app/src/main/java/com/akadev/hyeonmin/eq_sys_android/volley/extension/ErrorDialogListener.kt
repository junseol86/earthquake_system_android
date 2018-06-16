package com.akadev.hyeonmin.eq_sys_android.volley.extension

import android.app.Activity
import android.app.AlertDialog
import com.akadev.hyeonmin.eq_sys_android.activity.extension.MyCustActivity
import com.android.volley.Response
import com.android.volley.VolleyError
import org.json.JSONObject
import java.io.UnsupportedEncodingException


class ErrorDialogListener(val activity: MyCustActivity): Response.ErrorListener {
    override fun onErrorResponse(error: VolleyError?) {
        if (error!!.networkResponse.data != null) {
            try {
                var body = String(error.networkResponse.data, Charsets.UTF_8)
                activity.alert(JSONObject(body)["result"] as String)
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
        }
    }

    operator fun invoke(function: () -> Int): ErrorDialogListener {
        return this
    }
}