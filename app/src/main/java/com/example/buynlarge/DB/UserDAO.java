package com.example.buynlarge.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.buynlarge.User;

import java.util.List;

@Dao
public interface UserDAO {

    @Insert
    void insert(User... users);

    @Update
    void update(User... users);

    @Delete
    void delete(User users);

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE)
    List<User> getBnlLogs();

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE + " WHERE mUserId = :userId")
    List<User> getUserById(int userId);
}
