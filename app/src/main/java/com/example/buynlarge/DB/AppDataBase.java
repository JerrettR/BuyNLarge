package com.example.buynlarge.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.buynlarge.BuyNLarge;

@Database(entities = {BuyNLarge.class}, version = 2)
@TypeConverters({DateTypeConverter.class})
public abstract class AppDataBase extends RoomDatabase {
    public static final String DATABASE_NAME = "BuyNLarge.db";
    public static final String BUYNLARGE_TABLE = "buynlarge_table";

    private static volatile AppDataBase instance;
    private static final Object LOCK = new Object();

    public abstract BuyNLargeDAO BuyNLargeDAO();

    public static AppDataBase getInstance(Context context){
        if(instance == null){
            synchronized(LOCK){
                if(instance == null){
                    instance = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, DATABASE_NAME).build();
                }
            }
        }
        return instance;
    }
}
