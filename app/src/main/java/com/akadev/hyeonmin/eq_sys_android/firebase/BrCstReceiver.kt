package com.akadev.hyeonmin.eq_sys_android.firebase

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.akadev.hyeonmin.eq_sys_android.activity.MainActivity.MainActivity

class BrCstReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val ma = context as MainActivity
        val type = intent!!.extras!!["type"]
        val title = intent!!.extras!!["title"]
        val body = intent!!.extras!!["body"]

        if (type == "chat") {
            ma.chatMng!!.chatVly.getListAfter()

        } else if (type == "team") {
            ma.memberVly!!.getList()
            AlertDialog.Builder(ma)
                    .setTitle(title.toString())
                    .setMessage(body.toString())
                    .show()
        }
    }
}