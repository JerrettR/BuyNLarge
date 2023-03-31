package com.example.buynlarge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.buynlarge.DB.AppDataBase;
import com.example.buynlarge.DB.UserDAO;
import com.example.buynlarge.databinding.ActivityCreateNewAccountBinding;
import com.example.buynlarge.databinding.ActivityMainBinding;

public class CreateNewAccount extends AppCompatActivity {

    ActivityCreateNewAccountBinding mCreateBinding;
    EditText mNewUsername_edittext;
    EditText mNewPassword_edittext;
    Button mRegister_button;
    UserDAO mUserDAO;



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
        mUserDAO = Room.databaseBuilder(this, AppDataBase .class, AppDataBase.DATABASE_NAME).allowMainThreadQueries().build().UserDAO();

        mRegister_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });
    }

    private void registerNewUser(){
        String username = mNewUsername_edittext.getText().toString();
        String password = mNewPassword_edittext.getText().toString();

        User log = new User(username,password);
        mUserDAO.insert(log);
    }

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context, CreateNewAccount.class);
        return intent;
    }
}