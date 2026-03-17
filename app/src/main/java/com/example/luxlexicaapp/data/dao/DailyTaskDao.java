package com.example.luxlexicaapp.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.luxlexicaapp.data.entities.DailyTask;

import java.util.List;

@Dao
public interface DailyTaskDao {
    @Query("SELECT * FROM daily_tasks")
    LiveData<List<DailyTask>> getDailyTasks();

    @Query("SELECT * FROM daily_tasks")
    List<DailyTask> getDailyTasksSync();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DailyTask dailyTask);

    @Update
    void update(DailyTask dailyTask);

    @Query("DELETE FROM daily_tasks")
    void deleteAll();
}
