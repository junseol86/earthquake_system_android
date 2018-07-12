package com.akadev.hyeonmin.eq_sys_android.activity.extension

import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.akadev.hyeonmin.eq_sys_android.util.Caches
import com.akadev.hyeonmin.eq_sys_android.util.Singleton
import com.akadev.hyeonmin.eq_sys_android.volley.Login


open class ActivityCommon(val atvt: Activity, val af: ACFuncs) {

    val ReqCd_RegisterActivity = 0

    val ResCd_Cancel = 100
    val ResCd_Success = 101

    var caches: Caches = Caches(atvt)
    var loginVly: Login = Login(this)

    fun alert(message: String) {
        AlertDialog.Builder(atvt)
                .setTitle("오류")
                .setMessage(message)
                .show()
    }

    fun login(id: String, pw: String) {
        loginVly?.login(id, pw)
    }

    fun loginWithCache() {
        if (caches!!.id.isNotEmpty() && caches!!.pw.isNotEmpty()) {
            login(caches!!.id, caches!!.pw)
        }
    }

    open fun loginResult(id: String, pw: String, jwtToken: String) {
        Singleton.loginResult(jwtToken)
        caches?.id = id
        caches?.pw = pw
        af.loginResult()
    }

    fun askCallPermission() {
        if (ContextCompat.checkSelfPermission(atvt, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(atvt, arrayOf(android.Manifest.permission.CALL_PHONE), 0)
        }
    }
}