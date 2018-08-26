package com.akadev.hyeonmin.eq_sys_android.activity.MainActivity

import android.content.Intent
import android.net.Uri
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.ImageView
import com.akadev.hyeonmin.eq_sys_android.R

class MenuPopup(val atvt: MainActivity) {

    val menuPuCl = atvt.findViewById(R.id.menu_popup) as ConstraintLayout
    val menuPuBtn = atvt.findViewById(R.id.menu_pu_btn) as ImageView
    val menuStrp = atvt.findViewById(R.id.menu_strp) as ConstraintLayout
    val menuRep = atvt.findViewById(R.id.menu_rep) as ConstraintLayout
    val menuCht = atvt.findViewById(R.id.menu_cht) as ConstraintLayout
    val menuMem = atvt.findViewById(R.id.menu_mem) as ConstraintLayout
    val menuStr = atvt.findViewById(R.id.menu_str) as ConstraintLayout
    val menuCheck = atvt.findViewById(R.id.menu_check) as ConstraintLayout

    init {
        menuPuCl.setOnClickListener {
            menuPuCl.visibility = View.GONE
        }

        menuPuBtn.setOnClickListener {
            menuPuCl.visibility = View.VISIBLE
        }

        menuStrp.setOnClickListener {
            atvt.tb?.showStructuresDialog()
            menuPuCl.visibility = View.GONE
        }

        menuRep.setOnClickListener {
            atvt.rt?.setReportPopup(true)
            menuPuCl.visibility = View.GONE
        }

        menuCht.setOnClickListener {
            atvt.chatMng?.chatCl?.visibility = View.VISIBLE
            menuPuCl.visibility = View.GONE
        }

        menuMem.setOnClickListener {
            atvt.tb?.showMembersDialog()
            menuPuCl.visibility = View.GONE
        }

        menuStr.setOnClickListener {
            atvt.bb?.seeStrOnTeam()
            menuPuCl.visibility = View.GONE
        }

        menuCheck.setOnClickListener {
            val url = "http://35.229.252.63:8081/guide.html"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            atvt.startActivity(intent)
            menuPuCl.visibility = View.GONE
        }
    }
}