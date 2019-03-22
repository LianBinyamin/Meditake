package com.example.lian.meditake;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity (tableName = "prescription_table")
public class PrescriptionReminder {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int medId;
    private int hour;
    private int minutes;
    private int startAmount;
    private int reminderAmount;
    private double amountDif;

    public PrescriptionReminder(int medId, int hour, int minutes, int startAmount, int reminderAmount,
                                double amountDif) {
        this.medId = medId;
        this.hour = hour;
        this.minutes = minutes;
        this.startAmount = startAmount;
        this.reminderAmount = reminderAmount;
        this.amountDif = amountDif;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMedId() {
        return medId;
    }

    public void setMedId(int medId) {
        this.medId = medId;
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

    public int getStartAmount() {
        return startAmount;
    }

    public void setStartAmount(int startAmount) {
        this.startAmount = startAmount;
    }

    public int getReminderAmount() {
        return reminderAmount;
    }

    public void setReminderAmount(int reminderAmount) {
        this.reminderAmount = reminderAmount;
    }

    public double getAmountDif() {
        return amountDif;
    }

    public void setAmountDif(double amountDif) {
        this.amountDif = amountDif;
    }
}
