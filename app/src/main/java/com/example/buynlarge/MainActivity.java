package com.example.buynlarge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
    private SharedPreferences mPreferences = null;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mMainBinding.getRoot();

        setContentView(view);

        getDatabase();

        checkForUser();

        addUserToPreference(mUserId);
        loginUser(mUserId);

    }

    private void loginUser(int userId) {
        mUser = mUserDAO.getUserByUserId(userId);

        invalidateOptionsMenu();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(mUser != null){
            MenuItem item = menu.findItem(R.id.logout_menu);
            item.setTitle(mUser.getUsername());
        }

        return super.onPrepareOptionsMenu(menu);
    }

    private void addUserToPreference(int userId) {
        if(mPreferences == null){
            getPrefs();
        }
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(USER_ID_KEY, userId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout_menu:
                logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

        if(mPreferences == null) {
            getPrefs();
        }
        mUserId = mPreferences.getInt(USER_ID_KEY, -1);

        if(mUserId != -1){
            return;
        }

        //Do we have any users at all?
        List<User> users = mUserDAO.getAllUsers();
        if(users.size() <= 0){
            User defaultUser = new User("Rettro","password123");
            User altUser = new User("Jerrett","password123");
            mUserDAO.insert(defaultUser,altUser);
        }

        Intent intent = LoginActivity.getIntent(this);
        startActivity(intent);
    }

    private void getPrefs() {
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    private void clearUserFromIntent(){
        getIntent().putExtra(USER_ID_KEY, -1);
    }

    private void clearUserFromPref(){
        addUserToPreference(-1);
    }

    private void logoutUser(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage(R.string.logout);

        alertBuilder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearUserFromIntent();
                        clearUserFromPref();
                        mUserId = -1;
                        checkForUser();
                        Toast.makeText(MainActivity.this, "Successfully signed out", Toast.LENGTH_SHORT).show();
                    }
                });
        alertBuilder.setNegativeButton(getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "Did not sign out", Toast.LENGTH_SHORT).show();
                    }
                });
        alertBuilder.create().show();
    }

    public static Intent getIntent(Context context, int userId){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
        
        return intent;
    }
}