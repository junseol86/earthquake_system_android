package com.akadev.hyeonmin.eq_sys_android.activity.MainActivity.Chat

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.akadev.hyeonmin.eq_sys_android.R

class ChatViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    val chatItemCl: ConstraintLayout = itemView!!.findViewById(R.id.chat_item)
    val chatFromTo: TextView = itemView!!.findViewById(R.id.chat_fromto)
    val chatDateTime: TextView = itemView!!.findViewById(R.id.chat_datetime)
    val chatText: TextView = itemView!!.findViewById(R.id.chat_text)
}