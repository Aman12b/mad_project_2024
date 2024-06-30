package com.example.mad_project.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "Flight Reminder"
        val message = intent.getStringExtra("message") ?: "Your flight is coming up soon!"

        val notificationHelper = NotificationHelper(context)
        notificationHelper.createNotification(title, message)
    }
}