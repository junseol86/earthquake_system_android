package com.akadev.hyeonmin.eq_sys_android.activity.MainActivity

import android.app.AlertDialog
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.akadev.hyeonmin.eq_sys_android.R
import com.akadev.hyeonmin.eq_sys_android.volley.StrReport
import com.nhn.android.maps.overlay.NMapPOIitem

class StructureReport(val atvt: MainActivity) {

    var strReportVly: StrReport? = null
    var idxToSend: String = ""

    val strRptPopup = atvt.findViewById(R.id.strRpt) as ConstraintLayout
    val strRptTitle = atvt.findViewById(R.id.strRptTitle) as TextView
    val strRptSpecTv = atvt.findViewById(R.id.strRptSpecTv) as TextView
    val strRptSpecEt = atvt.findViewById(R.id.strRptEt) as EditText

    val strRptOk = atvt.findViewById(R.id.strRptOk) as Button
    val strRptCancel = atvt.findViewById(R.id.strRptCancel) as Button

    init {
        strReportVly = StrReport(this)

        strRptOk.setOnClickListener {
            strReportVly?.sendReport(strRptSpecEt.text.toString(), idxToSend)
            strRptPopup.visibility = View.GONE
        }

        strRptCancel.setOnClickListener {
            strRptPopup.visibility = View.GONE
        }
    }

    fun onClickStrBalloon (pi: NMapPOIitem) {
        showStrRptPopup(pi.tag as Map<String, String>)
    }

    fun showStrRptPopup (str: Map<String, String>) {
        idxToSend = str["str_idx"] as String
        strRptSpecEt.setText("")
        strRptTitle.text = str["str_name"] as String
        strRptSpecTv.text =
                "${str["str_branch"] as String} ${str["str_line"] as String}\n${str["str_spec"] as String}"
        strRptPopup.visibility = View.VISIBLE
    }

    fun reportResult() {
        AlertDialog.Builder(atvt)
                .setMessage("보고되었습니다.")
                .show()
    }
}