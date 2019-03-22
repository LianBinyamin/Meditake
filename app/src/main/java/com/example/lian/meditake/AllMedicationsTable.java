package com.example.lian.meditake;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "medications_info")
public class AllMedicationsTable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;

    public AllMedicationsTable(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
