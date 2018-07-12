package com.akadev.hyeonmin.eq_sys_android.volley

import com.akadev.hyeonmin.eq_sys_android.activity.MainActivity.MainActivity
import com.akadev.hyeonmin.eq_sys_android.util.Const
import com.akadev.hyeonmin.eq_sys_android.util.Singleton
import com.akadev.hyeonmin.eq_sys_android.volley.extension.ErrorDialogListener
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class FcmToken(val activity: MainActivity) {
    private val queue = Volley.newRequestQueue(activity)!!

    fun setFcmToken(fcmToken: String) {
        val loginRequest: StringRequest = object: StringRequest(Request.Method.PUT,
                Const.apiUrl + "member/setFcmToken",
                Response.Listener { response ->
                    if (fcmToken.isNotEmpty()) {
                        Singleton.refreshJwtToken(response)
                    }
                },
                ErrorDialogListener(activity.ac!!)) {

            override fun getBodyContentType(): String {
                return "application/x-www-form-urlencoded; charset=UTF-8"
            }

            override fun getParams(): MutableMap<String, String> {
                var params: MutableMap<String, String> = HashMap<String, String>()
                params["jwtToken"] = Singleton.jwtToken
                params["mbr_idx"] = Singleton.memberInfo!!["mbr_idx"]!!
                params["mbr_fcm"] = fcmToken
                return params
            }
        }
        queue.add(loginRequest)
    }
}