package com.akadev.hyeonmin.eq_sys_android.activity

import android.os.Bundle
import com.akadev.hyeonmin.eq_sys_android.R
import com.akadev.hyeonmin.eq_sys_android.activity.extension.MyCustActivity

class MainActivity : MyCustActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
