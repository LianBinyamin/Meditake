package com.example.lian.meditake;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import java.util.List;

@Dao
public interface MedicationsDao {

    @Query("SELECT * FROM meds_table ORDER BY medName ASC")
    List<MedicationsTable> getAllMedications();

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    void insert(MedicationsTable medication);

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    void insertReminder(RemindersTable reminder);

    @Delete
    void deleteMedication(MedicationsTable medication);

    @Query("SELECT * FROM meds_table WHERE medName = :name")
    MedicationsTable getMedicationByName(String name);

    @Query("DELETE FROM meds_table WHERE med_id = :id")
    void deleteFromMedicationsById(int id);

    @Query("DELETE FROM reminders_table WHERE medication_id = :id")
    void deleteFroRemindersById(int id);

    @Query("DELETE FROM prescription_table WHERE medId = :id")
    void deleteFromPrescriptionsById(int id);

    @Query("DELETE FROM meds_table")
    void deleteAll();

    @Query("SELECT * FROM reminders_table WHERE medication_id = :medId")
    List<RemindersTable> getAllRemindersByMedicationId(int medId);

    @Query("SELECT * FROM reminders_table INNER JOIN meds_table ON reminders_table.medication_id = meds_table.med_id "
            + "WHERE reminders_table.day LIKE :theDay")
    List<MedicationsWithReminders> getMedicationsWithRemindersByDay(String theDay);

    @Query("SELECT * FROM reminders_table INNER JOIN meds_table ON reminders_table.medication_id = meds_table.med_id" +
            " WHERE medName LIKE :name")
    List<MedicationsWithReminders> getAllMedicationsWithRemindersByName(String name);

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    void insertPrescriptionReminder(PrescriptionReminder prescriptionReminder);

    @Query("SELECT * FROM prescription_table INNER JOIN meds_table ON prescription_table.medId = meds_table.med_id")
    List<MedicationsWithPrescriptions> getAllPrescriptionReminders();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMedication(AllMedicationsTable medication);

    @Query("SELECT name FROM medications_info WHERE name LIKE :name")
    List<String> getMedicationsTableByPrefix(String name);

    @Query("SELECT * FROM medications_info")
    List<AllMedicationsTable> getAllMedicationsTable();

    @Query("SELECT id FROM medications_info WHERE name LIKE :name")
    int getMedicationIdByName(String name);

    @Query("SELECT * FROM prescription_table WHERE medId = :medicationId")
    PrescriptionReminder getPrescriptionByMedicationId(int medicationId);

    @Query("UPDATE prescription_table SET amountDif = :amount WHERE medId = :medicationId")
    void updateDifAmountPrescription(double amount, int medicationId);

    @Query("UPDATE meds_table SET isPrescription = :prescription WHERE med_id = :medicationId")
    void updateIsPrescriptionByMedicationId(boolean prescription, int medicationId);

}
