package com.example.luxlexicaapp.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "achievements")
public class Achievement {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public String description;
    public String condition;
    public String iconName;

    public Achievement(String title, String description, String condition, String iconName) {
        this.title = title;
        this.description = description;
        this.condition = condition;
        this.iconName = iconName;
    }
}
