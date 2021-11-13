package com.geckosoftlabs.mentorr.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.geckosoftlabs.mentorr.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseInstanceIDService: FirebaseMessagingService () {
    private var NOTIFICATION_CHANNEL_ID = "net.larnetch.notification"
    private var NOTIFICATION_ID = 100

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        Log.e("message", "Message Received")

        if(p0.data.isNotEmpty()) {
            val title = p0.data["title"]
            val body = p0.data["body"]
            showNotification(applicationContext, title, body)
        }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.e("token", "get new token")
    }

    private fun showNotification(
        context: Context,
        title: String?,
        message: String?
    ) {
        val ii = Intent(context, MainActivity::class.java)
        ii.data = Uri.parse("custom://" + System.currentTimeMillis())
        ii.action = "actionstring" + System.currentTimeMillis()
        ii.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP

        val pi = PendingIntent.getActivity(context, 0, ii, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification: Notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = NotificationCompat.Builder(context, context.packageName)
                .setOngoing(true)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pi)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setWhen(System.currentTimeMillis())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(title)
                .build()

            val notificationManager = context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                title,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
            notificationManager.notify(NOTIFICATION_ID, notification)
        } else {
            notification = NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setContentText(message)
                .setContentIntent(pi)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(title)
                .build()
            val notificationManager = context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager
            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }
}