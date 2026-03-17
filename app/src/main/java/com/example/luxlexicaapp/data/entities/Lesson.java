package com.example.luxlexicaapp.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "lessons")
public class Lesson {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int moduleId;
    public String content;

    public Lesson(int moduleId, String content) {
        this.moduleId = moduleId;
        this.content = content;
    }
}
