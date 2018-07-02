package com.akadev.hyeonmin.eq_sys_android.activity.MainActivity.Chat

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.akadev.hyeonmin.eq_sys_android.R

class ChatAdapter(val chats: ArrayList<Map<String, String>>): RecyclerView.Adapter<ChatViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val chatCl = LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false) as ConstraintLayout
        return ChatViewHolder(chatCl)
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.chatDateTime.text = chats[position]["cht_sent"]
        holder.chatText.text = chats[position]["cht_text"]
    }
}