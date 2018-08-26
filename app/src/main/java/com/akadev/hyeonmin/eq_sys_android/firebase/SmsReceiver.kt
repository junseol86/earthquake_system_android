package com.akadev.hyeonmin.eq_sys_android.firebase

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsMessage
import com.akadev.hyeonmin.eq_sys_android.volley.SmsParseAlarm
import java.util.*

class SmsReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent!!.action.equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {

            var smsList: ArrayList<String> = ArrayList()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                for (smsMessage in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                    smsList.add(smsMessage.messageBody)
                }
            }
            for (sms in smsList) {
                if (sms.contains("협착 및 이상징후 발생")) {
                    SmsParseAlarm(context!!).sendReport(sms)
                }
            }
        }
    }
}