package com.akadev.hyeonmin.eq_sys_android.activity.MainActivity.Chat

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.akadev.hyeonmin.eq_sys_android.R

class ChatViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    val chatItemCl: ConstraintLayout = itemView!!.findViewById(R.id.chat_item)
    val chatHdrLeft: TextView = itemView!!.findViewById(R.id.chat_hdr_left)
    val chatHdrRight: TextView = itemView!!.findViewById(R.id.chat_hdr_right)
    val chatText: TextView = itemView!!.findViewById(R.id.chat_text)
}