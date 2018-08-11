package com.akadev.hyeonmin.eq_sys_android.activity.MainActivity

import android.app.AlertDialog
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import com.akadev.hyeonmin.eq_sys_android.R
import com.akadev.hyeonmin.eq_sys_android.activity.MainActivity.Chat.ChatManager
import com.akadev.hyeonmin.eq_sys_android.activity.extension.ACFuncs
import com.akadev.hyeonmin.eq_sys_android.activity.extension.ActivityCommon
import com.akadev.hyeonmin.eq_sys_android.firebase.BrCstReceiver
import com.akadev.hyeonmin.eq_sys_android.util.Singleton
import com.akadev.hyeonmin.eq_sys_android.volley.*
import com.google.firebase.iid.FirebaseInstanceId
import com.nhn.android.maps.NMapActivity
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : NMapActivity() {

    var ac: ActivityCommon? = null
    var rt: Report? = null
    var tb: TopBar? = null
    var bb: BottomBar? = null
    var nm: NaverMap? = null
    var ml: MyLocation? = null
    var mp: MenuPopup? = null

    var fcmTokenVly: FcmToken? = null
    var chatMng: ChatManager? = null

    var earthquakeVly: Earthquake? = null

    var spotVly: Spot? = null
    var spots: ArrayList<Map<String, String>>? = null

    var structureVly: Structure? = null
    var structures: ArrayList<Map<String, String>>? = null

    var memberVly: Member? = null
    var members: ArrayList<Map<String, String>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ac = ActivityCommon(this, object: ACFuncs {})
//        ac?.askCallPermission()

        fcmTokenVly = FcmToken(this)
        fcmTokenSend()

        rt = Report(this)
        tb = TopBar(this)
        tb?.setTeamAndName()
        bb = BottomBar(this)
        ml = MyLocation(this)
        requestPermissions()

        earthquakeVly = Earthquake(this)
        structureVly = Structure(this)
        spotVly = Spot(this)
        memberVly = Member(this)

        spotGetList()

        memberGetList()

        chatMng = ChatManager(this)

        val intFtr = IntentFilter()
        intFtr.addAction("EqSystem")
        registerReceiver(BrCstReceiver(), intFtr)

        nm = NaverMap(this)
        mp = MenuPopup(this)
    }

    fun requestPermissions () {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.SYSTEM_ALERT_WINDOW,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.CALL_PHONE), 0)
        }

//        M 버전 이상일 시 알림소리 강제변경할 때 꼭 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                !((getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).isNotificationPolicyAccessGranted)) {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS))
        }
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

    fun spotGetList() {
        spotVly?.getList()
    }
    fun spotGetListResult(spt: ArrayList<Map<String, String>>) {
        spots = spt
        nm?.showSpots()
    }

    fun memberGetList() {
        memberVly?.getList()
    }

    fun memberGetListResult(mbr: ArrayList<Map<String, String>>) {
        members = mbr
        mbr.sortWith(Comparator { o1, o2 -> (o1!!["mbr_team"]!!.compareTo(o2!!["mbr_team"]!!)) })
        var list1 = ArrayList<Map<String, String>>()
        var list2 = ArrayList<Map<String, String>>()
        var list3 = ArrayList<Map<String, String>>()
        mbr.map {
            if (it["mbr_team"]!! == Singleton.memberInfo!!["mbr_team"]) {
                list1.add(it)
            } else if (it["mbr_team"]!! != "0") {
                list2.add(it)
            } else {
                list3.add(it)
            }
        }
        members = ArrayList()
        members?.addAll(list1)
        members?.addAll(list2)
        members?.addAll(list3)
        nm?.showMemberPosition()

        Handler().postDelayed( {
            memberGetList()
        }, 10000)
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

        if (mp?.menuPuCl?.visibility == View.VISIBLE) {
            mp?.menuPuCl?.visibility = View.GONE
            return
        }

        if (chatMng!!.offChatIfOn()) {
            return
        }

        if (rt!!.isReportPopupOn()) {
            rt!!.setReportPopup(false)
            return
        }

        AlertDialog.Builder(this)
                .setTitle("경주 지진알림을 종료하시겠습니까?")
                .setPositiveButton("확인") { _, _ ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        finishAndRemoveTask()
                    } else {
                        finish()
                    }
                }
                .setNegativeButton("취소") { _, _ ->}
                .show()
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?) {
        if (permissions != null && grantResults != null) {
            (0 until permissions.size).map {
                if (permissions[it] == android.Manifest.permission.ACCESS_FINE_LOCATION && grantResults[it] == PackageManager.PERMISSION_GRANTED) {
                    ml?.getLocWithPermissionCheck()
                }
            }
        }
    }

}
