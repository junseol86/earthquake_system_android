package com.akadev.hyeonmin.eq_sys_android.activity.MainActivity

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.widget.TextView
import com.akadev.hyeonmin.eq_sys_android.R
import com.akadev.hyeonmin.eq_sys_android.activity.LoginActivity
import com.akadev.hyeonmin.eq_sys_android.util.Singleton
import com.nhn.android.maps.maplib.NGeoPoint

class TopBar(var atvt: MainActivity) {

    val situation: TextView = atvt.findViewById(R.id.situation)
    val sitBtn: ConstraintLayout = atvt.findViewById(R.id.sit_btn)
    val myStatBtn: ConstraintLayout = atvt.findViewById(R.id.my_stat_btn)
    val teamAndName: TextView = atvt.findViewById(R.id.team_and_name)

    init {
        sitBtn.setOnClickListener {
            atvt.earthquakeGetList()
        }

        myStatBtn.setOnClickListener {
            AlertDialog.Builder(atvt)
                    .setTitle("메뉴")
                    .setItems(arrayOf("도착 소요시간 또는 응소불가 전송", "구조물 보고", "직원들 목록/전화", "로그아웃")) { _, i ->
                        when (i) {
                            0 -> atvt.rt?.setReportPopup(true)
                            1 -> showMembersDialog()
                            2 -> logout()
                        }
                    }
                    .show()
        }
    }

    fun logout() {
        AlertDialog.Builder(atvt)
                .setTitle("로그아웃")
                .setMessage("로그아웃하시겠습니까?")
                .setPositiveButton("확인") { _, _ ->
                    atvt.fcmTokenVly?.setFcmToken("")
                    atvt.ac?.caches?.id = ""
                    atvt.ac?.caches?.pw = ""
                    Singleton.memberInfo = null
                    Singleton.jwtToken = ""
                    atvt.startActivity(Intent(atvt, LoginActivity::class.java))
                    atvt.finish()
                }
                .setNegativeButton("취소") { _, _ ->
                }
                .show()
    }

    fun setSituation() {
        var sitStr = ""
        if (Singleton.earthquakeInfo == null) {
            sitStr = "상황없음"
        } else {
            val eqInf = Singleton.earthquakeInfo!!
            sitStr += "" + if (eqInf["eq_type"] == "inland") "내륙 " else "해역 "
            sitStr += eqInf["eq_strength"] + " - "
            sitStr += when (eqInf["eq_level"]) {
                "0" -> "자체대응"
                "1" -> "대응 1단계"
                else -> "대응 2단계"
            }
            atvt.rt?.setReportPopup(true)
        }
        situation.text = sitStr
    }

    fun setTeamAndName () {
        val team = Singleton.memberInfo!!["mbr_team"]
        val teamStr = if (team == "0") "미배정 - " else "${team}조 - "
        teamAndName.text = teamStr + Singleton.memberInfo!!["mbr_name"]
    }

    fun showStructuresDialog () {
        if (atvt.structures != null) {
            val strAry = Array(atvt.structures!!.size) {
                (if (atvt.structures!![it]["str_need_check"] as String == "1") "[점검요] " else "") + (atvt.structures!![it]["str_name"] as String)
            }
            AlertDialog.Builder(atvt)
                    .setTitle("구조물 목록")
                    .setItems(strAry) { _, i ->
                        atvt.nm?.mapController?.zoomLevel = 14
                        atvt.nm?.mapController?.mapCenter =
                                NGeoPoint(atvt.structures!![i]["longitude"]!!.toDouble(), atvt.structures!![i]["latitude"]!!.toDouble())
                        atvt.sr?.showStrRptPopup(atvt.structures!![i])
                    }
                    .show()
        }

    }

    fun showMembersDialog () {
        if (atvt.members != null) {
            val mbrAry = Array(atvt.members!!.size) {
                "[" + (if (atvt.members!![it]["mbr_team"] as String == "0") "미편성] " else  (atvt.members!![it]["mbr_team"] as String+ "조] ")) + (atvt.members!![it]["mbr_name"] as String)
            }
            val telAry = Array(atvt.members!!.size) {
                atvt.members!![it]["mbr_phone"]
            }

            AlertDialog.Builder(atvt)
                    .setTitle("직원 목록 - 선택시 전화연결")
                    .setItems(mbrAry) { _, i ->
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:${telAry[i]}")
                        if (ContextCompat.checkSelfPermission(atvt, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                            atvt.startActivity(intent)
                        } else {
                            atvt.ac?.askCallPermission()
                        }
                    }
                    .show()
        }
    }

}