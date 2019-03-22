package com.example.lian.meditake;

import android.arch.persistence.room.Embedded;

public class MedicationsWithReminders {

    @Embedded
    public MedicationsTable medication;

    @Embedded
    public RemindersTable reminder;

}
