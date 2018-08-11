package com.akadev.hyeonmin.eq_sys_android.activity.MainActivity

import android.app.AlertDialog
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.akadev.hyeonmin.eq_sys_android.R
import com.akadev.hyeonmin.eq_sys_android.volley.ArrivalLoc

class Report(val atvt: MainActivity) {

    val al = ArrivalLoc(this)
    val reportCl = atvt.findViewById(R.id.report_popup) as ConstraintLayout
    val reportEt = atvt.findViewById(R.id.report_et) as EditText
    val reportOkBtn = atvt.findViewById(R.id.report_ok) as Button
    val reportRejectBtn = atvt.findViewById(R.id.report_reject) as Button
    val reportCancelBtn = atvt.findViewById(R.id.report_cancel) as Button

    init {
        reportCancelBtn.setOnClickListener {
            setReportPopup(false)
            atvt.mp?.menuPuCl?.visibility = View.GONE
        }
        reportOkBtn.setOnClickListener {
            val arrival = reportEt.text.toString()
            if (arrival.isNotEmpty() && arrival.toInt() > 0) {
                al.arrivalReport(arrival)
                atvt.mp?.menuPuCl?.visibility = View.GONE
            }
        }
        reportRejectBtn.setOnClickListener {
            al.arrivalReport("-1")
            atvt.mp?.menuPuCl?.visibility = View.GONE
        }
    }

    fun isReportPopupOn() = reportCl.visibility == View.VISIBLE
    fun setReportPopup(onOff: Boolean) {
        reportCl.visibility = if (onOff) View.VISIBLE else View.GONE
        if (onOff) {
            reportEt.setText("")
        }
    }

    fun arrivalReportResult() {
        AlertDialog.Builder(atvt)
                .setTitle("전송 완료")
                .setMessage("예상 도착 소요시간이 보고되었습니다.")
                .show()
        setReportPopup(false)
    }

}