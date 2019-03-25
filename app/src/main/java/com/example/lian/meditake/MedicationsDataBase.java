package com.example.lian.meditake;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {MedicationsTable.class, RemindersTable.class, PrescriptionReminder.class,
            AllMedicationsTable.class}, version = 25)
public abstract class MedicationsDataBase extends RoomDatabase {

    private static MedicationsDataBase instance;
    public abstract MedicationsDao medicationsDao();

    public static synchronized MedicationsDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), MedicationsDataBase.class,
                    "medications_database").fallbackToDestructiveMigration().addCallback(roomCallBack).build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private MedicationsDao medicationsDao;

        private PopulateDbAsyncTask(MedicationsDataBase db) {
            this.medicationsDao = db.medicationsDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }

    public static void destroyInstance() {
        instance = null;
    }
}
