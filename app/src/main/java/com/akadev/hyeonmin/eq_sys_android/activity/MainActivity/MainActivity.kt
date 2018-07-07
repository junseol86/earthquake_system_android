package com.akadev.hyeonmin.eq_sys_android.activity.MainActivity

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import com.akadev.hyeonmin.eq_sys_android.R
import com.akadev.hyeonmin.eq_sys_android.activity.MainActivity.Chat.ChatManager
import com.akadev.hyeonmin.eq_sys_android.activity.extension.ACFuncs
import com.akadev.hyeonmin.eq_sys_android.activity.extension.ActivityCommon
import com.akadev.hyeonmin.eq_sys_android.firebase.BrCstReceiver
import com.akadev.hyeonmin.eq_sys_android.util.Singleton
import com.akadev.hyeonmin.eq_sys_android.volley.Earthquake
import com.akadev.hyeonmin.eq_sys_android.volley.FcmToken
import com.akadev.hyeonmin.eq_sys_android.volley.Structure
import com.google.firebase.iid.FirebaseInstanceId
import com.nhn.android.maps.NMapActivity
import java.util.*

class MainActivity : NMapActivity() {

    var ac: ActivityCommon? = null
    var tb: TopBar? = null
    var bb: BottomBar? = null
    var nm: NaverMap? = null

    var fcmTokenVly: FcmToken? = null
    var chatMng: ChatManager? = null

    var earthquakeVly: Earthquake? = null

    var structureVly: Structure? = null
    var structures: ArrayList<Map<String, String>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ac = ActivityCommon(this, object: ACFuncs {})

        fcmTokenVly = FcmToken(this)
        fcmTokenSend()

        tb = TopBar(this)
        tb?.setTeamAndName()
        bb = BottomBar(this)

        earthquakeVly = Earthquake(this)
        structureVly = Structure(this)

        chatMng = ChatManager(this)

        val intFtr = IntentFilter()
        intFtr.addAction("EqSystem")
        registerReceiver(BrCstReceiver(), intFtr)

        nm = NaverMap(this)
    }

    fun earthquakeGetList() {
        earthquakeVly?.getList()
    }
    fun earthquakeGetListResult(eqList: List<Map<String, String>>) {
        Singleton.earthquakeGetListResult(eqList)
        tb?.setSituation()

        nm!!.drawEqCircle()
        structureGetList()
    }

    fun structureGetList() {
        structureVly?.getList()
    }
    fun structureGetListResult(str: ArrayList<Map<String, String>>) {
        structures = str
        Collections.reverse(structures)
        nm?.showStructures()
    }

    fun fcmTokenSend() {
        var token = FirebaseInstanceId.getInstance().token
        if (token != null) {
            fcmTokenVly?.setFcmToken(token)
        } else {
            Handler().postDelayed(Runnable {
                fcmTokenSend()
            }, 3000)
        }
    }

    override fun onBackPressed() {
        if (chatMng!!.offChatIfOn()) {
            return
        }

        super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()

        Singleton.activityOn = true
//        화면이 꺼지거나 했다가 다시 켜진 경우 대화 등을 최신으로
        if (chatMng != null && !chatMng!!.chatList.isEmpty()) {
            chatMng!!.chatVly.getListAfter()
        }
    }

    override fun onPause() {
        super.onPause()
        Singleton.activityOn = false
    }

}
