package com.akadev.hyeonmin.eq_sys_android.activity.MainActivity.Chat

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.akadev.hyeonmin.eq_sys_android.R
import com.akadev.hyeonmin.eq_sys_android.activity.MainActivity.MainActivity
import com.akadev.hyeonmin.eq_sys_android.util.Singleton
import com.akadev.hyeonmin.eq_sys_android.volley.Chat

class ChatManager(val activity: MainActivity) {

    val chatVly = Chat(activity)

    val mapCl: ConstraintLayout = activity.findViewById(R.id.map_cl)
    val chatCl: ConstraintLayout = activity.findViewById(R.id.chat_cl)

    var chatList: ArrayList<Map<String, String>> = ArrayList()
    val chatRv: RecyclerView = activity.findViewById(R.id.chat_rv)
    val chatAdt = ChatAdapter(chatList)

    val seeChatBtn: ConstraintLayout = activity.findViewById(R.id.see_chat_btn)

    var chatTV: TextView = activity.findViewById(R.id.chat)

    init {
        seeChatBtn.setOnClickListener {
            mapCl.visibility = View.GONE
            chatCl.visibility = View.VISIBLE
        }

        chatRv.layoutManager = LinearLayoutManager(activity)
        chatRv.adapter = chatAdt

        chatVly.getList()

    }

    fun chatGetListResult(chtList: List<Map<String, String>>) {
        if (chtList.isEmpty()) {
            return
        }
        chatList.addAll(chtList)
        chatTV?.text = chatList[0]["cht_text"]
    }

    fun offChatIfOn(): Boolean {
        if (chatCl.visibility == View.VISIBLE) {
            mapCl.visibility = View.VISIBLE
            chatCl.visibility = View.GONE
            return true
        } else {
            return false
        }
    }

}