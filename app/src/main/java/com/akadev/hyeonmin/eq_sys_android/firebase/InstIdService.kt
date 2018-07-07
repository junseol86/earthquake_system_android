package com.akadev.hyeonmin.eq_sys_android.firebase

import com.akadev.hyeonmin.eq_sys_android.util.Singleton
import com.google.firebase.iid.FirebaseInstanceIdService

class InstIdService: FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        Singleton.fcmRefreshed = true
    }
}