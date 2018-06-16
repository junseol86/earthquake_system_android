package com.akadev.hyeonmin.eq_sys_android.util

object Singleton {
    var jwtToken = ""
    fun loginResult(jt: String) {
        jwtToken = jt
    }
}