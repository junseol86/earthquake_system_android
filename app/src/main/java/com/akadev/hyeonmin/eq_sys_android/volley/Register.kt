package com.akadev.hyeonmin.eq_sys_android.volley

import com.akadev.hyeonmin.eq_sys_android.activity.RegisterActivity
import com.akadev.hyeonmin.eq_sys_android.util.Const
import com.akadev.hyeonmin.eq_sys_android.volley.extension.ErrorDialogListener
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class Register(val atvt: RegisterActivity) {
    private val queue = Volley.newRequestQueue(atvt)!!

    fun register(params: MutableMap<String, String>) {
        val registerRequest: StringRequest = object: StringRequest(Request.Method.POST,
                Const.apiUrl + "member/register",
                Response.Listener { response ->
                    if (JSONObject(response)["result"] == "SUCCESS") {
                        atvt.ac?.caches?.id = params["mbr_id"] as String
                        atvt.ac?.caches?.pw = params["password"] as String
                        atvt.ac?.atvt?.setResult(atvt.ac!!.ResCd_Success)
                        atvt.finish()
                    }
                },
                ErrorDialogListener(atvt.ac!!)) {

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