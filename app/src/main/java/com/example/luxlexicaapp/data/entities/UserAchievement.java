package com.example.luxlexicaapp.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_achievements")
public class UserAchievement {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userId;
    public int achievementId;
    public boolean isUnlocked;
    public long unlockDate;

    public UserAchievement(int userId, int achievementId, boolean isUnlocked, long unlockDate) {
        this.userId = userId;
        this.achievementId = achievementId;
        this.isUnlocked = isUnlocked;
        this.unlockDate = unlockDate;
    }
}
