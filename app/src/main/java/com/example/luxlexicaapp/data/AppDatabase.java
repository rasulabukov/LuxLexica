package com.example.luxlexicaapp.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.luxlexicaapp.data.dao.AppDao;
import com.example.luxlexicaapp.data.dao.DailyTaskDao;
import com.example.luxlexicaapp.data.dao.UserDao;
import com.example.luxlexicaapp.data.entities.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {
        User.class, Language.class, Level.class, Module.class,
        Lesson.class, Test.class, Question.class, UserProgress.class,
        Achievement.class, UserAchievement.class, DailyTask.class,
        PracticeQuestion.class
}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract AppDao appDao();
    public abstract DailyTaskDao dailyTaskDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "lux_lexica_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
