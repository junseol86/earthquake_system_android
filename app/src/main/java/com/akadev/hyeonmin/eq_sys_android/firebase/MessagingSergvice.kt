package com.akadev.hyeonmin.eq_sys_android.firebase

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import com.akadev.hyeonmin.eq_sys_android.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.akadev.hyeonmin.eq_sys_android.activity.LoginActivity
import com.akadev.hyeonmin.eq_sys_android.util.Singleton

class MessagingSergvice: FirebaseMessagingService() {
    override fun onMessageReceived(rm: RemoteMessage?) {

//        앱이 실행중이라면 Broadcast를 보냄
        if (Singleton.activityOn) {
            var brIntent = Intent("com.akadev.hyeonmin.eq_sys_android")
            brIntent.putExtra("type", rm!!.data["type"])
            brIntent.putExtra("title", rm!!.data["title"])
            brIntent.putExtra("body", rm!!.data["body"])
            brIntent.action = "EqSystem"

            sendBroadcast(brIntent)

//            앱이 꺼져있다면 푸시알람을 띄움
        } else {

            var ChannelId = "EqSystem"
            val nb = NotificationCompat.Builder(this, ChannelId)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setSmallIcon(R.mipmap.ic_launcher_round_2)
                    .setBadgeIconType(R.mipmap.ic_launcher_2)
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_round_2))
                    .setContentTitle(rm!!.data["title"])
                    .setContentText(rm!!.data["body"])
                    .setColor(Color.WHITE)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

            val resultIntent = Intent(this, LoginActivity::class.java)

            val sb = TaskStackBuilder.create(this)
            sb.addParentStack(LoginActivity::class.java)
            sb.addNextIntent(resultIntent)

            val ri = sb.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

//            풀스크린인텐트를 사용하면 노티피케이션이 안 사라짐.  구버전에선 안 되는듯
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                nb.setFullScreenIntent(ri, true)
            } else {
                nb.setContentIntent(ri)
            }

            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                nm.createNotificationChannel(
                        NotificationChannel(ChannelId, ChannelId, NotificationManager.IMPORTANCE_HIGH)
                )
            }
            nm.notify(0, nb.build())

        }


    }

//    fun appRunning(): Boolean {
//        var result = false
//        val am = this.getSystemService(android.content.Context.ACTIVITY_SERVICE) as ActivityManager
//        return result
//    }

}