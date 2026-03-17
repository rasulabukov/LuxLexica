package com.example.luxlexicaapp.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String avatarUri;
    public int experience;
    public int streak;
    public long lastLoginDate;
    public int lessonsCompleted;
    public int modulesCompleted;
    public int totalCorrectAnswers;

    public User(String name) {
        this.name = name;
        this.experience = 0;
        this.streak = 0;
        this.lastLoginDate = System.currentTimeMillis();
        this.lessonsCompleted = 0;
        this.modulesCompleted = 0;
        this.totalCorrectAnswers = 0;
    }
}
