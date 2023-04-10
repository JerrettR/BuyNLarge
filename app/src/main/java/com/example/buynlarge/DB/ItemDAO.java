package com.example.buynlarge.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.buynlarge.Item;

import java.util.List;

@Dao
public interface ItemDAO {

    @Insert
    void insert(Item... items);

    @Update
    void update(Item... items);

    @Delete
    void delete(Item item);

    @Query("SELECT * FROM " + AppDataBase.ITEM_TABLE)
    List<Item> getItemLogs();

    @Query("SELECT * FROM " + AppDataBase.ITEM_TABLE + " WHERE mItemId = :itemId ORDER BY mItemId")
    List<Item> getItemById(int itemId);

    @Query("SELECT * FROM " + AppDataBase.ITEM_TABLE + " ORDER BY mItemId")
    List<Item> getAllItems();

//    @Query("SELECT mItemName FROM " + AppDataBase.ITEM_TABLE + " ORDER BY mItemId")
//    List<Item> getAllItemNames();

    @Query("SELECT * FROM " + AppDataBase.ITEM_TABLE + " WHERE mItemName = :itemName")
    Item getItemByItemName(String itemName);
}
