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
import android.widget.Toast;

import com.example.buynlarge.DB.AppDataBase;
import com.example.buynlarge.DB.UserDAO;
import com.example.buynlarge.databinding.ActivityLoginBinding;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding mLoginBinding;
    private EditText mUsername_edittext;
    private EditText mPassword_edittext;
    private Button mSignIn_button;
    private TextView mCreateNewAccount_textview;
    private UserDAO mUserDAO;
    private List<User> mUserList;
    private String mUsername;
    private String mPassword;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = mLoginBinding.getRoot();

        setContentView(view);

        mUsername_edittext = mLoginBinding.usernameInput;
        mPassword_edittext = mLoginBinding.passwordInput;
        mSignIn_button = mLoginBinding.signInButton;
        mCreateNewAccount_textview = mLoginBinding.createNewAccount;
        mUserDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME).allowMainThreadQueries().build().UserDAO();

        wireUpDisplay();

        getDatabase();

//        mSignIn_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String username_input = mUsername_edittext.getText().toString();
//                String password_input = mPassword_edittext.getText().toString();
//            }
//        });

        mCreateNewAccount_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = CreateNewAccount.getIntent(getApplicationContext());
                startActivity(intent);
            }
        });
    }

    private void wireUpDisplay(){
        mUsername_edittext = findViewById(R.id.username_input);
        mPassword_edittext = findViewById(R.id.password_input);
        mSignIn_button = findViewById(R.id.sign_in_button);

        mSignIn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValuesFromDisplay();
                if(checkForUserInDatabase()){
                    if(!validatePassword()){
                        Toast.makeText(LoginActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent = MainActivity.getIntent(getApplicationContext(),mUser.getUserId());
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private boolean validatePassword(){
        return mUser.getPassword().equals(mPassword);
    }

    private void getValuesFromDisplay(){
        mUsername = mUsername_edittext.getText().toString();
        mPassword = mPassword_edittext.getText().toString();
    }

    private boolean checkForUserInDatabase(){
        mUser = mUserDAO.getUserByUsername(mUsername);
        if(mUser == null){
            Toast.makeText(this, "No user " + mUsername + " found", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void getDatabase(){
        mUserDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME).allowMainThreadQueries().build().UserDAO();
    }

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }
}