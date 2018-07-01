package com.akadev.hyeonmin.eq_sys_android.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.akadev.hyeonmin.eq_sys_android.R
import com.akadev.hyeonmin.eq_sys_android.activity.extension.ACFuncs
import com.akadev.hyeonmin.eq_sys_android.activity.extension.ActivityCommon
import com.akadev.hyeonmin.eq_sys_android.volley.Register

class RegisterActivity: Activity() {

    var ac: ActivityCommon? = null

    var registerVly: Register? = null

    var etId: EditText? = null
    var etPw1: EditText? = null
    var etPw2: EditText? = null
    var etCode: EditText? = null
    var etName: EditText? = null
    var etPhone: EditText? = null

    var btOk: Button? = null
    var btCancel: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        ac = ActivityCommon(this, object: ACFuncs {})

        registerVly = Register(this)

        etId = findViewById(R.id.et_id)
        etPw1 = findViewById(R.id.et_pw1)
        etPw2 = findViewById(R.id.et_pw2)
        etCode = findViewById(R.id.et_code)
        etName = findViewById(R.id.et_name)
        etPhone = findViewById(R.id.et_phone)

        btOk = findViewById(R.id.btn_ok)
        btOk?.setOnClickListener { _ ->

            if (
                    etId!!.text.isEmpty() ||
                    etPw1!!.text.isEmpty() ||
                    etName!!.text.isEmpty() ||
                    etPhone!!.text.isEmpty()
                    ) {
                ac?.alert("모든 항목을 입력하세요.")
            } else if (!etPw1!!.text.toString().equals(etPw2!!.text.toString())) {
                ac?.alert("비밀번호를 동일하게 입력하세요.")
            } else {
                var params = HashMap<String, String>()
                params["mbr_id"] = etId!!.text.toString()
                params["password"] = etPw1!!.text.toString()
                params["code"] = etCode!!.text.toString()
                params["mbr_name"] = etName!!.text.toString()
                params["mbr_phone"] = etPhone!!.text.toString()
                registerVly?.register(params)
            }

        }

        btCancel = findViewById(R.id.btn_cancel)
        btCancel?.setOnClickListener { _ ->
            setResult(ac!!.ResCd_Cancel)
            finish()
        }
    }
}
