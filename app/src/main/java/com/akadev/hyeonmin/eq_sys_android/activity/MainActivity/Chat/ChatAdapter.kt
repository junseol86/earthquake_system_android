package com.akadev.hyeonmin.eq_sys_android.activity.MainActivity.Chat

import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akadev.hyeonmin.eq_sys_android.R
import com.akadev.hyeonmin.eq_sys_android.util.Singleton
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter(val chats: ArrayList<Map<String, String>>): RecyclerView.Adapter<ChatViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val chatCl = LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false) as ConstraintLayout
        return ChatViewHolder(chatCl)
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {

        var sent = ""
        try {
            var sentString = chats[position]["cht_sent"]
            var dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            var dateTime = dateFormat.parse(sentString)
            val sentCal = Calendar.getInstance()
            sentCal.time = dateTime
            val nowCal = Calendar.getInstance()

            val todayDate = nowCal.get(Calendar.YEAR).toString() + "-" + (nowCal.get(Calendar.MONTH) + 1) + "-" + nowCal.get(Calendar.DATE)
            var sentDate = sentCal.get(Calendar.YEAR).toString() + "-" + (sentCal.get(Calendar.MONTH) + 1) + "-" + sentCal.get(Calendar.DATE)

            if (todayDate == sentDate) {
                sentDate = "오늘"
            }

            sentDate += " " + sentCal.get(Calendar.HOUR).toString() + ":" + sentCal.get(Calendar.MINUTE) + "." + sentCal.get(Calendar.SECOND)
            sent = sentDate

        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (chats[position]["cht_from_idx"] == Singleton.memberInfo!!["mbr_idx"]) {
            holder.chatItemCl.setBackgroundColor(if (position % 2 == 0) Color.rgb(82, 137, 233) else Color.rgb(82, 124, 233))
            holder.chatText.gravity = Gravity.RIGHT
            holder.chatHdrLeft.text = sent
            holder.chatHdrRight.text = Singleton.memberInfo!!["mbr_name"]
        } else {
            val to: String = when (chats[position]["cht_to"]) {
                "0" -> "전원"
                Singleton.memberInfo!!["mbr_idx"] -> Singleton.memberInfo!!["mbr_name"]!!
                else -> chats[position]["cht_to_team"] + "조"
            }
            holder.chatHdrLeft.text = "→ $to"

            holder.chatItemCl.setBackgroundColor(if (position % 2 == 0) Color.rgb(42, 42, 42) else Color.rgb(34, 34, 34))
            holder.chatHdrRight.text = sent
        }
        holder.chatText.text = chats[position]["cht_text"]
    }
}