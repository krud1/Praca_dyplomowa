package com.example.firebase_android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

public class SmsBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String phone_number = intent.getStringExtra("number");
        String message = intent.getStringExtra("message");
        SmsManager sendSms = SmsManager.getDefault();
        sendSms.sendTextMessage(phone_number, null, message, null, null );
    }
}

