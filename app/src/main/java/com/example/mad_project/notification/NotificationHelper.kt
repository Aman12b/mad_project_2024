package com.example.mad_project.notification

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.mad_project.R

class NotificationHelper(private val context: Context) {

    @SuppressLint("ServiceCast")
    fun createNotification(title: String, message: String) {
        val builder = NotificationCompat.Builder(context, "flight_channel_id")
            //.setSmallIcon(R.drawable.) // Passe das Symbol an
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, builder.build())
    }
}