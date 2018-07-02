package com.akadev.hyeonmin.eq_sys_android.activity.MainActivity

import android.os.Bundle
import com.akadev.hyeonmin.eq_sys_android.R
import com.akadev.hyeonmin.eq_sys_android.activity.MainActivity.Chat.ChatManager
import com.akadev.hyeonmin.eq_sys_android.activity.extension.ACFuncs
import com.akadev.hyeonmin.eq_sys_android.activity.extension.ActivityCommon
import com.akadev.hyeonmin.eq_sys_android.util.Singleton
import com.akadev.hyeonmin.eq_sys_android.volley.Earthquake
import com.akadev.hyeonmin.eq_sys_android.volley.Structure
import com.nhn.android.maps.NMapActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : NMapActivity() {

    var ac: ActivityCommon? = null
    var tb: TopBar? = null
    var bb: BottomBar? = null
    var nm: NaverMap? = null

    var chatMng: ChatManager? = null

    var earthquakeVly: Earthquake? = null

    var structureVly: Structure? = null
    var structures: ArrayList<Map<String, String>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ac = ActivityCommon(this, object: ACFuncs {})
        tb = TopBar(this)
        tb?.setTeamAndName()
        bb = BottomBar(this)

        earthquakeVly = Earthquake(this)
        structureVly = Structure(this)

        chatMng = ChatManager(this)

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

    override fun onBackPressed() {
        if (chatMng!!.offChatIfOn()) {
            return
        }

        super.onBackPressed()
    }
}
