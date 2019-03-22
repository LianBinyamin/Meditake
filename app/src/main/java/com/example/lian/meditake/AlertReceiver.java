package com.example.lian.meditake;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class AlertReceiver extends BroadcastReceiver {
    private String name, day;
    private double doses;
    private int id;
    private int medicationId;
    private double mg;
    public static final String PHONE_SERVICE_KEY = "phone_service";
    public static final String NOTIFICATION_KEY = "notification_key";
    public static final String NAME_KEY = "name_alert";
    public static final String DAY_KEY = "day_alert";
    public static final String DOSES_KEY = "doses_alert";
    public static final String REMINDER_ID_KEY = "reminder_id";
    public static final String MEDICATION_ID = "medication_id";
    public static final String DOSES_TAKE = "doses_take_receiver";
    public static final String NAME_TAKE = "name_take_prescription";

    @Override
    public void onReceive(Context context, Intent intent) { //this method is called when the alarm is fired

        Log.d("ALERT RECEIVER", "######## inside alert receiver ########");

        name = intent.getStringExtra(NAME_KEY);
        //day = intent.getStringExtra(DAY_KEY);
        doses = intent.getDoubleExtra(DOSES_KEY, 1);
        id = intent.getIntExtra(REMINDER_ID_KEY, 100);
        medicationId = intent.getIntExtra(MEDICATION_ID, 0);
        mg = intent.getDoubleExtra("mg", 100);

        int key = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        //take action button
        Intent takeIntent = new Intent(context, ActionBtnNotificationReceiver.class);
        takeIntent.putExtra(NOTIFICATION_KEY, key);
        takeIntent.putExtra(MEDICATION_ID, medicationId);
        takeIntent.putExtra(DOSES_TAKE, doses);
        takeIntent.putExtra(NAME_TAKE, name);
        takeIntent.putExtra("mg", mg);
        takeIntent.setAction(ActionBtnNotificationReceiver.TAKE_ACTION);

        PendingIntent takePendingIntent = PendingIntent.getBroadcast(context, 1, takeIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        //snooze action button
        Intent snoozeIntent = new Intent(context, ActionBtnNotificationReceiver.class);
        snoozeIntent.putExtra(NOTIFICATION_KEY, key);   //for cancelling notification
        snoozeIntent.putExtra(MEDICATION_ID, medicationId);
        snoozeIntent.putExtra(DOSES_TAKE, doses);
        snoozeIntent.putExtra(NAME_TAKE, name);
        snoozeIntent.putExtra("mg", mg);
        snoozeIntent.putExtra(REMINDER_ID_KEY, id);
        snoozeIntent.setAction(ActionBtnNotificationReceiver.SNOOZE_ACTION);

        PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(context, 1, snoozeIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification()
                .setContentTitle(context.getResources().getString(R.string.notification_title))
                .setContentText(context.getResources().getString(R.string.notification_msg,
                        doses, name))
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.icon_meditake)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setColor(Color.BLUE)
                .addAction(R.drawable.ic_check, context.getResources().getString(R.string.take_action_btn), takePendingIntent)
                .addAction(R.drawable.ic_alarm, "Snooze", snoozePendingIntent);

        notificationHelper.getManager().notify(key, nb.build());

        //SMS service to medifriend
        SharedPreferences prefs = context.getSharedPreferences(MainActivity.PREFS_MEDIFRIEND, MODE_PRIVATE);
        String name = prefs.getString(MainActivity.NAME_MEDIFRIEND_PREFS, null);
        String phone = prefs.getString(MainActivity.PHONE_MEDIFRIEND_PREFS, null);
        if (name != null && phone != null) {    //medifriend exists
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Intent intentSms = new Intent(context, SmsReceiver.class);
            intentSms.putExtra(PHONE_SERVICE_KEY, phone);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,1000,
                    intentSms, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() +
                            60 * 1000, pendingIntent);  //alarm to 1 minute in future

        }
    }
}
