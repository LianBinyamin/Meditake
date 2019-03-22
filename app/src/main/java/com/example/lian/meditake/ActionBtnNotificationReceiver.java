package com.example.lian.meditake;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import java.util.Calendar;

public class ActionBtnNotificationReceiver extends BroadcastReceiver {
    public static final String URI_BASE = ActionBtnNotificationReceiver.class.getName() + ".";
    public static final String TAKE_ACTION = URI_BASE + "TAKE_ACTION";
    public static final String SNOOZE_ACTION = URI_BASE + "SNOOZE_ACTION";
    Intent intent;
    Context context;
    private String name;
    private int medicationId;
    private double doses;
    private double mg;
    private int reminderId;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.d("TAKE BTN CLICKED!", "###### inside take receiver! ######");

        this.intent = intent;
        this.context = context;

        doses = intent.getDoubleExtra(AlertReceiver.DOSES_TAKE, 1);
        name = intent.getStringExtra(AlertReceiver.NAME_TAKE);
        mg = intent.getDoubleExtra("mg", 100);
        medicationId = intent.getIntExtra(AlertReceiver.MEDICATION_ID, 0);
        reminderId = intent.getIntExtra(AlertReceiver.REMINDER_ID_KEY, 100);

        dismissNotification();
        stopSms();

        String action = intent.getAction();

        if (action.equals(TAKE_ACTION)) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    checkPrescriptionReminder();
                }
            });
            t.start();
        }
        else {  //SNOOZE_ACTION
            snooze();
        }
    }

    public void dismissNotification() {
        Log.d("TAKE BTN RECEIVER", "%%%%% dismiss notification %%%%%");

        int keyNotification = intent.getIntExtra(AlertReceiver.NOTIFICATION_KEY, 0);
        NotificationHelper notificationHelper = new NotificationHelper(context);
        notificationHelper.getManager().cancel(keyNotification);
    }

    public void stopSms() {
        Log.d("TAKE BTN RECEIVER", "%%%%% stop sms %%%%%");

        //stop sms from firing
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, SmsReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 1000, myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }

    public void checkPrescriptionReminder() {
        Log.d("TAKE BTN RECEIVER", "%%%%% check prescription %%%%%");

        MedicationsDataBase db = MedicationsDataBase.getInstance(context);
        MedicationsTable medication = db.medicationsDao().getMedicationByName(name);

        //check if medication has prescription refill
        if (medication.isPrescription()) {
            PrescriptionReminder prescriptionReminder = db.medicationsDao().getPrescriptionByMedicationId(medicationId);
            double updatedAmount = prescriptionReminder.getAmountDif() - doses;
            db.medicationsDao().updateDifAmountPrescription(updatedAmount, medicationId);

            if (updatedAmount == 0) {   //dif is 0 so need to schedule alarm for refill notification
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                Calendar setcalendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, prescriptionReminder.getHour());
                calendar.set(Calendar.MINUTE, prescriptionReminder.getMinutes());
                calendar.set(Calendar.SECOND, 0);

                if (calendar.before(setcalendar)) {
                    Log.d("IF CALENDAR BEFORE", "inside if");
                    calendar.add(Calendar.DATE, 1);
                }

                Intent intentPres = new Intent(context, PrescriptionAlertReceiver.class);
                intentPres.putExtra(AlertReceiver.NAME_TAKE, name);
                intentPres.putExtra(PrescriptionAlertReceiver.MEDICATION_ID_PRES, medicationId);
                intentPres.putExtra("mg", mg);
                intentPres.putExtra(PrescriptionAlertReceiver.START_AMOUNT_PRES, prescriptionReminder.getStartAmount());
                intentPres.putExtra(PrescriptionAlertReceiver.REMAINING_AMOUNT_PRES, prescriptionReminder.getReminderAmount());
                intentPres.putExtra(PrescriptionAlertReceiver.HOUR_PRES, prescriptionReminder.getHour());
                intentPres.putExtra(PrescriptionAlertReceiver.MINUTES_PRES, prescriptionReminder.getMinutes());

                PendingIntent pendingIntentPres = PendingIntent.getBroadcast(context,
                        prescriptionReminder.getId(), intentPres, PendingIntent.FLAG_UPDATE_CURRENT);

                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntentPres);
            }
        }
    }

    public void snooze() {
        //TODO: don't forget change snooze to 10 minutes!!
        Log.d("TAKE BTN RECEIVER", "%%%%% snooze %%%%%");

        Intent snoozeIntent = new Intent(context, AlertReceiver.class);
        snoozeIntent.putExtra(AlertReceiver.MEDICATION_ID, medicationId);
        snoozeIntent.putExtra(AlertReceiver.DOSES_KEY, doses);
        snoozeIntent.putExtra(AlertReceiver.NAME_KEY, name);
        snoozeIntent.putExtra("mg", mg);
        snoozeIntent.putExtra(AlertReceiver.REMINDER_ID_KEY, reminderId);

        PendingIntent pendingIntentSnooze = PendingIntent.getBroadcast(context,
                reminderId, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() +
                        10 * 60 * 1000, pendingIntentSnooze);    //snooze 10 minute in future

    }
}
