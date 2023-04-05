package com.example.buynlarge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.buynlarge.DB.AppDataBase;
import com.example.buynlarge.DB.ItemDAO;
import com.example.buynlarge.databinding.ActivityNewItemBinding;

public class NewItemActivity extends AppCompatActivity {

    private EditText mItemNameInput, mPriceInput, mQtyInput, mDescriptionInput;
    private Button mAddItem_Button;
    private ActivityNewItemBinding mNewItemBinding;
    private ItemDAO mItemDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        mNewItemBinding = ActivityNewItemBinding.inflate(getLayoutInflater());
        View view = mNewItemBinding.getRoot();
        setContentView(view);
        mItemDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME).allowMainThreadQueries().build().getItemDAO();

        mItemNameInput = mNewItemBinding.itemNameInput;
        mPriceInput = mNewItemBinding.priceInput;
        mQtyInput = mNewItemBinding.qtyInput;
        mDescriptionInput = mNewItemBinding.descriptionInput;
        mAddItem_Button = mNewItemBinding.addItemButton;

//        addNewItem();
    }

//    private void addNewItem(){
//        String itemName = mItemNameInput.getText().toString();
//        double price = Double.parseDouble(mPriceInput.getText().toString());
//        int quantity = Integer.parseInt(mQtyInput.getText().toString());
//        String description = mDescriptionInput.getText().toString();
//
//        Item newItem = new Item(itemName,price,quantity,description);
//        mItemDAO.insert(newItem);
//        Toast.makeText(NewItemActivity.this, "Item successfully created for: " + itemName, Toast.LENGTH_LONG).show();
//        Intent intent = ItemsActivity.getIntent(getApplicationContext());
//        startActivity(intent);
//    }

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context, NewItemActivity.class);
        return intent;
    }
}