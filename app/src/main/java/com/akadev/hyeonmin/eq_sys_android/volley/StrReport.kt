package com.akadev.hyeonmin.eq_sys_android.volley

import com.akadev.hyeonmin.eq_sys_android.activity.MainActivity.StructureReport
import com.akadev.hyeonmin.eq_sys_android.util.Const
import com.akadev.hyeonmin.eq_sys_android.util.Singleton
import com.akadev.hyeonmin.eq_sys_android.volley.extension.ErrorDialogListener
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class StrReport(val sr: StructureReport) {
    private val queue = Volley.newRequestQueue(sr.atvt)!!

    fun sendReport(report: String, idx: String) {
        val request: StringRequest = object: StringRequest(Request.Method.PUT,
                Const.apiUrl + "structure/report",
                Response.Listener { response ->
                    Singleton.refreshJwtToken(response)
                    sr.reportResult()
                },
                ErrorDialogListener(sr.atvt.ac!!)) {

            override fun getBodyContentType(): String {
                return "application/x-www-form-urlencoded; charset=UTF-8"
            }
            override fun getParams(): MutableMap<String, String> {
                var map = HashMap<String, String>()
                map.put("jwtToken", Singleton.jwtToken)
                map.put("str_idx", idx)
                map.put("str_report", report)
                return map
            }
        }
        queue.add(request)
    }
}