package com.akadev.hyeonmin.eq_sys_android.util

import android.app.Activity
import android.content.Context

class Caches(activity: Activity) {
    private val sf = activity.getSharedPreferences("account", Context.MODE_PRIVATE)!!

    var id: String
        get() = sf.getString("id", "")
        set(value) {
            sf.edit().putString("id", value).commit()
        }

    var pw: String
        get() = sf.getString("pw", "")
        set(value) {
            sf.edit().putString("pw", value).commit()
        }
}