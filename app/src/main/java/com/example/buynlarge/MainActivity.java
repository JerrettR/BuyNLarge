package com.example.buynlarge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.buynlarge.DB.AppDataBase;
import com.example.buynlarge.DB.UserDAO;
import com.example.buynlarge.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mMainBinding;
    private EditText mUsername_edittext;
    private EditText mPassword_edittext;
    private Button mSignIn_button;
    private TextView mCreateNewAccount_textview;
    private UserDAO mUserDAO;
    private List<User> mUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mMainBinding.getRoot();

        setContentView(view);

        mUsername_edittext = mMainBinding.usernameInput;
        mPassword_edittext = mMainBinding.passwordInput;
        mSignIn_button = mMainBinding.signInButton;
        mCreateNewAccount_textview = mMainBinding.createNewAccount;
        mUserDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME).allowMainThreadQueries().build().UserDAO();

        mSignIn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username_input = mUsername_edittext.getText().toString();
                String password_input = mPassword_edittext.getText().toString();
            }
        });

        mCreateNewAccount_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = CreateNewAccount.getIntent(getApplicationContext());
                startActivity(intent);
            }
        });
    }

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }
}