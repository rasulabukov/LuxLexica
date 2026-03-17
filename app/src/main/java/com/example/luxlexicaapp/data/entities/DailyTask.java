package com.example.luxlexicaapp.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "daily_tasks")
public class DailyTask {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String description;
    public int rewardExperience;
    public String conditionType;
    public int targetValue;
    public int currentValue;
    public boolean isCompleted;
    public long lastResetDate;

    public DailyTask(String description, int rewardExperience, String conditionType, int targetValue) {
        this.description = description;
        this.rewardExperience = rewardExperience;
        this.conditionType = conditionType;
        this.targetValue = targetValue;
        this.currentValue = 0;
        this.isCompleted = false;
        this.lastResetDate = System.currentTimeMillis();
    }
}
