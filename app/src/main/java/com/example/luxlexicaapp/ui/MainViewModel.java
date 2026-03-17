package com.example.luxlexicaapp.ui;

import android.app.Application;
import android.text.format.DateUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.luxlexicaapp.data.AppDatabase;
import com.example.luxlexicaapp.data.entities.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainViewModel extends AndroidViewModel {

    private final AppDatabase db;
    private final LiveData<User> mainUser;
    private final MutableLiveData<Integer> selectedLanguageId = new MutableLiveData<>(1);
    private final MutableLiveData<Integer> selectedLevelId = new MutableLiveData<>(1);

    private final Set<Integer> languagesUsedToday = new HashSet<>();
    private final Map<Integer, Integer> lessonsCountPerLanguageToday = new HashMap<>();
    private final Set<String> activitiesToday = new HashSet<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getDatabase(application);
        mainUser = db.userDao().getMainUser();
        checkAndResetDailyTasks();
    }

    public LiveData<User> getMainUser() {
        return mainUser;
    }

    public void updateUserName(String name) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            User user = db.userDao().getMainUserSync();
            if (user != null) {
                user.name = name;
                db.userDao().update(user);
            }
        });
    }

    private void checkAndResetDailyTasks() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<DailyTask> tasks = db.dailyTaskDao().getDailyTasksSync();
            if (tasks.isEmpty()) return;

            long lastReset = tasks.get(0).lastResetDate;
            if (!DateUtils.isToday(lastReset)) {
                languagesUsedToday.clear();
                lessonsCountPerLanguageToday.clear();
                activitiesToday.clear();
                for (DailyTask task : tasks) {
                    task.currentValue = 0;
                    task.isCompleted = false;
                    task.lastResetDate = System.currentTimeMillis();
                    db.dailyTaskDao().update(task);
                }
            }
        });
    }

    public void updateDailyTaskProgress(String type, int increment) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<DailyTask> tasks = db.dailyTaskDao().getDailyTasksSync();
            User user = db.userDao().getMainUserSync();
            if (user == null) return;

            Integer currentLangId = selectedLanguageId.getValue();
            boolean userUpdated = false;

            if (type.equals("LESSON_COMPLETED") && currentLangId != null) {
                languagesUsedToday.add(currentLangId);
                int count = lessonsCountPerLanguageToday.getOrDefault(currentLangId, 0) + increment;
                lessonsCountPerLanguageToday.put(currentLangId, count);
            } else if (type.equals("THEORY") || type.equals("PRACTICE") || type.equals("TEST")) {
                activitiesToday.add(type);
            }

            for (DailyTask task : tasks) {
                if (task.isCompleted) continue;

                boolean shouldIncrement = false;
                int customIncrement = increment;

                switch (task.conditionType) {
                    case "LOGIN":
                        if (type.equals("LOGIN")) shouldIncrement = true;
                        break;
                    case "ANY_LESSON":
                        if (type.equals("LESSON_COMPLETED")) shouldIncrement = true;
                        break;
                    case "PRACTICE":
                        if (type.equals("PRACTICE")) shouldIncrement = true;
                        break;
                    case "TEST":
                        if (type.equals("TEST")) shouldIncrement = true;
                        break;
                    case "THEORY":
                        if (type.equals("THEORY")) shouldIncrement = true;
                        break;
                    case "TOTAL_5":
                        if (type.equals("LESSON_COMPLETED")) shouldIncrement = true;
                        break;
                    case "LANG_2":
                        if (type.equals("LESSON_COMPLETED")) {
                            task.currentValue = languagesUsedToday.size();
                            if (task.currentValue >= task.targetValue) {
                                task.isCompleted = true;
                                user.experience += task.rewardExperience;
                                userUpdated = true;
                            }
                            db.dailyTaskDao().update(task);
                        }
                        continue;
                    case "FOCUS_1":
                        if (type.equals("LESSON_COMPLETED")) {
                            int maxInOneLang = 0;
                            for (int count : lessonsCountPerLanguageToday.values()) {
                                if (count > maxInOneLang) maxInOneLang = count;
                            }
                            task.currentValue = Math.min(maxInOneLang, task.targetValue);
                            if (task.currentValue >= task.targetValue) {
                                task.isCompleted = true;
                                user.experience += task.rewardExperience;
                                userUpdated = true;
                            }
                            db.dailyTaskDao().update(task);
                        }
                        continue;
                    case "ALL_LANGS":
                        if (type.equals("LESSON_COMPLETED")) {
                            task.currentValue = languagesUsedToday.size();
                            if (task.currentValue >= task.targetValue) {
                                task.isCompleted = true;
                                user.experience += task.rewardExperience;
                                userUpdated = true;
                            }
                            db.dailyTaskDao().update(task);
                        }
                        continue;
                    case "HARMONY":
                        if (type.equals("THEORY") || type.equals("PRACTICE") || type.equals("TEST")) {
                            task.currentValue = activitiesToday.size();
                            if (task.currentValue >= task.targetValue) {
                                task.isCompleted = true;
                                user.experience += task.rewardExperience;
                                userUpdated = true;
                            }
                            db.dailyTaskDao().update(task);
                        }
                        continue;
                }

                if (shouldIncrement) {
                    task.currentValue += customIncrement;
                    if (task.currentValue >= task.targetValue) {
                        task.currentValue = task.targetValue;
                        task.isCompleted = true;
                        user.experience += task.rewardExperience;
                        userUpdated = true;
                    }
                    db.dailyTaskDao().update(task);
                }
            }
            if (userUpdated) db.userDao().update(user);
        });
    }

    public LiveData<List<Language>> getLanguages() {
        return db.appDao().getLanguages();
    }

    public LiveData<List<Level>> getLevels() {
        return db.appDao().getLevels();
    }

    public void selectLanguage(int id) {
        selectedLanguageId.setValue(id);
    }

    public void selectLevel(int id) {
        selectedLevelId.setValue(id);
    }

    public LiveData<Integer> getSelectedLanguageId() { return selectedLanguageId; }
    public LiveData<Integer> getSelectedLevelId() { return selectedLevelId; }

    public LiveData<List<Module>> getFilteredModules() {
        return Transformations.switchMap(selectedLanguageId, langId -> 
            Transformations.switchMap(selectedLevelId, levelId -> {
                if (langId == -1 || levelId == -1) {
                    return new MutableLiveData<>();
                }
                return db.appDao().getModules(langId, levelId);
            })
        );
    }

    public LiveData<List<UserProgress>> getAllUserProgress() {
        return db.appDao().getAllUserProgress(1);
    }

    public LiveData<List<User>> getCommunityUsers() {
        return db.userDao().getCommunityUsers();
    }

    public LiveData<List<User>> getAllUsersByExperience() {
        return db.userDao().getAllUsersByExperience();
    }

    public LiveData<List<DailyTask>> getDailyTasks() {
        return db.dailyTaskDao().getDailyTasks();
    }

    public LiveData<List<Achievement>> getAllAchievements() {
        return db.appDao().getAllAchievements();
    }
}
