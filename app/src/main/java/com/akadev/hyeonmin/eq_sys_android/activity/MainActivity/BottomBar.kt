package com.akadev.hyeonmin.eq_sys_android.activity.MainActivity

import android.app.AlertDialog
import android.support.constraint.ConstraintLayout
import com.akadev.hyeonmin.eq_sys_android.R
import com.akadev.hyeonmin.eq_sys_android.util.Singleton
import com.nhn.android.maps.maplib.NGeoPoint
import kotlinx.android.synthetic.main.activity_main.*

class BottomBar(val atvt: MainActivity) {

    val onTeamBtn: ConstraintLayout = atvt.findViewById(R.id.on_team_btn)

    init {
        onTeamBtn.setOnClickListener {
            seeStrOnTeam()
        }
    }

    fun seeStrOnTeam () {
        if (Singleton.earthquakeInfo == null) {
            AlertDialog.Builder(atvt)
                    .setMessage("지진 발생상황이 아닙니다.")
                    .show()
        } else {
            var strOnTeam = ArrayList<Map<String, String>>()
            val myTeam = Singleton.memberInfo!!["mbr_team"]
            atvt.structures?.map {
                if (it["on_team"]!! == myTeam) {
                    strOnTeam.add(it)
                }
            }
            if (strOnTeam.size == 0) {
                AlertDialog.Builder(atvt)
                        .setMessage("할당된 구조물이 없습니다.")
                        .show()
            } else {
                var strOnTeamArr = arrayOfNulls<String>(strOnTeam.size)
                for (i in 0 until strOnTeam.size) {
                    strOnTeamArr[i] = strOnTeam[i]["str_name"]
                }

                AlertDialog.Builder(atvt)
                        .setItems(strOnTeamArr) { _, i ->
                            atvt.nm?.mapController?.zoomLevel = 14
                            atvt.nm?.mapController?.mapCenter = NGeoPoint(strOnTeam[i]["longitude"]!!.toDouble(), strOnTeam[i]["latitude"]!!.toDouble())
                        }
                        .setPositiveButton("확인") { _, _ ->
                        }
                        .show()

            }
        }
    }

}