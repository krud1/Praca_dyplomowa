package com.example.firebase_android;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import Activities.AddNewPositionActivity;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationsAdapter notificationAdapter = new NotificationsAdapter(context);
        String title = intent.getStringExtra("title");
        String text = intent.getStringExtra("text");
        String tableId = intent.getStringExtra("tableId");
        String notificationDate = intent.getStringExtra("notificationDate");


        PendingIntent setNew = PendingIntent.getActivity(context, 10, setNewAlarmIntent(context, tableId), PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent cancel = PendingIntent.getActivity(context, 110, cancelAlarmIntent(context, tableId, notificationDate), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder nb = notificationAdapter
                .getChannelNotification("Control app: "+ title + "\n", text)
                .addAction(R.drawable.ic_baseline_mic_24, "Repeat alarm", setNew)
                .addAction(R.drawable.ic_baseline_mic_24, "Cancel", cancel)
                .setAutoCancel(true)
                ;
        notificationAdapter.getManager().notify(createUniqueID(), nb.build());

    }
    public Intent setNewAlarmIntent(Context context, String tableId){
        Intent intent2 = new Intent(context, AddNewPositionActivity.class);
        intent2.putExtra("tableId3", tableId);
        return intent2;
    }

    public Intent cancelAlarmIntent(Context context, String tableId, String notificationDate){
        Intent intent1 = new Intent(context, AddNewPositionActivity.class);
        intent1.putExtra("tableId2", tableId);
        intent1.putExtra("finishedStatus", "finished_"+notificationDate);
        return intent1;
    }
    public int createUniqueID(){
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss",  Locale.US).format(now));
        return id;
    }

}
