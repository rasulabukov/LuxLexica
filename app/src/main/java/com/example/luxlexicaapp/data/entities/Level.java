package com.example.luxlexicaapp.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "levels")
public class Level {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String code;

    public Level(String code) {
        this.code = code;
    }
}
