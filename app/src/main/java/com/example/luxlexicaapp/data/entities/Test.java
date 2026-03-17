package com.example.luxlexicaapp.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tests")
public class Test {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int moduleId;

    public Test(int moduleId) {
        this.moduleId = moduleId;
    }
}
