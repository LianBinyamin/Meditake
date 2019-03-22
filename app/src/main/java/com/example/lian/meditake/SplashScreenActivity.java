package com.example.lian.meditake;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class SplashScreenActivity extends AppCompatActivity {

    public static MedicationsDataBase myDb;
    //final String PREFS_NAME = "MyPrefsFile";
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = getPreferences(MODE_PRIVATE);
        boolean isFirstTime = settings.getBoolean("my_first_time", true);

        Intent intent;

        if (isFirstTime) {

            //the app is being launched for the first time, go to get started activity
            Log.d("Comments", "First time");

            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).apply();

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    myDb = Room.databaseBuilder(getApplicationContext(),
                            MedicationsDataBase.class, "medications_database").build();
                }
            });

            t.start();

//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    initAllMedicationsTable();
//                }
//            });
//
//            thread.start();

            try {
                t.join();
                //thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            intent = new Intent(getApplicationContext(), GetStartedActivity.class);
        }

        else {  //not the first time app is being launched
            intent = new Intent(getApplicationContext(), MainActivity.class);
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                initAllMedicationsTable();
            }
        });

        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        startActivity(intent);
        finish();

    }

    public void initAllMedicationsTable() {
        MedicationsDataBase db = MedicationsDataBase.getInstance(getApplicationContext());
        List<AllMedicationsTable> list = db.medicationsDao().getAllMedicationsTable();
        Log.d("list size", String.valueOf(list.size()));
        if (list.isEmpty()) {
            db.medicationsDao().insertMedication(new AllMedicationsTable("Acamol"));
            db.medicationsDao().insertMedication(new AllMedicationsTable("Advil"));
            db.medicationsDao().insertMedication(new AllMedicationsTable("Optalgin"));
            db.medicationsDao().insertMedication(new AllMedicationsTable("Baclofen"));
            db.medicationsDao().insertMedication(new AllMedicationsTable("Caspofungin"));
            db.medicationsDao().insertMedication(new AllMedicationsTable("Dexamol"));
            db.medicationsDao().insertMedication(new AllMedicationsTable("Efavirenz"));
            db.medicationsDao().insertMedication(new AllMedicationsTable("Fentanyl"));
            db.medicationsDao().insertMedication(new AllMedicationsTable("Goserelin"));
            db.medicationsDao().insertMedication(new AllMedicationsTable("Haloperidol"));
            db.medicationsDao().insertMedication(new AllMedicationsTable("Ibuprofen"));
            db.medicationsDao().insertMedication(new AllMedicationsTable("Ketotifen"));
            db.medicationsDao().insertMedication(new AllMedicationsTable("Lanton"));
            db.medicationsDao().insertMedication(new AllMedicationsTable("Maprotiline"));
            db.medicationsDao().insertMedication(new AllMedicationsTable("Nurofen"));
            db.medicationsDao().insertMedication(new AllMedicationsTable("Penicillin"));
            db.medicationsDao().insertMedication(new AllMedicationsTable("Quinidine"));
            db.medicationsDao().insertMedication(new AllMedicationsTable("Raloxifene"));
            db.medicationsDao().insertMedication(new AllMedicationsTable("Sotalol"));
            db.medicationsDao().insertMedication(new AllMedicationsTable("Tazarotene"));
            db.medicationsDao().insertMedication(new AllMedicationsTable("Ursodeoxycholic"));
            db.medicationsDao().insertMedication(new AllMedicationsTable("Verapamil"));
            db.medicationsDao().insertMedication(new AllMedicationsTable("Warfarin"));
            db.medicationsDao().insertMedication(new AllMedicationsTable("Xylometazoline"));
            db.medicationsDao().insertMedication(new AllMedicationsTable("Yentreve"));
            db.medicationsDao().insertMedication(new AllMedicationsTable("Ziprasidone"));
        }
    }
}
