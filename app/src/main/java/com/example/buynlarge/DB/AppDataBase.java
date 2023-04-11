package com.example.buynlarge.DB;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.buynlarge.Item;
import com.example.buynlarge.Order;
import com.example.buynlarge.User;

@Database(entities = {User.class,Item.class, Order.class}, version = 12)
@TypeConverters({DateTypeConverter.class})
public abstract class AppDataBase extends RoomDatabase {
    public static final String DATABASE_NAME = "BUYNLARGE_DATABASE";
    public static final String USER_TABLE = "USER_TABLE";
    public static final String ITEM_TABLE = "ITEM_TABLE";
    public static final String ORDER_TABLE = "ORDER_TABLE";

    private static volatile AppDataBase instance;
    private static final Object LOCK = new Object();

    public abstract UserDAO getUserDAO();
    public abstract ItemDAO getItemDAO();
    public abstract OrderDAO getOrderDAO();

    public static AppDataBase getInstance(Context context){
        if(instance == null){
            synchronized(LOCK){
                if(instance == null){
                    instance = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, DATABASE_NAME).fallbackToDestructiveMigration().addCallback(roomCallback).build();
                }
            }
        }
        return instance;
    }

    private static AppDataBase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{
        private UserDAO userDAO;

        private PopulateDbAsyncTask(AppDataBase db){
            userDAO = db.getUserDAO();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
