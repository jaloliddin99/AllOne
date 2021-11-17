package com.tesseract.AllOneDriver.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tesseract.AllOneClient.BuildConfig
import com.tesseract.AllOneClient.MainActivity
import com.tesseract.AllOneClient.R

class AppFirebaseMessagingService : FirebaseMessagingService(){
    var TAG = "@@@"
    override fun onNewToken(token: String) {
        Log.d("@@@", "Debug-token $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        try {
            Log.d(TAG, "onMessageReceived:body ${remoteMessage?.notification?.body.toString()}")
            Log.d(TAG, "onMessageReceived:title ${remoteMessage?.notification?.title.toString()}")
            Log.d(TAG, "onMessageReceived: ${remoteMessage?.from.toString()}")
            Log.d(TAG, "onMessageReceived: ${remoteMessage?.data.toString()}")
//            try {
//
//            } catch (e: java.lang.Exception) {
//
//            }
            val title = remoteMessage?.notification?.title
            val body = remoteMessage?.notification?.body
            showMessaging(title ?: "", body ?: "")


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showMessaging(title: String, body: String, id: Long = System.currentTimeMillis()) {
        val defaultSoundUrl: Uri? = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        var intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val paddingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val channelID = BuildConfig.APPLICATION_ID
        val builder = NotificationCompat.Builder(this, channelID)
            .setDefaults(DEFAULT_BUFFER_SIZE)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setSmallIcon(R.drawable.ic_car)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    applicationContext.resources,
                    R.drawable.ic_car
                )
            )
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setColor(Color.parseColor("#FFFFFF"))
            .setSound(defaultSoundUrl)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
            .setContentIntent(paddingIntent)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelID, "${BuildConfig.APPLICATION_ID} channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }
        manager.notify(id.toInt(), builder.build())
    }
}