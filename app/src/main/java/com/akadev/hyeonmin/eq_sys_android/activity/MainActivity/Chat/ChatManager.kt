package com.akadev.hyeonmin.eq_sys_android.activity.MainActivity.Chat

import android.widget.TextView
import com.akadev.hyeonmin.eq_sys_android.R
import com.akadev.hyeonmin.eq_sys_android.activity.MainActivity.MainActivity
import com.akadev.hyeonmin.eq_sys_android.util.Singleton
import com.akadev.hyeonmin.eq_sys_android.volley.Chat

class ChatManager(val activity: MainActivity) {

    val chatVly = Chat(activity)
    var chatTV: TextView? = null

    init {
        chatTV = activity.findViewById(R.id.chat)
        chatVly.getList()
    }

    fun chatGetListResult(chtList: List<Map<String, String>>) {
        if (chtList.isEmpty()) {
            return
        }
        chatTV?.text = chtList[0]["cht_text"]
    }

}