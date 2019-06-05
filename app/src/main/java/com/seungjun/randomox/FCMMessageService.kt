package com.seungjun.randomox

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.support.v4.app.NotificationCompat

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.seungjun.randomox.activity.IntroActivity
import com.seungjun.randomox.db.LetterDBUtils
import com.seungjun.randomox.network.RetrofitApiCallback
import com.seungjun.randomox.network.RetrofitClient
import com.seungjun.randomox.network.data.HeaderInfo
import com.seungjun.randomox.utils.D
import com.seungjun.randomox.utils.PreferenceUtils

class FCMMessageService : FirebaseMessagingService() {

    private val TAG = "FCMMessageService"

    private val FCM_TYPE_NORMAL = 0
    private val FCM_TYPE_LETTER = 1

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)



        if (remoteMessage != null ){
            if(remoteMessage.data.isNotEmpty()){
                D.log(TAG, "Message FROM > " + remoteMessage.from)

                if (remoteMessage.data["message"] != null && remoteMessage.data["type"] != null) {

                    D.log(TAG, "Message Data Message > " + remoteMessage.data["message"]!!)
                    D.log(TAG, "Message Data Type > " + remoteMessage.data["type"]!!)

                    sendNotification(remoteMessage.data["message"], Integer.parseInt(remoteMessage.data["type"]!!))
                }
            }

            if (remoteMessage.notification != null) {
                D.log(TAG, "Message Notification Body: " + remoteMessage.notification!!.body!!)

            }
        }

    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }

    override fun onMessageSent(s: String?) {
        super.onMessageSent(s)
    }

    override fun onSendError(s: String?, e: Exception?) {
        super.onSendError(s, e)
    }

    override fun onNewToken(s: String?) {
        super.onNewToken(s)

        D.log(TAG, "NEW FCM TOKEN > " + s!!)

        PreferenceUtils.getInstance(applicationContext).userFcmKey = s

        if (PreferenceUtils.getInstance(applicationContext).isLoginSuccess) {

            RetrofitClient.callPostFcmUpdate(object : RetrofitApiCallback<HeaderInfo> {
                        override fun onError(t: Throwable) {
                            D.log(TAG, "FCM TOKEN UPDATE FAIL > " + t.message)
                        }

                        override fun onSuccess(code: Int, resultData: HeaderInfo) {

                            if (resultData.reqCode == 0)
                                D.log(TAG, "FCM TOKEN UPDATE")
                            else
                                D.log(TAG, "FCM TOKEN UPDATE FAIL > " + resultData.reqCode)
                        }

                        override fun onFailed(code: Int) {
                            D.log(TAG, "FCM TOKEN UPDATE FAIL > $code")
                        }
                    }, PreferenceUtils.getInstance(applicationContext).userKey!!, s)

        }

    }


    private fun sendNotification(messageBody: String?, type: Int) {

        val intent = Intent(this, IntroActivity::class.java)

        if (type == FCM_TYPE_LETTER) {
            intent.putExtra("textData", messageBody)
            LetterDBUtils.saveLetterData(messageBody!!)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val channelId = getString(R.string.default_notification_channel_id)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder =
                NotificationCompat.Builder(this, channelId).apply {
                    setSmallIcon(R.mipmap.ic_launcher)
                    setContentTitle(getString(R.string.app_name))
                    setContentText(if (type == FCM_TYPE_LETTER) "개발자의 편지가 도착했습니다 :)" else messageBody)
                    setAutoCancel(true)
                    setSound(defaultSoundUri)
                    setContentIntent(pendingIntent)
                }


        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "랜덤OX", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())

    }
}
