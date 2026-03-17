package com.example.luxlexicaapp.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.luxlexicaapp.data.entities.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users WHERE id = :userId")
    LiveData<User> getUserById(int userId);

    @Query("SELECT * FROM users WHERE id = 1") // Main user
    LiveData<User> getMainUser();

    @Query("SELECT * FROM users WHERE id = 1")
    User getMainUserSync();

    @Query("SELECT * FROM users WHERE id != 1 ORDER BY experience DESC LIMIT 10")
    LiveData<List<User>> getCommunityUsers();

    @Query("SELECT * FROM users ORDER BY experience DESC")
    LiveData<List<User>> getAllUsersByExperience();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Update
    void update(User user);
}
