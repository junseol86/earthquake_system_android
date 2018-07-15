package com.akadev.hyeonmin.eq_sys_android.activity.MainActivity

import android.app.AlertDialog
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.Build
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
import com.akadev.hyeonmin.eq_sys_android.volley.Member
import com.akadev.hyeonmin.eq_sys_android.volley.Structure
import com.google.firebase.iid.FirebaseInstanceId
import com.nhn.android.maps.NMapActivity
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.ArrayList

class MainActivity : NMapActivity() {

    var ac: ActivityCommon? = null
    var rt: Report? = null
    var tb: TopBar? = null
    var bb: BottomBar? = null
    var nm: NaverMap? = null
    var ml: MyLocation? = null

    var fcmTokenVly: FcmToken? = null
    var chatMng: ChatManager? = null

    var earthquakeVly: Earthquake? = null

    var structureVly: Structure? = null
    var structures: ArrayList<Map<String, String>>? = null

    var memberVly: Member? = null
    var members: ArrayList<Map<String, String>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ac = ActivityCommon(this, object: ACFuncs {})
        ac?.askCallPermission()

        fcmTokenVly = FcmToken(this)
        fcmTokenSend()

        rt = Report(this)
        tb = TopBar(this)
        tb?.setTeamAndName()
        bb = BottomBar(this)
        ml = MyLocation(this)
        ml?.getLocWithPermissionCheck()

        earthquakeVly = Earthquake(this)
        structureVly = Structure(this)
        memberVly = Member(this)
        memberGetList()

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

    fun memberGetList() {
        memberVly?.getList()
    }

    fun memberGetListResult(mbr: ArrayList<Map<String, String>>) {
        members = mbr
        mbr.sortWith(Comparator { o1, o2 -> (o1!!["mbr_team"]!!.compareTo(o2!!["mbr_team"]!!)) })
        var list1 = ArrayList<Map<String, String>>()
        var list2 = ArrayList<Map<String, String>>()
        mbr.map {
            if (it["mbr_team"]!! == Singleton.memberInfo!!["mbr_team"]) {
                list1.add(it)
            } else {
                list2.add(it)
            }
        }
        members = ArrayList()
        members?.addAll(list1)
        members?.addAll(list2)
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
        if (requestCode == ml!!.REQ_CODE) {
            if (permissions != null && grantResults != null) {
                (0 until permissions.size).map {
                    if (permissions[it] == android.Manifest.permission.ACCESS_FINE_LOCATION && grantResults[it] == PackageManager.PERMISSION_GRANTED) {
                        ml?.getLocWithPermissionCheck()
                    }
                }
            }
        }
    }

}
