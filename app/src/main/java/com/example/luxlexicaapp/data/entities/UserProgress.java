package com.example.luxlexicaapp.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_progress")
public class UserProgress {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userId;
    public int moduleId;
    public boolean isPracticeCompleted;
    public boolean isTestCompleted;
    public boolean isCompleted;

    public UserProgress(int userId, int moduleId) {
        this.userId = userId;
        this.moduleId = moduleId;
        this.isPracticeCompleted = false;
        this.isTestCompleted = false;
        this.isCompleted = false;
    }
}
