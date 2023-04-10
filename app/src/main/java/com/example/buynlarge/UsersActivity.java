package com.example.buynlarge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buynlarge.DB.AppDataBase;
import com.example.buynlarge.DB.UserDAO;
import com.example.buynlarge.databinding.ActivityUsersBinding;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {
    private static final String USER_ID_KEY = "com.example.buynlarge.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.buynlarge.PREFERENCES_KEY";
    private int mUserId = -1;
    private User mUser;
    private SharedPreferences mPreferences = null;
    ActivityUsersBinding mUsersBinding;
    private UserDAO mUserDAO;
    private List<User> mUserList;
    private TextView mUsersList_TextView;
    private Spinner mUserSpinner;
    private Button mDeleteUser_Button;
    private ImageButton mBackButton;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        mUsersBinding = ActivityUsersBinding.inflate(getLayoutInflater());
        View view = mUsersBinding.getRoot();
        setContentView(view);

        mUserDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME).allowMainThreadQueries().build().getUserDAO();

        mUsersList_TextView = mUsersBinding.usersListTextView;
        mUserSpinner = mUsersBinding.usersSpinner;
        mDeleteUser_Button = mUsersBinding.deleteUserButton;
        mBackButton = mUsersBinding.backButton;

        getDatabase();

        checkForUser();

        addUserToPreference(mUserId);

        loginUser(mUserId);

        displayUsers();

        displayUsersSpinner();

        pressBackButton();
    }

    private void getDatabase(){
        mUserDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME).allowMainThreadQueries().build().getUserDAO();
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
    }

    private void addUserToPreference(int userId) {
        if(mPreferences == null){
            getPrefs();
        }
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(USER_ID_KEY, userId);
    }

    private void getPrefs() {
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    private void loginUser(int userId) {
        mUser = mUserDAO.getUserByUserId(userId);

        invalidateOptionsMenu();
    }

    private void displayUsers(){
        StringBuilder sb = new StringBuilder();
        for(User user : mUserDAO.getAllUsers()){
            sb.append(user);
        }
        mUsersList_TextView.setText(sb);
        mUsersList_TextView.setMovementMethod(new ScrollingMovementMethod());
    }

    private void displayUsersSpinner(){
        mUserList = mUserDAO.getAllUsers();
        System.out.println("mUserList" + mUserList);
        if(! mUserList.isEmpty()){
            ArrayAdapter<User> adapter = new ArrayAdapter<User>(this, android.R.layout.simple_expandable_list_item_1, mUserList);
            adapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
            mUserSpinner.setAdapter(adapter);

            mUserSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedUser = parent.getItemAtPosition(position).toString();

                    String[] wordArray = selectedUser.trim().split("\\s+", 0);
                    String word = "";
                    for(int i=0; i<wordArray.length; i++) {
                        word = wordArray[i];
                        System.out.println("wordArray[" + i + "]: " + wordArray[i]);
                    }
                    Toast.makeText(parent.getContext(), "Selected: " + selectedUser, Toast.LENGTH_LONG).show();
                    User user = mUserDAO.getUserByUsername(wordArray[4]);
                    System.out.println("User: " + user);

                    mDeleteUser_Button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mUserDAO.delete(user);
                            Toast.makeText(UsersActivity.this, "Deleted user: " + selectedUser, Toast.LENGTH_LONG).show();
                            Intent intent = UsersActivity.getIntent(getApplicationContext(),mUser.getUserId());
                            startActivity(intent);
                        }
                    });
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private void pressBackButton() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("mUser: " + mUser);
                Intent intent = AdminActivity.getIntent(getApplicationContext(),mUser.getUserId());
                startActivity(intent);
            }
        });
    }

    public static Intent getIntent(Context context, int userId){
        Intent intent = new Intent(context, UsersActivity.class);
        intent.putExtra(USER_ID_KEY, userId);

        return intent;
    }
}