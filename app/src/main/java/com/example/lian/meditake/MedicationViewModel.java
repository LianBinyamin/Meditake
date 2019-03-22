package com.example.lian.meditake;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import java.util.List;

public class MedicationViewModel extends AndroidViewModel {
    private MedicationRepository medicationRepository;
    private List<MedicationsTable> allMeds;


    public MedicationViewModel(@NonNull Application application) {
        super(application);
        medicationRepository = new MedicationRepository(application);
        allMeds = medicationRepository.getAllMedications();
    }

    public void insert(MedicationsTable medication) {
        medicationRepository.insert(medication);
    }

    public void insertReminder(RemindersTable reminder) {
        medicationRepository.insertReminder(reminder);
    }

    public void delete(MedicationsTable medication) {
        medicationRepository.delete(medication);
    }

    public void deleteAll() {
        medicationRepository.deleteAll();
    }

    public List<MedicationsTable> getAllMedications() {
        return allMeds;
    }
}
