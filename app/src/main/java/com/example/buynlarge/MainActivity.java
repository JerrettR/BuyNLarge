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
import com.example.buynlarge.DB.BuyNLargeDAO;
import com.example.buynlarge.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mMainBinding;
    TextView mMainDisplay_textview;
    EditText mUsername_edittext;
    EditText mPassword_edittext;
    Button mSignIn_button;
    TextView mCreateNewAccount_textview;
    BuyNLargeDAO mBuyNLargeDAO;

    List<BuyNLarge> mBuyNLargeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mMainBinding.getRoot();

        setContentView(view);

        mMainDisplay_textview = mMainBinding.mainDisplay;
        mUsername_edittext = mMainBinding.usernameInput;
        mPassword_edittext = mMainBinding.passwordInput;
        mSignIn_button = mMainBinding.signInButton;
        mCreateNewAccount_textview = mMainBinding.createNewAccount;
        mBuyNLargeDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME).allowMainThreadQueries().build().BuyNLargeDAO();

        refreshDisplay();

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

    private void refreshDisplay(){
        mBuyNLargeList = mBuyNLargeDAO.getBnlLogs();
        if(! mBuyNLargeList.isEmpty()){
            StringBuilder sb = new StringBuilder();
            for(BuyNLarge log : mBuyNLargeList){
                sb.append(log.toString());
            }
            mMainDisplay_textview.setText(sb.toString());
        }else{
            mMainDisplay_textview.setText(R.string.no_logs_yet);
        }
    }

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }
}