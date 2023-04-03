package com.example.buynlarge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.buynlarge.databinding.ActivityItemsBinding;

public class ItemsActivity extends AppCompatActivity {

    ActivityItemsBinding mItemsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        mItemsBinding = ActivityItemsBinding.inflate(getLayoutInflater());
        View view = mItemsBinding.getRoot();
        setContentView(view);
    }

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context, ItemsActivity.class);
        return intent;
    }
}