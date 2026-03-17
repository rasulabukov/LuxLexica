package com.example.luxlexicaapp.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "modules")
public class Module {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int languageId;
    public int levelId;
    public String name;
    public String description;

    public Module(int languageId, int levelId, String name, String description) {
        this.languageId = languageId;
        this.levelId = levelId;
        this.name = name;
        this.description = description;
    }
}
