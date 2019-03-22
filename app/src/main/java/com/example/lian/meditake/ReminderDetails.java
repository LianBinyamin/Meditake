package com.example.lian.meditake;

public class ReminderDetails {

    private int hour;
    private int minutes;
    private double doses;
    private int keyId;

    public ReminderDetails(int hour, int minutes) {
        this.hour = hour;
        this.minutes = minutes;
        this.doses = 1.0;
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

    public int getKeyId() {
        return keyId;
    }

    public void setKeyId(int keyId) {
        this.keyId = keyId;
    }
}
