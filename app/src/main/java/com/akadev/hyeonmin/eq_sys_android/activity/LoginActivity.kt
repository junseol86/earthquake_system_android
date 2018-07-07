package com.akadev.hyeonmin.eq_sys_android.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.akadev.hyeonmin.eq_sys_android.R
import com.akadev.hyeonmin.eq_sys_android.activity.MainActivity.MainActivity
import com.akadev.hyeonmin.eq_sys_android.activity.extension.ACFuncs
import com.akadev.hyeonmin.eq_sys_android.activity.extension.ActivityCommon

class LoginActivity: Activity() {

    var ac: ActivityCommon? = null

    var etId: EditText? = null
    var etPw: EditText? = null

    var btnLogin: Button? = null
    var btnRegister: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val thisActivity = this
        ac = ActivityCommon(this, object:ACFuncs {
            override fun loginResult() {
                startActivity(Intent(thisActivity, MainActivity::class.java))
            }
        })

        ac?.loginWithCache()

        etId = findViewById(R.id.et_id)
        etPw = findViewById(R.id.et_pw)
        btnLogin = findViewById(R.id.btn_login)
        btnLogin?.setOnClickListener { _ ->
            ac?.login(etId!!.text.toString(), etPw!!.text.toString())
        }

        btnRegister = findViewById(R.id.btn_register)
        btnRegister?.setOnClickListener { _ ->
            startActivityForResult(Intent(this, RegisterActivity::class.java), 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ac!!.ReqCd_RegisterActivity && resultCode == ac!!.ResCd_Success) {
            ac?.loginWithCache()
        }
    }

}
