package com.akadev.hyeonmin.eq_sys_android.volley

import com.akadev.hyeonmin.eq_sys_android.activity.MainActivity.MainActivity
import com.akadev.hyeonmin.eq_sys_android.util.Const
import com.akadev.hyeonmin.eq_sys_android.util.Tool
import com.akadev.hyeonmin.eq_sys_android.volley.extension.ErrorDialogListener
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class Member(val atvt: MainActivity) {
    private val queue = Volley.newRequestQueue(atvt)!!

    fun getList() {
        val loginRequest: StringRequest = object: StringRequest(Request.Method.GET,
                Const.apiUrl + "member/getList",
                Response.Listener { response ->
                    atvt.memberGetListResult(Tool.jaToAL(JSONArray(response)))
                },
                ErrorDialogListener(atvt.ac!!)) {

            override fun getBodyContentType(): String {
                return "application/x-www-form-urlencoded; charset=UTF-8"
            }

        }
        queue.add(loginRequest)
    }

}
