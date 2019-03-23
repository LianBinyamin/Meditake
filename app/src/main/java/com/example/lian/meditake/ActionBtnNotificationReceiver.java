package com.example.lian.meditake;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

public class ActionBtnNotificationReceiver extends BroadcastReceiver {
    public static final String URI_BASE = ActionBtnNotificationReceiver.class.getName() + ".";
    public static final String TAKE_ACTION = URI_BASE + "TAKE_ACTION";
    public static final String SNOOZE_ACTION = URI_BASE + "SNOOZE_ACTION";
    public static final String TTS_ACTION = URI_BASE + "TTS_ACTION";
    public static final long SNOOZE = 10 * 60 * 1000;   //snooze 10 minute in future
    Intent intent;
    Context context;
    private String name;
    private int medicationId;
    private double doses;
    private double mg;
    private int reminderId;
    private TextToSpeech tts;

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

        stopSms();

        String action = intent.getAction();

        if (action.equals(TAKE_ACTION)) {
            dismissNotification();
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    checkPrescriptionReminder();
                }
            });
            t.start();
        }
        else if (action.equals(SNOOZE_ACTION)) {
            dismissNotification();
            snooze();
        }
        else {  //SPEAK_ACTION
            final String msg = context.getResources().getString(R.string.notification_msg, doses, name);
            tts = new TextToSpeech(context.getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        int ttsLang = tts.setLanguage(Locale.US);

                        if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                                || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                            Log.e("TTS", "The Language is not supported!");
                        } else {
                            Log.i("TTS", "Language Supported.");
                        }
                        Log.i("TTS", "Initialization success.");
                    } else {
                        Toast.makeText(context, "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                    }
                    speakNow(msg);
                }
            });
        }

    }

    public void speakNow(String msg) {
        Log.i("TTS", "action button clicked: " + msg);
        int speechStatus = tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null, null);

        if (speechStatus == TextToSpeech.ERROR) {
            Log.e("TTS", "Error in converting Text to Speech!");
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
                SystemClock.elapsedRealtime() + SNOOZE, pendingIntentSnooze);

    }
}
