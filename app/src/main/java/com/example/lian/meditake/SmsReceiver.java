package com.example.lian.meditake;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("SMS RECEIVER", "####### inside sms receiver #######");

        //int keyNotification = intent.getIntExtra("key", 0);
        String phone = intent.getStringExtra(AlertReceiver.PHONE_SERVICE_KEY);

        SmsManager sm = SmsManager.getDefault();
        sm.sendTextMessage(phone, null, context.getResources().getString(R.string.sms_body)
                , null, null);
    }
}
