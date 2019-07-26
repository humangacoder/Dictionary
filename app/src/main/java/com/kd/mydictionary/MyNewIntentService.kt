package com.kd.mydictionary

import androidx.core.app.NotificationManagerCompat
import android.app.PendingIntent
import android.content.Intent
import android.R
import android.app.IntentService
import android.app.Notification


class MyNewIntentService : IntentService("MyNewIntentService"){

    override fun onHandleIntent(intent: Intent?) {
        val wordoftheday = intent?.getStringExtra("Word")
        val builder = Notification.Builder(this)
        builder.setContentTitle("Word of the day")
        builder.setContentText(wordoftheday)
        builder.setSmallIcon(R.drawable.sym_def_app_icon)
        val notifyIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        //to be able to launch your activity from the notification
        builder.setContentIntent(pendingIntent)
        val notificationCompat = builder.build()
        val managerCompat = NotificationManagerCompat.from(this)
        managerCompat.notify(NOTIFICATION_ID, notificationCompat)
    }

    companion object {
        private val NOTIFICATION_ID = 3
    }
}