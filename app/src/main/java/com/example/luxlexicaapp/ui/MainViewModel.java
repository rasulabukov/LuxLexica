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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainViewModel extends AndroidViewModel {

    private final AppDatabase db;
    private final LiveData<User> mainUser;
    private final MutableLiveData<Integer> selectedLanguageId = new MutableLiveData<>(1);
    private final MutableLiveData<Integer> selectedLevelId = new MutableLiveData<>(1);

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

            boolean userUpdated = false;

            for (DailyTask task : tasks) {
                if (task.isCompleted) continue;

                boolean shouldUpdate = false;
                switch (task.conditionType) {
                    case "LOGIN":
                        if (type.equals("LOGIN")) shouldUpdate = true;
                        break;
                    case "ANY_LESSON":
                        if (type.equals("THEORY") || type.equals("PRACTICE") || type.equals("TEST")) shouldUpdate = true;
                        break;
                    case "PRACTICE":
                        if (type.equals("PRACTICE")) shouldUpdate = true;
                        break;
                    case "TEST":
                        if (type.equals("TEST")) shouldUpdate = true;
                        break;
                    case "THEORY":
                        if (type.equals("THEORY")) shouldUpdate = true;
                        break;
                    case "TOTAL_5":
                        if (type.equals("THEORY") || type.equals("PRACTICE") || type.equals("TEST")) shouldUpdate = true;
                        break;
                    case "LANG_2":
                    case "FOCUS_1":
                    case "ALL_LANGS":
                    case "HARMONY":
                        // These require complex tracking, handled via helper
                        updateComplexTask(task, type);
                        break;
                }

                if (shouldUpdate) {
                    task.currentValue += increment;
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

    private void updateComplexTask(DailyTask task, String type) {
        task.currentValue += 1; 
        if (task.currentValue >= task.targetValue) {
            task.currentValue = task.targetValue;
            task.isCompleted = true;
            User user = db.userDao().getMainUserSync();
            user.experience += task.rewardExperience;
            db.userDao().update(user);
        }
        db.dailyTaskDao().update(task);
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
