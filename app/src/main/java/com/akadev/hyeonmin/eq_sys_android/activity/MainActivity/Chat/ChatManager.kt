package com.akadev.hyeonmin.eq_sys_android.activity.MainActivity.Chat

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.akadev.hyeonmin.eq_sys_android.R
import com.akadev.hyeonmin.eq_sys_android.activity.MainActivity.MainActivity
import com.akadev.hyeonmin.eq_sys_android.volley.Chat
import java.util.*

class ChatManager(val activity: MainActivity) {

    val chatVly = Chat(this)

    val mapCl: ConstraintLayout = activity.findViewById(R.id.map_cl)
    val chatCl: ConstraintLayout = activity.findViewById(R.id.chat_cl)

    var chatList: ArrayList<Map<String, String>> = ArrayList()
    val chatRv: RecyclerView = activity.findViewById(R.id.chat_rv)
    val chatAdt = ChatAdapter(chatList)

    val seeChatBtn: ConstraintLayout = activity.findViewById(R.id.see_chat_btn)

    var chatTV: TextView = activity.findViewById(R.id.chat)

    var chatTextET: EditText = activity.findViewById(R.id.chat_et)
    var chatSendBtn: Button = activity.findViewById(R.id.chat_send)

    init {
        seeChatBtn.setOnClickListener {
            mapCl.visibility = View.GONE
            chatCl.visibility = View.VISIBLE
        }

        chatRv.layoutManager = LinearLayoutManager(activity)
        chatRv.adapter = chatAdt

        chatSendBtn.setOnClickListener {
            chatVly.sendChat(chatTextET.text.toString())
        }

        chatVly.getListBefore()
    }

    fun chatGetListBeforeResult(chtList: List<Map<String, String>>, scrollToBottom: Boolean) {
        if (chtList.isEmpty()) {
            return
        }
        Collections.reverse(chtList)
        chatList.addAll(0, chtList)

        if (scrollToBottom) {
            chatRv.scrollToPosition(chatList.size - 1)
        }
        chatTV.text = chatList.last()["cht_text"]
    }

    fun chatGetListAfterResult(chtList: List<Map<String, String>>) {
        if (chtList.isEmpty()) {
            return
        }
        Collections.reverse(chtList)
        chatList.addAll(chtList)

        chatRv.scrollToPosition(chatList.size - 1)
        chatTV.text = chatList.last()["cht_text"]
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

    fun sendChatResult() {
        chatVly.getListAfter()
        chatTextET.setText("")
    }
}