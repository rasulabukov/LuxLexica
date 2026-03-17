package com.example.luxlexicaapp.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.luxlexicaapp.data.entities.*;

import java.util.List;

@Dao
public interface AppDao {
    @Query("SELECT * FROM languages")
    LiveData<List<Language>> getLanguages();

    @Query("SELECT * FROM levels")
    LiveData<List<Level>> getLevels();

    @Query("SELECT * FROM modules WHERE languageId = :langId AND levelId = :levelId")
    LiveData<List<Module>> getModules(int langId, int levelId);

    @Query("SELECT * FROM lessons WHERE moduleId = :moduleId")
    LiveData<Lesson> getLessonByModule(int moduleId);

    @Query("SELECT * FROM tests WHERE moduleId = :moduleId")
    LiveData<Test> getTestByModule(int moduleId);

    @Query("SELECT * FROM questions WHERE testId = :testId")
    LiveData<List<Question>> getQuestionsByTest(int testId);

    @Query("SELECT * FROM practice_questions WHERE moduleId = :moduleId")
    LiveData<List<PracticeQuestion>> getPracticeByModule(int moduleId);

    @Query("SELECT * FROM user_progress WHERE userId = :userId AND moduleId = :moduleId")
    LiveData<UserProgress> getUserProgress(int userId, int moduleId);

    @Query("SELECT * FROM user_progress WHERE userId = :userId AND moduleId = :moduleId")
    UserProgress getUserProgressSync(int userId, int moduleId);

    @Query("SELECT * FROM user_progress WHERE userId = :userId")
    LiveData<List<UserProgress>> getAllUserProgress(int userId);

    @Query("SELECT * FROM achievements")
    LiveData<List<Achievement>> getAllAchievements();

    @Query("SELECT * FROM user_achievements WHERE userId = :userId")
    LiveData<List<UserAchievement>> getUserAchievements(int userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertLanguage(Language language);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertLevel(Level level);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertModule(Module module);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertLesson(Lesson lesson);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertTest(Test test);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertQuestion(Question question);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPracticeQuestion(PracticeQuestion question);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertAchievement(Achievement achievement);

    @Update
    void updateUserProgress(UserProgress userProgress);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserProgress(UserProgress userProgress);

    @Update
    void updateUserAchievement(UserAchievement userAchievement);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserAchievement(UserAchievement userAchievement);
}
