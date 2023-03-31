package com.example.buynlarge.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.buynlarge.User;

@Database(entities = {User.class}, version = 2)
@TypeConverters({DateTypeConverter.class})
public abstract class AppDataBase extends RoomDatabase {
    public static final String DATABASE_NAME = "User.db";
    public static final String USER_TABLE = "user_table";

    private static volatile AppDataBase instance;
    private static final Object LOCK = new Object();

    public abstract UserDAO UserDAO();

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
