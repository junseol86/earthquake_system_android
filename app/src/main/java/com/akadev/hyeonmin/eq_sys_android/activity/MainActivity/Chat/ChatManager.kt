package com.akadev.hyeonmin.eq_sys_android.activity.MainActivity.Chat

import android.graphics.Color
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.akadev.hyeonmin.eq_sys_android.R
import com.akadev.hyeonmin.eq_sys_android.activity.MainActivity.MainActivity
import com.akadev.hyeonmin.eq_sys_android.util.Singleton
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

    val chatTV: TextView = activity.findViewById(R.id.chat)

    val chatTextET: EditText = activity.findViewById(R.id.chat_et)
    val chatSendBtn: Button = activity.findViewById(R.id.chat_send)

    val chatFilterAry = arrayOf(
            arrayOf(
                    activity.findViewById(R.id.chat_filter_hq) as ConstraintLayout,
                    activity.findViewById(R.id.chat_filter_hq_txt) as TextView
                    ),
            arrayOf(
                    activity.findViewById(R.id.chat_filter_all) as ConstraintLayout,
                    activity.findViewById(R.id.chat_filter_all_txt) as TextView

            ),
            arrayOf(
                    activity.findViewById(R.id.chat_filter_team) as ConstraintLayout,
                    activity.findViewById(R.id.chat_filter_team_txt) as TextView
            )
    )
//    0: 상황실에게, 1: 전체에게, 2: 같은 조에게
    var chatFilter = 0

    init {
        seeChatBtn.setOnClickListener {
            mapCl.visibility = View.GONE
            chatCl.visibility = View.VISIBLE
        }

        visChatFilter()
        for (i in 0 until chatFilterAry.size) {
            chatFilterAry[i][0].setOnClickListener {
                chatFilter = i
                visChatFilter()
            }
        }

        chatRv.layoutManager = LinearLayoutManager(activity)
        chatRv.adapter = chatAdt

//        chatRv.scrollToPosition(chatList.size - 1)

        chatSendBtn.setOnClickListener {
            if (chatTextET.text.toString().trim().isNotEmpty()) {
                chatVly.sendChat(chatTextET.text.toString())
            }
        }

//        키보드가 나타나 사이즈가 변경되었을 시 맨 아래로 스크롤
        chatRv.addOnLayoutChangeListener { _, _, top, _, bottom, _, oldTop, _, oldBottom ->
            if (bottom - top < oldBottom - oldTop) {
                Handler().postDelayed(Runnable {
                    chatRv.scrollToPosition(chatList.size - 1)
                }, 100)
            }
        }

        chatRv.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if ((chatRv.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() < 3) {
                    if (!Singleton.getChatsBeforeLocked) {
                        Singleton.getChatsBeforeLocked = true
                        chatVly.getListBefore()
                    }

                }
            }
        })

        chatVly.getListBefore()
    }

    fun visChatFilter() {
        for (i in 0 until chatFilterAry.size) {
            if (i == chatFilter) {
                chatFilterAry[i][0].setBackgroundColor(Color.parseColor("#FFFFFF"))
                (chatFilterAry[i][1] as TextView).setTextColor(Color.parseColor("#000000"))

            } else {
                chatFilterAry[i][0].setBackgroundColor(Color.parseColor("#888888"))
                (chatFilterAry[i][1] as TextView).setTextColor(Color.parseColor("#FFFFFF"))
            }
        }
        val teamNo = Singleton.memberInfo!!["mbr_team"]
        (chatFilterAry[2][1] as TextView).text = "→ ${if (teamNo == "0") "미편성" else (teamNo + "조")}"
    }

    fun chatGetListBeforeResult(downList: List<Map<String, String>>, scrollToBottom: Boolean) {
        if (downList.isEmpty()) {
            return
        }
        Collections.reverse(downList)
        chatList.addAll(0, downList)
        chatRv.adapter.notifyDataSetChanged()

        chatRv.scrollToPosition(if (scrollToBottom) chatList.size -1 else downList.size + 4)
        chatTV.text = chatList.last()["cht_text"]
        Singleton.getChatsBeforeLocked = false
    }

    fun chatGetListAfterResult(downList: List<Map<String, String>>) {
        if (downList.isEmpty()) {
            return
        }
        Collections.reverse(downList)
        downList.map {
            if (chatList.last()["cht_idx"]!!.toInt() < it["cht_idx"]!!.toInt()) {
                chatList.add(it)
            }
        }

        chatRv.scrollToPosition(chatList.size - 1)
        chatTV.text = chatList.last()["cht_text"]
    }

    fun offChatIfOn(): Boolean {
        return if (chatCl.visibility == View.VISIBLE) {
            mapCl.visibility = View.VISIBLE
            chatCl.visibility = View.GONE
            true
        } else {
            false
        }
    }

    fun sendChatResult() {
        chatVly.getListAfter()
        chatTextET.setText("")
    }
}