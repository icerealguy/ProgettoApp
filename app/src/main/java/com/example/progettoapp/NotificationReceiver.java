package com.example.progettoapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(context.NOTIFICATION_SERVICE);
        Intent repeating_intent = new Intent(context, RepeatingActivity.class);
        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT );
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.default_notification_channel_id))
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_notifica)
                .setContentTitle("Bevi")
                .setContentText("Ricorda di bere")
                .setAutoCancel(true);
        notificationManager.notify(100, builder.build());

    }
}
