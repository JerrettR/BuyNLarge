package com.example.buynlarge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.buynlarge.DB.AppDataBase;
import com.example.buynlarge.DB.UserDAO;
import com.example.buynlarge.databinding.ActivityCreateNewAccountBinding;
import com.example.buynlarge.databinding.ActivityLoginBinding;

public class CreateNewAccount extends AppCompatActivity {

    ActivityCreateNewAccountBinding mCreateBinding;
    private EditText mNewUsername_edittext;
    private EditText mNewPassword_edittext;
    private Button mRegister_button;
    private ImageButton mBackButton;
    private UserDAO mUserDAO;
    private int mUserId = -1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_account);

        mCreateBinding = ActivityCreateNewAccountBinding.inflate(getLayoutInflater());
        View view = mCreateBinding.getRoot();

        setContentView(view);

        mNewUsername_edittext = mCreateBinding.newUsernameInput;
        mNewPassword_edittext = mCreateBinding.newPasswordInput;
        mRegister_button = mCreateBinding.newRegister;
        mBackButton = mCreateBinding.backButton;
        mUserDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME).allowMainThreadQueries().build().getUserDAO();

        pressRegisterButton();

        pressBackButton();
    }

    private void registerNewUser(){
        String username = mNewUsername_edittext.getText().toString();
        String password = mNewPassword_edittext.getText().toString();
        boolean isAdmin = Boolean.parseBoolean(null);

        User log = new User(username,password,isAdmin);
        mUserDAO.insert(log);
        Toast.makeText(CreateNewAccount.this, "Account successfully created for: " + username, Toast.LENGTH_LONG).show();
        Intent intent = LoginActivity.getIntent(getApplicationContext());
        startActivity(intent);
    }

    private void pressRegisterButton(){
        mRegister_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });
    }

    private void pressBackButton() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LoginActivity.getIntent(getApplicationContext());
                startActivity(intent);
            }
        });
    }

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context, CreateNewAccount.class);
        return intent;
    }
}