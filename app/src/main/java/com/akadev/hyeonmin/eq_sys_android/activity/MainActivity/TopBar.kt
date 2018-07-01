package com.akadev.hyeonmin.eq_sys_android.activity.MainActivity

import android.support.constraint.ConstraintLayout
import android.widget.TextView
import com.akadev.hyeonmin.eq_sys_android.R
import com.akadev.hyeonmin.eq_sys_android.util.Singleton
import kotlinx.android.synthetic.main.activity_main.view.*

class TopBar(var atvt: MainActivity) {

    val situation: TextView = atvt.findViewById(R.id.situation)
    val sitBtn: ConstraintLayout = atvt.findViewById(R.id.sit_btn)
    val teamAndName: TextView = atvt.findViewById(R.id.team_and_name)

    init {
        sitBtn.setOnClickListener {
            atvt.earthquakeGetList()
        }
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
        }
        situation.text = sitStr
    }

    fun setTeamAndName() {
        val team = Singleton.memberInfo!!["mbr_team"]
        var teamStr = if (team == "0") "미배정 - " else "${team}조 - "
        teamAndName.text = teamStr + Singleton.memberInfo!!["mbr_name"]
    }

}