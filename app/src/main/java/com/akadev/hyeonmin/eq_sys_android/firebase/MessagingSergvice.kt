package com.akadev.hyeonmin.eq_sys_android.firebase

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.VibrationEffect
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import com.akadev.hyeonmin.eq_sys_android.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.akadev.hyeonmin.eq_sys_android.activity.LoginActivity
import com.akadev.hyeonmin.eq_sys_android.util.Singleton

class MessagingSergvice: FirebaseMessagingService()
//        , MediaPlayer.OnPreparedListener
{

    override fun onMessageReceived(rm: RemoteMessage?) {

//        앱이 실행중이라면 Broadcast를 보냄
        if (Singleton.activityOn) {
            var brIntent = Intent("com.akadev.hyeonmin.eq_sys_android")
            brIntent.putExtra("type", rm!!.data["type"])
            brIntent.putExtra("title", rm.data["title"])
            brIntent.putExtra("body", rm.data["body"])
            brIntent.action = "EqSystem"

            sendBroadcast(brIntent)

//            앱이 꺼져있다면 푸시알람을 띄움
        } else {

//            소리조절 - 여기서 미리 해놔야 아래 소리알림에 적용이 됨
            val manager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                manager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_UNMUTE, 0)
                manager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_UNMUTE, 0)
                manager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_UNMUTE, 0)
            } else {
                manager.setStreamMute(AudioManager.STREAM_SYSTEM, false)
                manager.setStreamMute(AudioManager.STREAM_RING, false)
                manager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false)
            }
            manager.setStreamVolume(AudioManager.STREAM_SYSTEM, manager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM), 0)
            manager.setStreamVolume(AudioManager.STREAM_RING, manager.getStreamMaxVolume(AudioManager.STREAM_RING), 0)
            manager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, manager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION), 0)

//            팝업 띄우기

            var channelId = "EqSystem"
            val nb = NotificationCompat.Builder(this, channelId)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setBadgeIconType(R.mipmap.ic_launcher_2)
                    .setContentTitle(rm!!.data["title"])
                    .setContentText(rm.data["body"])
                    .setColor(Color.WHITE)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setAutoCancel(true)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                nb.setSmallIcon(R.mipmap.ic_launcher_round_2)
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_round_2))

            } else {
                nb.setSmallIcon(R.mipmap.ic_launcher_round_3)
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_round_3))

            }

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
                        NotificationChannel(channelId, "MyChannel", NotificationManager.IMPORTANCE_HIGH)
                )
            }

            nb.setVibrate(longArrayOf(1000, 2000, 3000, 4000))
            nb.setSound(
                    Uri.parse(
                            "android.resource://${applicationContext.packageName}/raw/siren"
                    )
            )

            nm.notify(0, nb.build())

            if (rm.data["type"] == "earthquake" || rm.data["type"] == "structure") {
                val r = RingtoneManager.getRingtone(applicationContext, Uri.parse(
                        "android.resource://${applicationContext.packageName}/raw/siren"
                ))
                r.play()
            }

//            소리 재생 - 위에 조절한 볼륨으로
//            val soundNotification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//            val player = MediaPlayer()
//
//            try {
//                player.setDataSource(applicationContext,
//                        Uri.parse(
//                                "android.resource://com.akadev.hyeonmin.eq_sys_android/raw/siren"
//                        ))
//
//            } catch (e: Exception) {
//
//            }
//
//            player.prepareAsync()
//
        }

    }
//
//    override fun onPrepared(mp: MediaPlayer?) {
//        mp?.start()
//    }

}