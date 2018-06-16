package com.akadev.hyeonmin.eq_sys_android.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.akadev.hyeonmin.eq_sys_android.R
import com.akadev.hyeonmin.eq_sys_android.activity.extension.MyCustActivity
import com.akadev.hyeonmin.eq_sys_android.volley.Login

class LoginActivity : MyCustActivity() {


    var etId: EditText? = null
    var etPw: EditText? = null

    var btnLogin: Button? = null
    var btnRegister: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginWithCache()

        etId = findViewById(R.id.et_id)
        etPw = findViewById(R.id.et_pw)
        btnLogin = findViewById(R.id.btn_login)
        btnLogin?.setOnClickListener { _ ->
            login(etId!!.text.toString(), etPw!!.text.toString())
        }

        btnRegister = findViewById(R.id.btn_register)
        btnRegister?.setOnClickListener { _ ->
            startActivityForResult(Intent(this, RegisterActivity::class.java), 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ReqCd_RegisterActivity && resultCode == ResCd_Success) {
            loginWithCache()
        }
    }

    override fun loginResult(id: String, pw: String) {
        super.loginResult(id, pw)
        startActivity(Intent(this, MainActivity::class.java))
    }

}
