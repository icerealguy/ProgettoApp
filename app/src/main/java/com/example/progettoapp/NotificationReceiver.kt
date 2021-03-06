package com.example.progettoapp

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = context
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val repeating_intent = Intent(context, RepeatingActivity::class.java)
        repeating_intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pendingIntent = PendingIntent.getActivity(context, 100, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(context, context.getString(R.string.default_notification_channel_id))
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_notifica)
                .setContentTitle("Bevi")
                .setContentText("Ricorda di bere")
                .setAutoCancel(true)
        notificationManager.notify(100, builder.build())
    }
}