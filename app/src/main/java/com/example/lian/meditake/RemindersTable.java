package com.example.lian.meditake;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "reminders_table")
public class RemindersTable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "reminder_id")
    private int id;

    @ColumnInfo(name = "medication_id")
    private int medicationId;

    private int hour;
    private int minutes;
    private double doses;
    private String day;

    public RemindersTable(int hour, int minutes, double doses, String day, int medicationId) {
        this.hour = hour;
        this.minutes = minutes;
        this.doses = doses;
        this.day = day;
        this.medicationId = medicationId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public double getDoses() {
        return doses;
    }

    public void setDoses(double doses) {
        this.doses = doses;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(int medicationId) {
        this.medicationId = medicationId;
    }
}
