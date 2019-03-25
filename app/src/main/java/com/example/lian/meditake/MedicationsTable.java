package com.example.lian.meditake;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity (tableName = "meds_table")
public class MedicationsTable {

    @PrimaryKey
    @ColumnInfo(name = "med_id")
    private int id;
    private boolean isPrescription=false;
    private String medName;
    private double mg;
    private int amountRemindersPerDay;
    private String days;

    public MedicationsTable(int id, String medName, double mg, int amountRemindersPerDay, String days) {
        this.id = id;
        this.medName = medName;
        this.mg = mg;
        this.amountRemindersPerDay = amountRemindersPerDay;
        this.days = days;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMedName() {
        return medName;
    }

    public double getMg() {
        return mg;
    }

    public int getAmountRemindersPerDay() {
        return amountRemindersPerDay;
    }

    public boolean isPrescription() {
        return isPrescription;
    }

    public void setPrescription(boolean prescription) {
        isPrescription = prescription;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }
}
