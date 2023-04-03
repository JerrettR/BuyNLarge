package com.example.buynlarge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

    ActivityUsersBinding mUsersBinding;
    private UserDAO mUserDAO;
    private List<User> mUserList;
    private TextView mUsersList_TextView;
    private Spinner mUserSpinner;
    private Button mDeleteUser_Button;
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

        displayUsers();

        displayUsersSpinner();
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
                            Intent intent = UsersActivity.getIntent(getApplicationContext());
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

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context, UsersActivity.class);
        return intent;
    }
}