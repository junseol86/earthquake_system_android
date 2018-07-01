package com.akadev.hyeonmin.eq_sys_android.volley

import com.akadev.hyeonmin.eq_sys_android.activity.extension.ActivityCommon
import com.akadev.hyeonmin.eq_sys_android.util.Const
import com.akadev.hyeonmin.eq_sys_android.volley.extension.ErrorDialogListener
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class Login(val activityCommon: ActivityCommon) {
    private val queue = Volley.newRequestQueue(activityCommon.atvt)!!

    fun login(id: String, pw: String) {
        val loginRequest: StringRequest = object: StringRequest(Request.Method.POST,
            Const.apiUrl + "member/passwordLogin",
            Response.Listener { response ->
                activityCommon.loginResult(id, pw, response)
            },
                ErrorDialogListener(activityCommon)) {

            override fun getBodyContentType(): String {
                return "application/x-www-form-urlencoded; charset=UTF-8"
            }

            override fun getParams(): MutableMap<String, String> {
                var params: MutableMap<String, String> = HashMap<String, String>()
                params["mbr_id"] = id
                params["password"] = pw
                return params
            }
        }
        queue.add(loginRequest)
    }

}