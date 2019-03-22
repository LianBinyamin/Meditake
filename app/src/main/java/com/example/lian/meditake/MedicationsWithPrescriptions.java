package com.example.lian.meditake;

import android.arch.persistence.room.Embedded;

class MedicationsWithPrescriptions {

    @Embedded
    public MedicationsTable medication;

    @Embedded
    public PrescriptionReminder prescriptionReminder;
}
