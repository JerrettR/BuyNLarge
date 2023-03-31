package com.example.buynlarge.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.buynlarge.BuyNLarge;

import java.util.List;

@Dao
public interface BuyNLargeDAO {

    @Insert
    void insert(BuyNLarge... buyNLarges);

    @Update
    void update(BuyNLarge... buyNLarges);

    @Delete
    void delete(BuyNLarge buyNLarges);

    @Query("SELECT * FROM " + AppDataBase.BUYNLARGE_TABLE)
    List<BuyNLarge> getBnlLogs();

    @Query("SELECT * FROM " + AppDataBase.BUYNLARGE_TABLE + " WHERE mLogId = :logId")
    List<BuyNLarge> getLogById(int logId);
}
