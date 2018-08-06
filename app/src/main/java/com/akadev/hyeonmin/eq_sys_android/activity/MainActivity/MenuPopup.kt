package com.akadev.hyeonmin.eq_sys_android.activity.MainActivity

import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.ImageView
import com.akadev.hyeonmin.eq_sys_android.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MenuPopup(val atvt: MainActivity) {

    val menuPuCl = atvt.findViewById(R.id.menu_popup) as ConstraintLayout
    val menuPuBtn = atvt.findViewById(R.id.menu_pu_btn) as ImageView
    val menuSit = atvt.findViewById(R.id.menu_sit) as ConstraintLayout
    val menuRep = atvt.findViewById(R.id.menu_rep) as ConstraintLayout
    val menuCht = atvt.findViewById(R.id.menu_cht) as ConstraintLayout
    val menuMem = atvt.findViewById(R.id.menu_mem) as ConstraintLayout
    val menuStr = atvt.findViewById(R.id.menu_str) as ConstraintLayout
    val menuLgt = atvt.findViewById(R.id.menu_lgt) as ConstraintLayout

    init {
        menuPuCl.setOnClickListener {
            menuPuCl.visibility = View.GONE
        }

        menuPuBtn.setOnClickListener {
            menuPuCl.visibility = View.VISIBLE
        }

        menuSit.setOnClickListener {
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
            atvt.tb?.showMembers()
            menuPuCl.visibility = View.GONE
        }

        menuStr.setOnClickListener {
            atvt.bb?.seeStrOnTeam()
            menuPuCl.visibility = View.GONE
        }

        menuLgt.setOnClickListener {
            atvt.tb?.logout()
            menuPuCl.visibility = View.GONE
        }
    }
}