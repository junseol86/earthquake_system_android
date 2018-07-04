package com.akadev.hyeonmin.eq_sys_android.volley

import com.akadev.hyeonmin.eq_sys_android.activity.MainActivity.Chat.ChatManager
import com.akadev.hyeonmin.eq_sys_android.util.Const
import com.akadev.hyeonmin.eq_sys_android.util.Singleton
import com.akadev.hyeonmin.eq_sys_android.util.Tool
import com.akadev.hyeonmin.eq_sys_android.volley.extension.ErrorDialogListener
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class Chat(val chatMng: ChatManager) {
    private val queue = Volley.newRequestQueue(chatMng.activity)!!

    fun getListBefore() {
        val firstDown = chatMng.chatList.size == 0
        val request: StringRequest = object: StringRequest(Request.Method.GET,
                Const.apiUrl + "chat/getMbrBefore/" + if (firstDown) "0" else chatMng.chatList.first()["cht_idx"],
                Response.Listener { response ->
                    chatMng.chatGetListBeforeResult(Tool.jaToAL(JSONArray(response)), firstDown)
                },
                ErrorDialogListener(chatMng.activity.ac!!)) {

            override fun getBodyContentType(): String {
                return "application/x-www-form-urlencoded; charset=UTF-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                var map = HashMap<String, String>()
                map.put("cht_to_team", Singleton.memberInfo!!["mbr_team"]!!)
                map.put("mbr_idx", Singleton.memberInfo!!["mbr_idx"]!!)
                return map
            }

        }
        queue.add(request)
    }

    fun getListAfter() {
        val request: StringRequest = object: StringRequest(Request.Method.GET,
                Const.apiUrl + "chat/getMbrAfter/" + chatMng.chatList.last()["cht_idx"],
                Response.Listener { response ->
                    chatMng.chatGetListAfterResult(Tool.jaToAL(JSONArray(response)))
                },
                ErrorDialogListener(chatMng.activity.ac!!)) {

            override fun getBodyContentType(): String {
                return "application/x-www-form-urlencoded; charset=UTF-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                var map = HashMap<String, String>()
                map.put("cht_to_team", Singleton.memberInfo!!["mbr_team"]!!)
                map.put("mbr_idx", Singleton.memberInfo!!["mbr_idx"]!!)
                return map
            }

        }
        queue.add(request)
    }

    fun sendChat(text: String) {
        val request: StringRequest = object: StringRequest(Request.Method.POST,
                Const.apiUrl + "chat/insertMbr",
                Response.Listener { response ->
                    Singleton.refreshJwtToken(response)
                    chatMng.sendChatResult()
                },
                ErrorDialogListener(chatMng.activity.ac!!)) {

            override fun getBodyContentType(): String {
                return "application/x-www-form-urlencoded; charset=UTF-8"
            }
            override fun getParams(): MutableMap<String, String> {
                var map = HashMap<String, String>()
                map.put("jwtToken", Singleton.jwtToken)
                map.put("cht_from_idx", Singleton.memberInfo!!["mbr_idx"]!!)
                map.put("cht_from_name", Singleton.memberInfo!!["mbr_name"]!!)
                map.put("cht_text", text)
                return map
            }
        }
        queue.add(request)
    }
}