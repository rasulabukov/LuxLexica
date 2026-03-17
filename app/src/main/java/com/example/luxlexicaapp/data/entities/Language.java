package com.example.luxlexicaapp.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "languages")
public class Language {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String code;
    public String name;

    public Language(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
