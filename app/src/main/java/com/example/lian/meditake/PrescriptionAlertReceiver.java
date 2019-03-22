package com.example.lian.meditake;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Date;

public class PrescriptionAlertReceiver extends BroadcastReceiver {
    public static final String HOUR_PRES = "hour_pres";
    public static final String MINUTES_PRES = "minutes_pres";
    public static final String REMAINING_AMOUNT_PRES = "remaining_amount";
    public static final String START_AMOUNT_PRES = "start_amount";
    public static final String MEDICATION_ID_PRES = "medication_id_pres";

    @Override
    public void onReceive(final Context context, final Intent intent) {

        Log.d("PRESCRIPTION RECEIVER", "###### inside prescription receiver! ######");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("PRESC THREAD", "###### inside thread prescription receiver! ######");
                String name = intent.getStringExtra(AlertReceiver.NAME_TAKE);
                double mg = intent.getDoubleExtra("mg", 100);

                int key = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

                NotificationHelper notificationHelper = new NotificationHelper(context);
                NotificationCompat.Builder nb = notificationHelper.getChannelNotification()
                        .setContentTitle("Prescription Alert")
                        .setContentText("You need to refill your " + name + ", " + mg + " medication")
                        .setAutoCancel(true)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.icon_meditake)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setColor(Color.BLUE);

                notificationHelper.getManager().notify(key, nb.build());

                //update table and renew difAmount value
                int startAmount = intent.getIntExtra(START_AMOUNT_PRES, 30);
                int remainingAmount = intent.getIntExtra(REMAINING_AMOUNT_PRES, 10);
                int medicationId = intent.getIntExtra(MEDICATION_ID_PRES, 0);
                int hour = intent.getIntExtra(HOUR_PRES, 11);
                int minutes = intent.getIntExtra(MINUTES_PRES, 0);

                MedicationsDataBase db = MedicationsDataBase.getInstance(context);
                db.medicationsDao().deleteFromPrescriptionsById(medicationId);  //delete reminder
                //make new one
                PrescriptionReminder prescriptionReminder = new PrescriptionReminder(medicationId, hour, minutes,
                        (startAmount+remainingAmount), remainingAmount, (double)(startAmount-remainingAmount));
                db.medicationsDao().insertPrescriptionReminder(prescriptionReminder);
            }
        });

        thread.start();
    }
}
