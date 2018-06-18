package com.akadev.hyeonmin.eq_sys_android.volley

import com.akadev.hyeonmin.eq_sys_android.activity.MapActivity
import com.akadev.hyeonmin.eq_sys_android.util.Const
import com.akadev.hyeonmin.eq_sys_android.util.Tool
import com.akadev.hyeonmin.eq_sys_android.volley.extension.ErrorDialogListener
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class Structure(val activity: MapActivity) {
    private val queue = Volley.newRequestQueue(activity)!!

    fun getList() {
        val loginRequest: StringRequest = object: StringRequest(Request.Method.GET,
                Const.apiUrl + "structure/getList",
                Response.Listener { response ->
                    activity.structureGetListResult(Tool.jaToAL(JSONArray(response)))
                },
                ErrorDialogListener(activity)) {

            override fun getBodyContentType(): String {
                return "application/x-www-form-urlencoded; charset=UTF-8"
            }

        }
        queue.add(loginRequest)
    }

}