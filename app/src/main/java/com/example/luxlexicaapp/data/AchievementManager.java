package com.example.luxlexicaapp.data;

import android.content.Context;
import com.example.luxlexicaapp.data.entities.Achievement;
import com.example.luxlexicaapp.data.entities.User;
import com.example.luxlexicaapp.data.entities.UserAchievement;
import java.util.List;

public class AchievementManager {

    public static void checkAchievements(Context context, AppDatabase db) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            User user = db.userDao().getMainUserSync();
            if (user == null) return;

            
            checkAndUnlock(db, user, "lessons_5", user.lessonsCompleted >= 5);
            checkAndUnlock(db, user, "correct_50", user.totalCorrectAnswers >= 50);
            checkAndUnlock(db, user, "streak_7", user.streak >= 7);
        });
    }

    private static void checkAndUnlock(AppDatabase db, User user, String condition, boolean isMet) {
        if (!isMet) return;

    }
}
