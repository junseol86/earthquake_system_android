package com.akadev.hyeonmin.eq_sys_android.activity.extension

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import com.akadev.hyeonmin.eq_sys_android.util.Cache
import com.akadev.hyeonmin.eq_sys_android.volley.Login


open class MyCustActivity: Activity() {

    val ReqCd_RegisterActivity = 0

    val ResCd_Cancel = 100
    val ResCd_Success = 101

    var cache: Cache? = null
    var loginVly: Login? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cache = Cache(this)
        loginVly = Login(this)
    }

    fun alert(message: String) {
        AlertDialog.Builder(this)
                .setTitle("오류")
                .setMessage(message as String)
                .show()
    }

    fun login(id: String, pw: String) {
        loginVly?.login(id, pw)
    }

    fun loginWithCache() {
        if (cache!!.id.isNotEmpty() && cache!!.pw.isNotEmpty()) {
            login(cache!!.id, cache!!.pw)
        }
    }

    open fun loginResult(id: String, pw: String) {
        cache?.id = id
        cache?.pw = pw
    }
}