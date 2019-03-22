package com.example.lian.meditake;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import java.util.List;

public class MedicationRepository {

    private MedicationsDao medicationsDao;
    private List<MedicationsTable> allMeds;

    public MedicationRepository(Application app) {
        MedicationsDataBase db = MedicationsDataBase.getInstance(app);
        medicationsDao = db.medicationsDao();
        allMeds = medicationsDao.getAllMedications();
    }

    public void insert(MedicationsTable medication) {
        new InsertAsyncTask(medicationsDao).execute(medication);
    }

    public void insertReminder(RemindersTable reminder) {
        new InsertReminderAsyncTask(medicationsDao).execute(reminder);
    }

    public void deleteAll() {
        new DeleteAllAsyncTask(medicationsDao).execute();
    }

    public void delete(MedicationsTable medication) {
        new DeleteAsyncTask(medicationsDao).execute(medication);
    }

    public List<MedicationsTable> getAllMedications() {
        return allMeds;
    }


    private static class InsertAsyncTask extends AsyncTask<MedicationsTable, Void, Void> {
        private MedicationsDao medicationsDao;

        private InsertAsyncTask(MedicationsDao medicationsDao) {
            this.medicationsDao = medicationsDao;
        }

        @Override
        protected Void doInBackground(MedicationsTable... medicationsTables) {
            medicationsDao.insert(medicationsTables[0]);
            return null;
        }
    }

    private static class InsertReminderAsyncTask extends AsyncTask<RemindersTable, Void, Void> {
        private MedicationsDao medicationsDao;

        private InsertReminderAsyncTask(MedicationsDao medicationsDao) {
            this.medicationsDao = medicationsDao;
        }

        @Override
        protected Void doInBackground(RemindersTable... remindersTables) {
            medicationsDao.insertReminder(remindersTables[0]);
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private MedicationsDao medicationsDao;

        private DeleteAllAsyncTask(MedicationsDao medicationsDao) {
            this.medicationsDao = medicationsDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            medicationsDao.deleteAll();
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<MedicationsTable, Void, Void> {
        private MedicationsDao medicationsDao;

        private DeleteAsyncTask(MedicationsDao medicationsDao) {
            this.medicationsDao = medicationsDao;
        }

        @Override
        protected Void doInBackground(MedicationsTable... medicationsTables) {
            medicationsDao.deleteMedication(medicationsTables[0]);
            return null;
        }
    }
}
