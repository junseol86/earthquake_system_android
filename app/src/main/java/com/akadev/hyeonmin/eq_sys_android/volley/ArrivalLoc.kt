package com.akadev.hyeonmin.eq_sys_android.volley

import android.app.AlertDialog
import com.akadev.hyeonmin.eq_sys_android.activity.MainActivity.Report
import com.akadev.hyeonmin.eq_sys_android.util.Const
import com.akadev.hyeonmin.eq_sys_android.util.Singleton
import com.akadev.hyeonmin.eq_sys_android.volley.extension.ErrorDialogListener
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class ArrivalLoc(val rpt: Report) {
    private val queue = Volley.newRequestQueue(rpt.atvt)!!

    fun arrivalReport(arrival: String) {
        val request: StringRequest = object : StringRequest(Request.Method.POST,
                Const.apiUrl + "member/arrivalReport",
                Response.Listener { response ->
                    Singleton.refreshJwtToken(response)
                    rpt.arrivalReportResult()
                },
                ErrorDialogListener(rpt.atvt.ac!!)) {

            override fun getBodyContentType(): String {
                return "application/x-www-form-urlencoded; charset=UTF-8"
            }

            override fun getParams(): MutableMap<String, String> {
                var map = HashMap<String, String>()
                map["jwtToken"] = Singleton.jwtToken
                map["mbr_idx"] = Singleton.memberInfo!!["mbr_idx"]!!
                map["arrival"] = arrival
                return map
            }
        }
        queue.add(request)
    }

    fun locationReport(latitude: Double, longitude: Double) {
        val request: StringRequest = object : StringRequest(Request.Method.POST,
                Const.apiUrl + "member/locationReport",
                Response.Listener { response ->
                    Singleton.refreshJwtToken(response)
                },
                ErrorDialogListener(rpt.atvt.ac!!)) {

            override fun getBodyContentType(): String {
                return "application/x-www-form-urlencoded; charset=UTF-8"
            }

            override fun getParams(): MutableMap<String, String> {
                var map = HashMap<String, String>()
                map["jwtToken"] = Singleton.jwtToken
                map["mbr_idx"] = Singleton.memberInfo!!["mbr_idx"]!!
                map["latitude"] = latitude.toString()
                map["longitude"] = longitude.toString()
                return map
            }
        }
        queue.add(request)
    }
}