package com.akadev.hyeonmin.eq_sys_android.volley

import com.akadev.hyeonmin.eq_sys_android.activity.RegisterActivity
import com.akadev.hyeonmin.eq_sys_android.util.Const
import com.akadev.hyeonmin.eq_sys_android.volley.extension.ErrorDialogListener
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class Register(val activity: RegisterActivity) {
    private val queue = Volley.newRequestQueue(activity)!!

    fun register(params: MutableMap<String, String>) {
        val registerRequest: StringRequest = object: StringRequest(Request.Method.POST,
                Const.apiUrl + "member/register",
                Response.Listener { response ->
                    if (JSONObject(response)["result"] == "SUCCESS") {
                        activity.cache?.id = params["mbr_id"] as String
                        activity.cache?.pw = params["password"] as String
                        activity.setResult(activity.ResCd_Success)
                        activity.finish()
                    }
                },
                ErrorDialogListener(activity)) {

            override fun getBodyContentType(): String {
                return "application/x-www-form-urlencoded; charset=UTF-8"
            }

            override fun getParams(): MutableMap<String, String> {
                return params
            }
        }
        queue.add(registerRequest)
    }
}