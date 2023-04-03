package com.example.buynlarge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.buynlarge.databinding.ActivityAdminBinding;
import com.example.buynlarge.databinding.ActivityMainBinding;



public class AdminActivity extends AppCompatActivity {

    ActivityAdminBinding mAdminBinding;
    Button mViewItems;
    Button mViewUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        mAdminBinding = ActivityAdminBinding.inflate(getLayoutInflater());
        View view = mAdminBinding.getRoot();
        setContentView(view);

        mViewUsers = mAdminBinding.viewUsersButton;
        mViewItems = mAdminBinding.viewItemsButton;

        mViewUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = UsersActivity.getIntent(getApplicationContext());
                startActivity(intent);
            }
        });
    }

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context, AdminActivity.class);
        return intent;
    }
}