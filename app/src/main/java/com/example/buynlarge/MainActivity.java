package com.example.buynlarge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.buynlarge.DB.AppDataBase;
import com.example.buynlarge.DB.UserDAO;
import com.example.buynlarge.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.buynlarge.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.buynlarge.PREFERENCES_KEY";
    ActivityMainBinding mMainBinding;
    private int mUserId;
    private UserDAO mUserDAO;
    private List<User> mUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mMainBinding.getRoot();

        setContentView(view);

        getDatabase();

        checkForUser();
    }

    private void getDatabase(){
        mUserDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME).allowMainThreadQueries().build().UserDAO();
    }

    private void checkForUser() {
        //Do we have a user in the intent?
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);

        //Do we have a user in the preferences?
        if(mUserId != -1){
            return;
        }

        SharedPreferences preferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        mUserId = preferences.getInt(USER_ID_KEY, -1);

        if(mUserId != -1){
            return;
        }

        //Do we have any users at all?
        List<User> users = mUserDAO.getAllUsers();
        if(users.size() <= 0){
            User defaultUser = new User("Rettro","password123");
            mUserDAO.insert(defaultUser);
        }

        Intent intent = LoginActivity.getIntent(this);
        startActivity(intent);
    }

    public static Intent getIntent(Context context, int userId){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
        
        return intent;
    }
}